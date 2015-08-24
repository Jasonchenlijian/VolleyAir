package com.android.volley;

import android.content.Context;

import com.android.volley.base.ApplicationController;
import com.android.volley.base.NetworkResponse;
import com.android.volley.base.Request;
import com.android.volley.base.RequestTickle;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.VolleyTickle;

import java.util.Iterator;
import java.util.Map;

/**
 * volley的二次封装
 */
public class VolleyRequest {
    public static StringRequest stringRequest;
    public static SimpleMultiPartRequest simpleMultiPartRequest;
    public static Context context;

    /**
     * Get的StringRequest(通过接口回调处理成功和失败的结果)
     */
    public static void RequestGet(Context context, String url, String tag, VolleyInterface vif) {

        ApplicationController.cancelPendingRequests(tag);
        stringRequest = new StringRequest(Request.Method.GET, url, vif.loadingListener(), vif.errorListener());
        stringRequest.setTag(tag);
        ApplicationController.getRequestQueue().add(stringRequest);
    }

    /**
     * Post的StringRequest(通过接口回调处理成功和失败的结果)
     */
    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyInterface vif) {
        ApplicationController.cancelPendingRequests(tag);
        stringRequest = new StringRequest(Request.Method.POST, url, vif.loadingListener(), vif.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        ApplicationController.getRequestQueue().add(stringRequest);
    }

    /**
     * Post的StringRequest(直接解析结果)
     */
    public static NetworkResponse RequestPostTickle(Context context, String url, String tag, final Map<String, String> params, VolleyInterface vif) {
        RequestTickle mRequestTickle = VolleyTickle.newRequestTickle(context);
        stringRequest = new StringRequest(Request.Method.POST, url, null, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        mRequestTickle.add(stringRequest);
        NetworkResponse response = mRequestTickle.start();
        return response;
    }


    /**
     * Post的MultipartRequest(文本，图片，文件)(直接解析结果)
     */
    public static NetworkResponse RequestMultipartTickle(Context context, String url, String tag,
                                                         Map<String, String> map_string, Map<String, String> map_file, VolleyInterface vif) {

        RequestTickle mRequestTickle = VolleyTickle.newRequestTickle(context);

        simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, url, null, null);
        if (map_string != null) {
            Iterator iter = map_string.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                simpleMultiPartRequest.addMultipartParam((String) key, null, (String) val);
            }
        }
        if (map_file != null) {
            Iterator iter = map_file.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                simpleMultiPartRequest.addFile((String) key, (String) val);
            }
        }
        simpleMultiPartRequest.setTag(tag);
        mRequestTickle.add(simpleMultiPartRequest);
        NetworkResponse response = mRequestTickle.start();
        return response;
    }

}
