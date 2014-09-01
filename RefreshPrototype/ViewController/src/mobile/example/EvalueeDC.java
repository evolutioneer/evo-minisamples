package mobile.example;

import com.evolutioneer.activerecord.ActiveHierarchy;
import com.evolutioneer.activerecord.ActiveRecordProvider;

import java.util.ArrayList;

import java.util.HashMap;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeSupport;

public class EvalueeDC extends ActiveRecordProvider {
    
    private static ArrayList _activeRecordSet = new ArrayList();
    private static Evaluee[] _evaluees = new Evaluee[0];
    private static HashMap _dependencyCache = null;

    public EvalueeDC() {
        super();
    }

    protected void register() {
        //Evaluee has no active record parents
    }

    protected void initDependencyCache() {
        _dependencyCache = new HashMap();
        //Evaluee DC has no dependent values
    }

    protected void updateActiveRecordSet() {
        _activeRecordSet = new ArrayList();
        _activeRecordSet.add(new Evaluee("Abraham Abrams", "12345"));
        _activeRecordSet.add(new Evaluee("Benjamin Button", "23456"));
        _activeRecordSet.add(new Evaluee("Colleen Crawford", "34567"));
        _evaluees = (Evaluee[]) _activeRecordSet.toArray(new Evaluee[_activeRecordSet.size()]);
        
        Logger.log("Update complete, added " + _evaluees.length + " records");
    }

    protected void finalzeUpdateAndDispatchNotifications() {
        providerChangeSupport.fireProviderRefresh("evaluees");
    }

    protected Object getIdFromRecord(Object record) {
        return ((Evaluee) record).getEvalueeId();
    }
    
    public String getSelectedEvalueeId() {
        return (String) ah().getSelectedRecordForProvider(this.getClass());
    }
    
    public void setSelectedEvalueeId(String evalueeId) {
        Logger.log("Setting selected evaluee ID: " + evalueeId);
        try {
            setSelectedRecordId(evalueeId);
        }
        
        catch(Exception e) {
            Logger.log("!!! Exception while setting Id: " + e.getMessage(), e);
        }
    }

    public Evaluee[] getEvaluees() {
        checkForActiveRecordSetChanges();
        return _evaluees;
    }

    public ArrayList getActiveRecordSet() {
        return _activeRecordSet;
    }

    protected void resetActiveRecordSet() {
        _activeRecordSet = new ArrayList();
    }
    
    public HashMap getDependencyCache() {
        return _dependencyCache;
    }

    public void resetDependencyCache() {
        initDependencyCache();
    }
}
