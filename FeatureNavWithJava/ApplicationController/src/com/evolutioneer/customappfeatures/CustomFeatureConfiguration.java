package com.evolutioneer.customappfeatures;

import java.util.ArrayList;
import java.util.Hashtable;

import oracle.adfmf.framework.FeatureInformation;

/**
 * CustomFeatureConfiguration
 *
 * This is a dummy object to return a parent/child mapping of features as well as
 * a list of which features are on the springboard.
 *
 * This class should be rewritten to check against a configuration file instead of
 * hard-coded constants.
 *
 */
public class CustomFeatureConfiguration {
    
    private static final String FEATURE_1 = "feature1";    
    private static final String FEATURE_2 = "feature2";    
    private static final String FEATURE_3 = "feature3";    
    private static final String FEATURE_4 = "feature4";    
    private static final String FEATURE_5 = "feature5";
    
    private static Hashtable _childToParent = null;
    private static ArrayList _springboard = null;
    
    public CustomFeatureConfiguration() {
        super();
    }
    
    /**
     * 
     */
    private static void init() {
        if(_childToParent == null) {
            _childToParent = new Hashtable();
            _childToParent.put(FEATURE_1, FEATURE_1);
            _childToParent.put(FEATURE_2, FEATURE_1);
            _childToParent.put(FEATURE_3, FEATURE_1);
            _childToParent.put(FEATURE_4, FEATURE_4);
            _childToParent.put(FEATURE_5, FEATURE_5);
        }
        
        if(_springboard == null) {
            _springboard = new ArrayList();
            _springboard.add(FEATURE_1);
            _springboard.add(FEATURE_2);
        }
    }
    
    /**
     *
     * @param info
     * @return
     */
    public boolean featureIsSpringboard(FeatureInformation info) {
        init();
        return _springboard.indexOf(info.getId()) > -1;
    }
    
    /**
     *
     * @param info
     * @param parentFeatureId
     * @return
     */
    public boolean featureIsChildOfFeature(FeatureInformation info, String parentFeatureId) {
        init();
        
        if(_childToParent.get(info.getId()) != null) {
            
            System.out.println("child: " + info.getId() 
                + "; mapping: " + _childToParent.get(info.getId()) 
                + "; passed parent: " + parentFeatureId);
            
            return _childToParent.get(info.getId()).equals(parentFeatureId);
        }
        
        return false;
    }
}
