package com.evolutioneer.customappfeatures;

import java.util.ArrayList;

import oracle.adf.model.datacontrols.application.ApplicationFeatures;

import oracle.adfmf.framework.FeatureInformation;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;

public class CustomApplicationFeatures extends ApplicationFeatures {
    
    private static FeatureInformation[] _springboardFeatures = null;
    private static FeatureInformation[] _activeNavigationFeatures = null;
    private static String _activeFeatureId = null;
    private static CustomFeatureConfiguration _config = new CustomFeatureConfiguration();
    
    /**
     * 
     */
    public CustomApplicationFeatures() {
        super();
    }
    
    /**
     *
     * @return
     */
    public FeatureInformation[] getSpringboardFeatures() {
        
        System.out.println("+++ getSpringboardFeatures()");
        
        if(_springboardFeatures == null) {
            FeatureInformation[] input = getFeatures();
            ArrayList outputBuffer = new ArrayList();
            int inputCt = input.length;
            
            System.out.println("+++ Found features: " + inputCt);
            
            for(int i = 0; i < inputCt; i++) {
                FeatureInformation feature = input[i];
                if(_config.featureIsSpringboard(feature)) {
                    outputBuffer.add(feature);
                    System.out.println("+++ Adding springboard feature: " + feature.getId());
                }
            }
            
            _springboardFeatures = (FeatureInformation[]) outputBuffer.toArray(new FeatureInformation[outputBuffer.size()]);
        }
        
        return _springboardFeatures;
    }
    
    /**
     *
     * @return
     */
    public FeatureInformation[] getActiveNavigationFeatures() {
        
        System.out.println("+++ getActiveNavigationFeatures()");
        
        FeatureInformation _currentFeature = AdfmfContainerUtilities.getFeatureByName(AdfmfJavaUtilities.getFeatureName());
        System.out.println("+++ current feature ID: " + _currentFeature.getId());
        
        if(_currentFeature.getId().equals(_activeFeatureId) == false) {
            
            _activeFeatureId = _currentFeature.getId();

            FeatureInformation[] input = getFeatures();
            ArrayList outputBuffer = new ArrayList();
            int inputCt = input.length;
            
            System.out.println("+++ Found features: " + inputCt);
            
            for(int i = 0; i < inputCt; i++) {
                FeatureInformation feature = input[i];
                if(_config.featureIsChildOfFeature(feature, _activeFeatureId)) {
                    outputBuffer.add(feature);
                    System.out.println("+++ Adding navigation feature: " + feature.getId());
                }
            }
            
            _activeNavigationFeatures = (FeatureInformation[]) outputBuffer.toArray(new FeatureInformation[outputBuffer.size()]);
        
        }
        
        return _activeNavigationFeatures;
    }

    public String getActiveFeatureId() {
        return _activeFeatureId;
    }
}
