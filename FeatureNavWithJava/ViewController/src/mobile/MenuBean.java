package mobile;

import java.util.Hashtable;

import oracle.adfmf.framework.FeatureContext;
import oracle.adfmf.framework.FeatureInformation;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class MenuBean {
    
    public static final String FEATURE_1 = "feature1";
    public static final String FEATURE_2 = "feature2";
    public static final String FEATURE_3 = "feature3";
    public static final String FEATURE_4 = "feature4";
    public static final String FEATURE_5 = "feature5";
    
    private Hashtable _parentOf = new Hashtable();
    
    /**
     * 
     */
    public MenuBean() {
        super();
        
        //1. Map children to parents
        //The following key/value pairs map features by name to their "parent"
        //Features 1, 2, and 3 are grouped under feature 1;
        //Features 4 and 5 are under feature 4
        
        _parentOf.put(FEATURE_1, FEATURE_1);
        _parentOf.put(FEATURE_2, FEATURE_1);
        _parentOf.put(FEATURE_3, FEATURE_1);
        _parentOf.put(FEATURE_4, FEATURE_4);
        _parentOf.put(FEATURE_5, FEATURE_4);
    }
    
    /**
     * Gets the ID of the currently active feature
     * @return
     */
    public String getCurrentFeatureId() {
        
        FeatureInformation info = AdfmfContainerUtilities.getFeatureByName(AdfmfJavaUtilities.getFeatureName());
        
        //TODO get the feature ID from the info and return it
        return null;
    }
    
    /**
     *  Gets the hashtable showing the child (key) to parent (value) mapping of our features.
     *  Parent = springboard, child = menu-button feature.
     * @return
     */
    public Hashtable getParentOf() {
        return _parentOf;
    }
}
