package mobile.example;


import com.evolutioneer.activerecord.ActiveRecordProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import oracle.adfmf.amx.event.ValueChangeEvent;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class SessionDC extends ActiveRecordProvider {
    
    private static ArrayList _activeRecordSet = new ArrayList();
    private static Session[] _sessions = new Session[0];
    private static HashMap _dependencyCache = null;
    private static int _incompleteSessionCt = 0;

    public SessionDC() {
        super();
    }

    protected void register() {
        ah().registerParentProvider(EvalueeDC.class, this);
    }
    
    protected void initDependencyCache() {
        _dependencyCache = new HashMap();
        _dependencyCache.put(ah().getSelectedRecordNameForProvider(EvalueeDC.class), null);
    }

    protected void updateActiveRecordSet() {
        Evaluee evaluee = (Evaluee) ah().getSelectedRecordForProvider(EvalueeDC.class);
        
        Logger.log("Updating active record set with evaluee " + evaluee.getEvalueeName());
        
        //Populate our activeRecordSet 
        _activeRecordSet = new ArrayList();
        Random rand = new Random();
        int sessionCt = rand.nextInt(20) + 1;
        
        //Let's test the autoselect single record logic
        sessionCt = 1;
        
        for(int i = 0; i < sessionCt; i++) {
            _activeRecordSet.add(new Session(
                "Session " + i + " for " + evaluee.getEvalueeName(), 
                "" + i + "00" + evaluee.getEvalueeId(), "" + i, evaluee.getEvalueeId(), false
            ));
        }
        
        _sessions = (Session[]) _activeRecordSet.toArray(new Session[_activeRecordSet.size()]);
        Logger.log("+++ Complete");
    }

    protected void finalzeUpdateAndDispatchNotifications() {
        providerChangeSupport.fireProviderRefresh("sessions");
        checkForCompletion();
    }
    
    public void checkForCompletion() {
        //Check to see if all sessions are completed
        int sessionCt = _sessions.length;
        int incompleteSessionCt = 0;
        
        for(int i = 0; i < sessionCt; i++) {
            if(_sessions[i].isCompleted() == false) {
                incompleteSessionCt++;
            }
        }
        
        Logger.log("... found " + incompleteSessionCt + " incomplete sessions");
        setIncompleteSessionCt(incompleteSessionCt);
    }

    protected Object getIdFromRecord(Object record) {
        return ((Session) record).getSessionId();
    }
    
    public String getSelectedSessionId() {
        return (String) ah().getSelectedRecordValueForProvider(this.getClass());
    }
    
    public void setSelectedSessionId(String sessionId) {
        Logger.log("Setting selected session ID: " + sessionId);
        setSelectedRecordId(sessionId);
    }

    public Session[] getSessions() {
        checkForActiveRecordSetChanges();
        return _sessions;
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

    public void setIncompleteSessionCt(int _incompleteSessionCt) {
        int oldIncompleteSessionCt = SessionDC._incompleteSessionCt;
        SessionDC._incompleteSessionCt = _incompleteSessionCt;
        propertyChangeSupport.firePropertyChange("IncompleteSessionCt", oldIncompleteSessionCt, _incompleteSessionCt);
        AdfmfJavaUtilities.setELValue("#{applicationScope.incompleteSessionCt}", "" + _incompleteSessionCt);
    }

    public int getIncompleteSessionCt() {
        return _incompleteSessionCt;
    }

    public void onInputChange(ValueChangeEvent valueChangeEvent) {
        invalidate();
        checkForActiveRecordSetChanges();
    }
}
