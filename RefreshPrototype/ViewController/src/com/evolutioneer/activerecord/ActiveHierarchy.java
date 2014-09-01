package com.evolutioneer.activerecord;

import java.util.ArrayList;

import mobile.example.Logger;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;

import org.apache.commons.lang.StringUtils;

public class ActiveHierarchy {
    
    public static final String EL_SELECTED_RECORD_SUFFIX = "_selectedRecordId";
    
    private static ActiveHierarchy _instance;
    private ArrayList _parentToChild = new ArrayList();
    
    
    /**
     * Singleton constructor
     */
    private ActiveHierarchy() {
        super();
    }
    
    /**
     * Singleton accessor method
     * @return
     */
    public static ActiveHierarchy getInstance() {
        if(_instance == null) {
            _instance = new ActiveHierarchy();
        }
        
        return _instance;
    }
    
    /**
     * Registers a child ActiveRecordProvider to a parent ActiveRecordProvider to which it has a
     * refresh dependency.  When the parent provider refreshes, the child provider will be refreshed.
     * Child providers may have multiple parents and will be refreshed in the order in which they are
     * added via this method call.
     * 
     * If the parentProvider is null then the childProvider is considered a root-level provider.
     * @param parentProviderClass
     * @param childProvider
     */
    public void registerParentProvider(Class parentProviderClass, ActiveRecordProvider childProvider) {
        String parentClassName = getClassNameWithoutPackage(parentProviderClass);
        ARHKeyValue kv = new ARHKeyValue(parentClassName, childProvider);
        
        //For now, do not register duplicate mappings
        if(_parentToChild.contains(kv)) {
            return;
        }
        
        Logger.log("Registering " + childProvider.getBeanName() + " under parent " + parentClassName);
        _parentToChild.add(kv);
    }
    
    /**
     * Returns the selected record for the passed active record provider class, if any
     * @param activeRecordProviderClass
     * @return
     */
    public Object getSelectedRecordForProvider(Class activeRecordProviderClass) {
        ActiveRecordProvider arp = getProviderWithClass(activeRecordProviderClass);
        Logger.log("Getting active record for provider: " + arp);
        return arp.getSelectedRecord();
    }
    
    /**
     * Returns an active record provider defined in application scope by its class name
     * @param activeRecordProviderClass
     * @return
     */
    public ActiveRecordProvider getProviderWithClass(Class activeRecordProviderClass) {
        return (ActiveRecordProvider) AdfmfJavaUtilities.evaluateELExpression("#{applicationScope." + getClassNameWithoutPackage(activeRecordProviderClass) + "}");
    }
    
    /**
     * Returns an EL expression for getting the selected record ID for this ActiveRecordProvider from EL context
     * @return
     */
    public String getSelectedRecordNameForProvider(Class activeRecordProviderClass) {
        return "#{applicationScope." + getClassNameWithoutPackage(activeRecordProviderClass) + EL_SELECTED_RECORD_SUFFIX + "}";
    }
    
    /**
     * Returns the selected record ID for this ActiveRecordProvider from EL context
     * @return
     */
    public Object getSelectedRecordValueForProvider(Class activeRecordProviderClass) {
        return AdfmfJavaUtilities.evaluateELExpression(getSelectedRecordNameForProvider(activeRecordProviderClass));
    }
    
    /**
     * 
     * @param provider
     */
    public void refreshWithProvider(ActiveRecordProvider provider) {
        Logger.log("+++ Updating children of " + provider.getBeanName());
        
        //Iterate over our parent-to-child mapping and invoke the dependent providers in order
        ArrayList refreshedChildProviders = new ArrayList();
        int mappingCt = _parentToChild.size();
        
        for(int i = 0; i < mappingCt; i++) {
            
            ARHKeyValue kv = (ARHKeyValue)_parentToChild.get(i);
            
            //If the key is not equal to this provider's class, skip it
            if(kv.getKey().equals(provider.getBeanName()) == false) {
                continue;
            }

            ActiveRecordProvider child = kv.getValue();

            Logger.log("+++ Updating child " + child.getBeanName());
            boolean didUpdate = child.checkForActiveRecordSetChanges();
            
            if(didUpdate) {
                refreshedChildProviders.add(child);
            }
        }
        
        //If any child providers fired a refresh, iterate over them now and call refreshWithProvider on each
        //This secondary loop allows us to defer the refresh of child providers, effectively creating a
        //breadth-first refresh process instead of a depth-first one
        int refreshedChildCt = refreshedChildProviders.size();
        for(int i = 0; i < refreshedChildCt; i++) {
            ActiveRecordProvider child = (ActiveRecordProvider)refreshedChildProviders.get(i);
            Logger.log("+++ Updating children of child " + child.getBeanName());
            refreshWithProvider(child);
        }
    }
    
    /**
     * Utility function to get the name of a class without its package prefix
     */
    public String getClassNameWithoutPackage(Class className) {
        String[] nameArray = StringUtils.split(className.getName(), ".");
        return nameArray[nameArray.length - 1];
    }
}
