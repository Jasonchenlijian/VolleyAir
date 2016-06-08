package com.volley.air;

import android.content.Context;

import com.volley.air.base.ApplicationController;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 网络请求工具类
 */
public class VolleyAir {


    public static final int RequestWithoutLogin = HttpProcessException.createCustomErrorCode();
    public static final int BadResponse = HttpProcessException.createCustomErrorCode();
    public static final int BadMethod = HttpProcessException.createCustomErrorCode();

    private Context context;
    private ReentrantLock loginLock;
    private TaskCenter center;

    private VolleyPostString volleyPostString;
    private OauthVolleyPostString oauthVolleyPostString;
    private VolleyPostMultipart volleyPostMultipart;


    public VolleyAir(ApplicationController app) {
        context = app.getApplicationContext();
        loginLock = new ReentrantLock();
        center = new TaskCenter();

        volleyPostString = new VolleyPostString();
        oauthVolleyPostString = new OauthVolleyPostString();
        volleyPostMultipart = new VolleyPostMultipart();
    }








}
