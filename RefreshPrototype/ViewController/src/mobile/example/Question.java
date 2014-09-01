package mobile.example;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Question {
    
    private String _questionId;
    private String _questionName;
    private String _questionAnswer;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Question() {
        super();
    }

    public Question(String _questionName, String _questionId, String _questionAnswer) {
        super();
        this._questionId = _questionId;
        this._questionName = _questionName;
        this._questionAnswer = _questionAnswer;
    }

    public String getQuestionId() {
        return _questionId;
    }

    public String getQuestionName() {
        return _questionName;
    }

    public void setQuestionAnswer(String _questionAnswer) {
        String oldQuestionAnswer = this._questionAnswer;
        this._questionAnswer = _questionAnswer;
        propertyChangeSupport.firePropertyChange("QuestionAnswer", oldQuestionAnswer, _questionAnswer);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getQuestionAnswer() {
        return _questionAnswer;
    }
}
