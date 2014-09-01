package com.evolutioneer.activerecord;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Iterator;

import mobile.example.Logger;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;

import org.apache.commons.lang.StringUtils;

public abstract class ActiveRecordProvider {
    
    private String _beanName = null;
    
    private boolean _autoSelectSingleActiveRecord = true;
    private boolean _updating = false;
    private boolean _firstLoad = true;
    
    protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected transient ProviderChangeSupport providerChangeSupport = new ProviderChangeSupport(this);
    
    public ActiveRecordProvider() {
        super();
        register();
        initDependencyCache();
    }
    
    /**
     * Sugar method for easy access to the ActiveHierarchy singleton
     * @return
     */
    protected ActiveHierarchy ah() {
        return ActiveHierarchy.getInstance();
    }
    
    /**
     * Returns the application-Scoped Bean name of this ActiveRecordProvider.
     * @return
     */
    public String getBeanName() {
        if(_beanName == null) {
            _beanName = ah().getClassNameWithoutPackage(this.getClass());
        }
        
        return _beanName;
    }
        
    /**
     * Template method for subclasses to register this ActiveRecordProvider under its parent providers
     * with the ActiveHierarchy.
     */
    protected abstract void register();
    
    
    /**
     * Template method for subclasses to define their dependencies by setting keys on the dependency cache
     */
    protected abstract void initDependencyCache();
    
    /**
     * Invoked when dependent data may have changed and this ActiveRecordProvider must subsequently
     * refresh its active record set and/or selected record.
     * @return boolean true if a refresh took place, requiring dependent providers to refresh; false if no refresh
     */
    public boolean checkForActiveRecordSetChanges() {
        
        try {
            if(isDirty() == false) {
                Logger.log("Not dirty; returning false");
                return false;
            }
            
            //Set our updating state flag to prevent unwanted recursions
            _updating = true;
            
            //Update our local dependency cache with values from the EL context
            updateDependencyCacheFromEL();
            
            //If we do not have valid dependencies, empty our active record set.  If this was a data change, return true
            if(assertValidDependencies() == false) {
                Logger.log("Dependencies are not valid and thus cannot retrieve active records");
                
                if(getActiveRecordSet().size() > 0) {
                    resetActiveRecordSet();
                    _updating = false;
                    return true;
                }
                
                else {
                    _updating = false;
                    return false;
                }
            }
            
            //Having gotten this far, we have the data we need to refresh our active record set
            updateActiveRecordSet();
            
            //If our update returned exactly 1 record and we are auto-selecting single-recordsets, handle this now
            if(isAutoSelectSingleActiveRecord() && getActiveRecordSet().size() == 1) {
                doAutoSelectSingleRecordSet();
            }
            
            //Complete the update by calling our finalizer
            finalzeUpdateAndDispatchNotifications();
            
            //Unset our updating state flag
            _updating = false;
            _firstLoad = false;
            return true;
        }
        
        catch(Exception e) {
            Logger.log("!!! Exception during update: " + e, e);
            return false;
        }
    }
    
    
    /**
     * Updates the local active record set based on the dependency values.  In ActiveRecordProvider subclasses
     * that handle data entry type records, updateActiveRecordSet() is the appropriate place to use a participating
     * ActiveRecordProvider that gives definition objects that must be "instanced" as corresponding data entry
     * records on this provider.  For example, in the QuestionProvider, updateActiveRecordSet() would iterate
     * over the QuestionDefinitionProvider.getActiveRecordSet() values and verify the existence of a Question data
     * entry object for each definition.  The resulting Question objects would be saved as the active record set
     * for the QuestionProvider.
     */
    protected abstract void updateActiveRecordSet();
    
    
    /**
     * Template for subclasses to fire refresh providers and reset state after a successful record set update
     */
    protected abstract void finalzeUpdateAndDispatchNotifications();
    
    /** 
     * Template method to allow subclasses to easily return the id from a provided record.
     */
    protected abstract Object getIdFromRecord(Object record);
    
    /**
     * Template method to handle the automatic selection the only active record in a 1-record set
     */
    protected void doAutoSelectSingleRecordSet() {
        
        if(getActiveRecordSet() == null || getActiveRecordSet().size() != 1) {
            Logger.log("Did not find exactly one record in active record set; returning");
            return;
        }
        
        setSelectedRecordId(getIdFromRecord(getActiveRecordSet().get(0)));
    }
    
