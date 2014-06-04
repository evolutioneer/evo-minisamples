package dc;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Widget {
    
    private int _widgetId;
    private String _widgetName;
    private String _widgetColor;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Widget() {
        super();
    }

    public Widget(int _widgetId, String _widgetName, String _widgetColor) {
        super();
        this._widgetId = _widgetId;
        this._widgetName = _widgetName;
        this._widgetColor = _widgetColor;
    }


    public void setWidgetId(int _widgetId) {
        int oldWidgetId = this._widgetId;
        this._widgetId = _widgetId;
        propertyChangeSupport.firePropertyChange("WidgetId", oldWidgetId, _widgetId);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getWidgetId() {
        return _widgetId;
    }

    public void setWidgetName(String _widgetName) {
        String oldWidgetName = this._widgetName;
        this._widgetName = _widgetName;
        propertyChangeSupport.firePropertyChange("WidgetName", oldWidgetName, _widgetName);
    }

    public String getWidgetName() {
        return _widgetName;
    }

    public void setWidgetColor(String _widgetColor) {
        String oldWidgetColor = this._widgetColor;
        this._widgetColor = _widgetColor;
        propertyChangeSupport.firePropertyChange("WidgetColor", oldWidgetColor, _widgetColor);
    }

    public String getWidgetColor() {
        return _widgetColor;
    }
}
