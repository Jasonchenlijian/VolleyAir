package com.android.volley;

/**
 * Created by Jason Chen on 2015/8/18.
 * CHBNetworkModule中所有网络请求类实现的接口，目的是为了做到能够统一访问这些请求类
 */
public interface Processor<T> {
    public T process(HttpRequest request) throws Throwable;
}
