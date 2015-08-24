package com.android.volley;

import android.content.Context;

import com.android.volley.base.Response;
import com.android.volley.error.VolleyError;

/**
 * Volley请求成功和请求失败的接口
 */
public abstract class VolleyInterface {
    public Context context;
    public static Response.Listener<String> listener;
    public static Response.ErrorListener errorListener;

    public abstract void onMySuccess(String result);
    public abstract void onMyError(VolleyError error);

    public VolleyInterface (Context context, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this.context = context;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    public Response.Listener<String> loadingListener() {
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onMySuccess(response);
            }
        };
        return listener;
    }

    public Response.ErrorListener errorListener() {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onMyError(error);
            }
        };
        return errorListener;
    }
}