    /**
     * Validates that the passed recordId is in the getRecords() set (or is null), then
     * invokes ActiveHierarchy.refreshWithProvider().
     * @param recordId
     */
    public void setSelectedRecordId(Object recordId) {
        
        Logger.log("+++ Setting selected record ID: " + recordId);
        
        //Do not select the record if the key is invalid
        if(getRecordWithId(recordId) == null) {
            Logger.log("Could not find a matching record; canceling selection");
            return;
        }
        
        try {
            String elExpression = ah().getSelectedRecordNameForProvider(this.getClass());
            Logger.log("setting " + recordId + " to " + elExpression);
            
            //Set the value based on the syntax provided by the ActiveHierarchy
            AdfmfJavaUtilities.setELValue(elExpression, recordId);
            
            //If we are not in the midst of an update, begin a refresh with the ActiveHierarchy
            if(!_updating) {
                ah().refreshWithProvider(this);
            }
        }
        
        catch(Exception e) {
            Logger.log("Error during selection set operation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Returns the selected record ID as stored in the EL context
     * @return
     */
    public Object getSelectedRecordId() {
        return ah().getSelectedRecordValueForProvider(this.getClass());
    }
    
    /**
     * Returns the selected record, as identified by the ID stored in EL context
     * @return
     */
    public Object getSelectedRecord() {
        return getRecordWithId(getSelectedRecordId());
    }
    
    /**
     * Using keys in the dependencyCache as EL statements, reads values from EL context and updates the
     * corresponding vlues in the dependencyCache object
     */
    protected void updateDependencyCacheFromEL() {
        
        if(getDependencyCache() == null) {
            return;
        }
        
        Iterator entries = getDependencyCache().entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            String elKey = (String) thisEntry.getKey();
            
            Object elValue = AdfmfJavaUtilities.evaluateELExpression(elKey);
            Logger.log(getBeanName() + " dependency cache is updating key " + elKey + " to value '" + elValue + "'");
            
            getDependencyCache().remove(elKey);
            getDependencyCache().put(elKey, elValue);
        }
    }
    
    /**
     * Validates that all input dependencies are non-null
     * @return
     */
    protected boolean assertValidDependencies() {
        
        if(getDependencyCache() == null) {
            return false;
        }
        
        Iterator entries = getDependencyCache().entrySet().iterator();
        boolean foundDependencyRule = false;
        
        while (entries.hasNext()) {
            foundDependencyRule = true;
            Entry thisEntry = (Entry) entries.next();
            String elKey = (String) thisEntry.getKey();
            Object elValue = thisEntry.getValue();
            
            Logger.log("Checking valid dependency: " + elKey + " = '" + elValue + "'");
            
            if(elValue == null) {
                Logger.log("Assert valid dependencies failed, key returned null: " + elKey);
                return false;
            }
        }
        
        if(foundDependencyRule == false) {
            Logger.log("!!! Warning: failed to find any dependencies, so validation automatically passes");
        }
        
        return true;
    }
    
    /**
     * Template method for subclasses to determine if 
     * @return
     */
    protected boolean isDirty() {
        
        if(getDependencyCache() == null) {
            Logger.log("Dependency cache is null; must at least be initialized to empty set; returning false");
            return false;
        }
        
        if(_updating) {
            Logger.log("Never dirty while updating = true; return false");
            return false;
        }
        
        if(_firstLoad) {
            Logger.log("Provider is always dirty on first load, returning true");
            return true;
        }
        
        Iterator entries = getDependencyCache().entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            String elKey = (String) thisEntry.getKey();
            Object elCachedValue = thisEntry.getValue(); 
            
            Object elValue = AdfmfJavaUtilities.evaluateELExpression(elKey);
            
            Logger.log("Checking dirty state for " + elKey + ": cached = " + elCachedValue + ", el: " + elValue);
            
            if(elValue == null || elValue.equals(elCachedValue) == false) {
                Logger.log("Found a changed key in EL scope relative to cache: " + elKey);
                return true;
            }
        }
        
        Logger.log("Found no difference in cached and EL values; not dirty");
        return false;
    }
    
    /**
     * Returns an ArrayList of this ActiveRecordProvider's active records
     * @return
     */
    protected abstract ArrayList getActiveRecordSet();
    protected abstract void resetActiveRecordSet();
    
    /**
     * Clears the local dependencyCache, which will effectively force this data provider
     * to run the entire refresh process when refreshActiveRecord() is next invoked
     */
    public void invalidate() {
        Logger.log("Invalidating dependencies for provider: " + getBeanName());
        resetDependencyCache();
    }
    
    /**
     * Returns a record from getRecords() with an id matching the passed recordId, or null
     * if no record is found
     * @param recordId
     * @return
     */
    public Object getRecordWithId(Object recordId) {
        
        if(recordId == null) {
            Logger.log("Cannot get record with null id; returning null");
            return null;
        }
        
        int recordCt = getActiveRecordSet().size();
        Logger.log("Getting record with id: " + recordId + "; searching " + recordCt + " records");
        
        for(int i = 0; i < recordCt; i++) {
            Object record = getActiveRecordSet().get(i);
            if(recordId.equals(getIdFromRecord(record))) {
                Logger.log("+++ Record found: " + record);
                return record;
            }
        }
        
        Logger.log("No record found; returning null");
        return null;
    }

    public void setAutoSelectSingleActiveRecord(boolean _autoSelectSingleActiveRecord) {
        this._autoSelectSingleActiveRecord = _autoSelectSingleActiveRecord;
    }

    public boolean isAutoSelectSingleActiveRecord() {
        return _autoSelectSingleActiveRecord;
    }
    
    public abstract HashMap getDependencyCache();
    
    public abstract void resetDependencyCache();
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    public void addProviderChangeListener(ProviderChangeListener l) {
        providerChangeSupport.addProviderChangeListener(l);
    }
    
    public void removeProviderChangeListener(ProviderChangeListener l) {
        providerChangeSupport.removeProviderChangeListener(l);
    }
}
