package mobile.example;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Session {
    
    private String _sessionName;
    private String _sessionId;
    private String _sessionDefId;
    private String _evalueeId;
    private boolean _completed;
    private boolean _questionsCompleted;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Session() {
        super();
    }

    public Session(String _sessionName, String _sessionId, String _sessionDefId, String _evalueeId, boolean _completed) {
        super();
        this._sessionName = _sessionName;
        this._sessionId = _sessionId;
        this._sessionDefId = _sessionDefId;
        this._evalueeId = _evalueeId;
        this._completed = _completed;
    }

    public String getSessionName() {
        return _sessionName;
    }

    public String getSessionId() {
        return _sessionId;
    }

    public String getSessionDefId() {
        return _sessionDefId;
    }
    
    public String getEvalueeId() {
        return _evalueeId;
    }
    
    public boolean isCompleted() {
        return _completed;
    }

    public void setQuestionsCompleted(boolean _questionsCompleted) {
        boolean oldQuestionsCompleted = this._questionsCompleted;
        this._questionsCompleted = _questionsCompleted;
        propertyChangeSupport.firePropertyChange("QuestionsCompleted", oldQuestionsCompleted, _questionsCompleted);
    }
    
    public boolean isQuestionsCompleted() {
        return _questionsCompleted;
    }

    public void setCompleted(boolean _completed) {
        boolean oldCompleted = this._completed;
        this._completed = _completed;
        propertyChangeSupport.firePropertyChange("Completed", oldCompleted, _completed);
    }
}
