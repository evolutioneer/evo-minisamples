package dc;

import java.math.BigDecimal;

import java.util.ArrayList;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.exception.AdfInvocationException;
import oracle.adfmf.util.GenericType;


public class WidgetDC {
    
    private static Widget[] _widgets;
    private static final String DC_NAME = "WebService";
    private static final String FIND_FUNC_NAME = "findWidgetView1";
    
    public WidgetDC() {
        super();
    }
    
    /**
     *
     * @return
     */
    public Widget[] getWidgets() {
        //Return a list of widgets
        
        if(_widgets == null) {
            findFromWebService();
        }
        
        return _widgets;
    }
    
    /**
     * 
     */
    protected void findFromWebService() {
        
        //Call the web service findWidgetView1
        //and populate the _widgets ArrayList with the results.
        
        GenericType output = null;
        ArrayList tempList = new ArrayList();
        
        try {
            output =
                    (GenericType) AdfmfJavaUtilities.invokeDataControlMethod(
                DC_NAME,
                null,
                FIND_FUNC_NAME, new ArrayList(), new ArrayList(), new ArrayList());
        } catch (AdfInvocationException e) {
            _widgets = new Widget[0];
            return;
        }
        
        //Parse through the GenericType and turn the response into widgets
        int rowCt = output.getAttributeCount();
        
        for(int i = 0; i < rowCt; i++) {
            //Parse this row of the response
            GenericType row = (GenericType) output.getAttribute(i);
            
            Widget widget = new Widget(
                ((BigDecimal) row.getAttribute("WidgetId")).intValue(), 
                (String) row.getAttribute("WidgetName"), 
                (String) row.getAttribute("WidgetColor") 
            );
            
            //Lastly, add to the static widgets array
            tempList.add(widget);
        }
        
        //Cast the temporary ArrayList into the static Widget[] array
        //String[] arr = list.toArray(new String[list.size()]);
        _widgets = (Widget[])tempList.toArray(new Widget[tempList.size()]);
        
        //We're done
    }
}
