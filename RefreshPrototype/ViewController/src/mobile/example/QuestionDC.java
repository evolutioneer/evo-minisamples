package mobile.example;

import com.evolutioneer.activerecord.ActiveRecordProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.el.ELContext;

import javax.el.ELResolver;

import oracle.adfmf.amx.event.ValueChangeEvent;
import oracle.adfmf.framework.AdfChannel;
import oracle.adfmf.framework.FeatureContext;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class QuestionDC extends ActiveRecordProvider {
    
    private static ArrayList _activeRecordSet = new ArrayList();
    private static Question[] _questions = new Question[0];
    private static HashMap _dependencyCache = null;
    private static int _incompleteQuestionCt = 0;

    public QuestionDC() {
        super();
    }

    protected void register() {
        ah().registerParentProvider(SessionDC.class, this);
    }

    protected void initDependencyCache() {
        _dependencyCache = new HashMap();
        _dependencyCache.put(ah().getSelectedRecordNameForProvider(SessionDC.class), null);
    }

    protected void updateActiveRecordSet() {
        Session session = (Session) ah().getSelectedRecordForProvider(SessionDC.class);
        
        Logger.log("Updating active record set with session " + session.getSessionName());
        
        //Populate our activeRecordSet 
        _activeRecordSet = new ArrayList();
        
        Random rand = new Random();
        int questionCt = rand.nextInt(3) + 1;
        
        for(int i = 0; i < questionCt; i++) {
            _activeRecordSet.add(new Question(
                "Question " + i + " for " + session.getSessionName(), 
                "" + i + "00" + session.getSessionId(), ""
            ));
        }
        
        _questions = (Question[]) _activeRecordSet.toArray(new Question[_activeRecordSet.size()]);
    }
    
    protected void finalzeUpdateAndDispatchNotifications() {
        providerChangeSupport.fireProviderRefresh("questions");
        checkForCompletion();
    }

    public void checkForCompletion() {
        //Check to see if all questions are completed
        int questionCt = _questions.length;
        int incompleteQuestionCt = 0;

        Logger.log("Validating " + questionCt + " questions for completion");
        
        for(int i = 0; i < questionCt; i++) {
            if(_questions[i].getQuestionAnswer().equals("")) {
                incompleteQuestionCt++;
            }
        }
        
        Logger.log("... found " + incompleteQuestionCt + " incomplete questions");
        setIncompleteQuestionCt(incompleteQuestionCt);
        
        //Check our active session for completion.  If we're setting it to a new value,
        //invalidate the provider and force it to refresh.
        SessionDC sessionDC = (SessionDC) ah().getProviderWithClass(SessionDC.class);
        Session session = (Session) sessionDC.getSelectedRecord();
        boolean sessionQuestionsCompleted = session.isQuestionsCompleted();
        
        session.setQuestionsCompleted(incompleteQuestionCt == 0);
        if(incompleteQuestionCt == 0 != sessionQuestionsCompleted) {
            sessionDC.invalidate();
            sessionDC.checkForActiveRecordSetChanges();
        }
    }

    protected Object getIdFromRecord(Object record) {
        return ((Question)record).getQuestionId();
    }
    
    public String getSelectedQuestionId() {
        return (String) ah().getSelectedRecordForProvider(this.getClass());
    }
    
    public void setSelectedQuestionid(String questionId) {
        Logger.log("Setting selected question ID: " + questionId);
        setSelectedRecordId(questionId);
    }

    public Question[] getQuestions() {
        checkForActiveRecordSetChanges();
        return _questions;
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

    public void setIncompleteQuestionCt(int _incompleteQuestionCt) {
        int oldIncompleteQuestionCt = QuestionDC._incompleteQuestionCt;
        QuestionDC._incompleteQuestionCt = _incompleteQuestionCt;
        propertyChangeSupport.firePropertyChange("IncompleteQuestionCt", oldIncompleteQuestionCt, _incompleteQuestionCt);
        AdfmfJavaUtilities.setELValue("#{applicationScope.incompleteQuestionCt}", "" + _incompleteQuestionCt);

    }
    
    public int getIncompleteQuestionCt() {
        return _incompleteQuestionCt;
    }

    public void onQuestionAnswerChange(ValueChangeEvent valueChangeEvent) {
        //QuestionDC bean = (QuestionDC) AdfmfJavaUtilities.getDataControlProvider(getBeanName());
        //bean.validateAnswers();
        checkForCompletion();
    }
}
