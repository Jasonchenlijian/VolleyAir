package com.android.volley;

import java.util.HashMap;


/**
 * Created by Jason Chen on 2015/8/13.
 * <p/>
 * Volley网络请求的参数
 */
public class HttpRequest {

    private String requestTag;
    private String url;
    private HashMap<String, String> hashMap;
    private HashMap<String, String> hashMap_file;

    //构造函数(有URL才能构造哦！没有URL地址你还请求个蛋？)
    public HttpRequest(String url) {
        this.url = url;
        requestTag = null;
    }

    //添加HashMap参数
    public void addParameter(String name, String value) {
        if (null == hashMap)
            hashMap = new HashMap<String, String>();
        hashMap.put(name, value);
    }

    //获取HashMap键值
    public String getParameter(String name) {
        if (null != hashMap) {
            return hashMap.get(name);
        }
        return null;
    }

    //添加文件参数
    public void addFile(String name, String value) {
        if (null == hashMap_file)
            hashMap_file = new HashMap<String, String>();
        hashMap_file.put(name, value);
    }

    //获取文件
    public String getFile(String name) {
        if (null != hashMap_file) {
            return hashMap_file.get(name);
        }
        return null;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap<String, String> getHashMap_file() {
        return hashMap_file;
    }

    public void setHashMap_file(HashMap<String, String> hashMap_file) {
        this.hashMap_file = hashMap_file;
    }

}
