package com.android.volley;

/**
 * Created by Jason Chen on 2015/8/18.
 * 网络请求结果接收器，每一个网络线程处理者都要实现的接口，目的是为了做到区别请求者
 */
public interface Receiver<T> {

    public void onSucess(TaskHandle handle, T result);

    public void onError(TaskHandle handle, Throwable error);

}
