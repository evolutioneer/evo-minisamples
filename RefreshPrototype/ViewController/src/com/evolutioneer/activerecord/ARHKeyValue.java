package com.evolutioneer.activerecord;


public class ARHKeyValue {
    
    private String _key;
    private ActiveRecordProvider _value;
    
    public ARHKeyValue() {
        super();
    }

    public ARHKeyValue(String _key, ActiveRecordProvider _value) {
        super();
        this._key = _key;
        this._value = _value;
    }

    public String getKey() {
        return _key;
    }

    public ActiveRecordProvider getValue() {
        return _value;
    }
    
    public boolean equals(Object obj) {
        
        if(obj instanceof ARHKeyValue == false) {
            return false;
        }
        
        ARHKeyValue kv = (ARHKeyValue) obj;
        
        return kv.getKey().equals(getKey()) 
               && kv.getValue().getBeanName().equals(getValue().getBeanName());
    }
}
