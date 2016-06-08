package com.volley.air;


public class IDData {
    public int code;
    public Object data;

    public IDData(int code, Object data){
        this.code = code;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T tryToGet(Class<T> klass){
        if(klass.isInstance(data)){
            return (T)data;
        }
        return null;
    }
}
