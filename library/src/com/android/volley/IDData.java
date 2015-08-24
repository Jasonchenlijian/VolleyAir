package com.android.volley;

/**
 * Created by Jason on 2015/8/15.
 */
public class IDData {
    public int code;
    public Object data;

    public IDData(int code, Object data){
        this.code = code;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T tryToGet(Class<T> klass){
        if(klass.isInstance(data)){		//isInstance(Object obj)方法，obj是被测试的对象，如果obj是调用这个方法的class或接口 的实例，则返回true。
            return (T)data;
        }
        return null;
    }
}
