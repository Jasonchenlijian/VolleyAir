package com.chenlijian.volleyair;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.volley.air.HttpProcessException;
import com.volley.air.HttpRequest;
import com.volley.air.Processor;
import com.volley.air.TaskCenter;
import com.volley.air.TaskHandle;
import com.volley.air.VolleyRequest;
import com.volley.air.base.NetworkResponse;
import com.volley.air.toolbox.VolleyTickle;
import com.chenlijian.volleyair.bean.LoginState;
import com.chenlijian.volleyair.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by chenlijian on 2015/8/12.
 * 网络模块
 */
public class NetworkModule {

    public static final int RequestWithoutLogin = HttpProcessException.createCustomErrorCode();
    public static final int BadResponse = HttpProcessException.createCustomErrorCode();
    public static final int BadMethod = HttpProcessException.createCustomErrorCode();

    private static final Boolean DEBUG = true;

    private static final String API_URL = "http://api.chihuo888.com/";
    public static final String ACTION_USER_LOGIN_CHANGED = "action.com.chenlijian.volleyair.loginStateChanged";


    /****************************************************Volley请求***********************************************/

    /**
     * Volley的String类型的Post请求，成功返回请求Tag和包含JSON内容的CHBRsp,失败返回VolleyError
     */
    private class VolleyPostString implements Processor<DataModule> {

        @Override
        public DataModule process(final HttpRequest request) throws HttpProcessException {

            if (DEBUG)
                Log.e("post", request.getUrl() + (request.getHashMap() == null ? "" : request.getHashMap().toString()));

            NetworkResponse response = VolleyRequest.RequestPostTickle(context,
                    request.getUrl(), request.getRequestTag(), request.getHashMap(), null);

            if (response.statusCode == 200) {
                String result = VolleyTickle.parseResponse(response);
                try {
                    if (DEBUG) Log.e("response", request.getUrl() + result);

                    JSONObject obj = new JSONObject(result);

                    return new DataModule(obj.getInt("code"),
                            obj.getString("str"),
                            obj.isNull("data") ? null : obj.get("data"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * 需要身份认证的请求类
     */
    private class OauthVolleyPostString implements Processor<DataModule> {
        @Override
        public DataModule process(HttpRequest request) throws HttpProcessException {
            // TODO Auto-generated method stub

            request.addParameter("token", getAccessToken(null));

            DataModule rsp = volleyPostString.process(request);

            if (rsp.str().equals("token验证失败")) {
                logoutUser();
                throw new HttpProcessException("token验证失败!", RequestWithoutLogin);
            }
            return rsp;
        }
    }

    /**
     * 有后续process请求类(比如登陆成功后请求用户用户信息)
     */
    private class NameVolleyPostString implements Processor<DataModule> {

        private String methodName;
        private Processor<DataModule> p;

        NameVolleyPostString(String name, Processor<DataModule> processor) {
            methodName = name;
            p = processor;
        }

        @Override
        public DataModule process(HttpRequest request) throws HttpProcessException {
            try {
                DataModule rsp = p.process(request);
                Method method = NetworkModule.class.getMethod(methodName, DataModule.class, HttpRequest.class);
                return (DataModule) method.invoke(NetworkModule.this, rsp, request);

            } catch (Throwable e) {
                if (e instanceof HttpProcessException)
                    throw (HttpProcessException) e;
                else if (e instanceof InvocationTargetException) {
                    throw new HttpProcessException(((InvocationTargetException) e).getTargetException(),
                            "invoke error:" + methodName, HttpProcessException.ReadResponseError);
                } else {
                    e.printStackTrace();
                    throw new HttpProcessException(e, "bad method:" + methodName, BadMethod);
                }
            }
        }
    }


    /**
     * Volley的复合类型的Post请求，成功返回请求Tag和包含JSON内容的CHBRsp,失败返回VolleyError
     */
    private class VolleyPostMultipart implements Processor<DataModule> {

        @Override
        public DataModule process(final HttpRequest request) throws Throwable {

            if (DEBUG)
                Log.e("post_multipart", request.getUrl() + (request.getHashMap_file() == null ? "" : request.getHashMap_file().toString()));

            NetworkResponse response = VolleyRequest.RequestMultipartTickle(context, request.getUrl(),
                    request.getRequestTag(), request.getHashMap(), request.getHashMap_file(), null);

            if (response.statusCode == 200) {
                String result = VolleyTickle.parseResponse(response);
                try {
                    if (DEBUG) Log.e("response_multipart", request.getUrl() + result);

                    JSONObject obj = new JSONObject(result);

                    DataModule rsp = new DataModule(obj.getInt("code"),
                            obj.getString("str"),
                            obj.isNull("data") ? null : obj.get("data"));
                    if (rsp.str().equals("token验证失败")) {
                        logoutUser();
                    }
                    return rsp;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }







    /***************************************
     *inner
     **********************************************/

    private Context context;
    private LoginState loginState;
    private ReentrantLock loginLock;
    private TaskCenter center;

    private VolleyPostString volleyPostString;
    private OauthVolleyPostString oauthVolleyPostString;
    private VolleyPostMultipart volleyPostMultipart;

    public NetworkModule(ApplicationController app) {
        context = app.getApplicationContext();
        loginState = null;
        loginLock = new ReentrantLock();
        center = new TaskCenter();

        volleyPostString = new VolleyPostString();
        oauthVolleyPostString = new OauthVolleyPostString();
        volleyPostMultipart = new VolleyPostMultipart();
    }



















    /*******************************process请求事务,根据自己的业务需求，自定义请求地址以及参数)***************************************************/


    /**
     * 得到用户登陆后得到的的token
     */
    private String getAccessToken(String invalid) throws HttpProcessException {
        loginLock.lock();
        try {
            if (loginState != null) {
                if (loginState.getToken() != null) {
                    if (!loginState.getToken().equals(invalid))
                        return loginState.getToken();
                    loginState.setToken(null);
                }
            }
            logoutUser();
            throw new HttpProcessException("there's no rule to get token.", RequestWithoutLogin);
        } finally {
            loginLock.unlock();
        }
    }

    /**
     * 根据登陆事务的网络下行数据，进行用户信息的网络请求(由getMethod定位，需要public修饰)
     */
    public DataModule processLogin(DataModule rsp, HttpRequest request) throws HttpProcessException {
        if (rsp.code() == DataModule.CodeSucess) {

            LoginState state = rsp.parseLoginState();
            UserInfo user = new UserInfo();
            user.setLogin_phone(request.getParameter("phone"));

            loginLock.lock();
            try {
                loginState = state;
                loginState.setUser(user);
            } finally {
                loginLock.unlock();
            }

            final HttpRequest request_new = new HttpRequest(API_URL + "user/getUserInfo");
            request_new.addParameter("token", getAccessToken(null));
            request_new.setRequestTag("processLogin");

            try {
                if (processUserInfo(oauthVolleyPostString.process(request_new), request_new.getUrl()).code() == DataModule.CodeSucess) {
                    return rsp;
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return rsp;
    }


    /**
     * 获取用户信息，给成员变量LoginState的User成员变量赋具体值
     */
    private DataModule processUserInfo(DataModule rsp, String url) {

        if (rsp.code() == DataModule.CodeSucess) {
            try {
                JSONObject json = (JSONObject) rsp.getExtra();
                UserInfo user;
                loginLock.lock();
                try {
                    if (null == loginState)
                        try {
                            throw new HttpProcessException("not login", RequestWithoutLogin);
                        } catch (HttpProcessException e) {
                            e.printStackTrace();
                        }

                    user = loginState.getUser();
                    user.setId(json.getInt("id"));

                } finally {
                    loginLock.unlock();
                }
                rsp.setExtra(user);
                context.sendBroadcast(new Intent(ACTION_USER_LOGIN_CHANGED));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rsp;
    }


    /**
     * 网络请求更新完用户信息后，需要更新本地的UserInfo对象
     */
    public DataModule processUpdate(DataModule rsp, HttpRequest request) throws HttpProcessException {
        if (rsp.code() == DataModule.CodeSucess) {

            String temp = request.getParameter("nickname");

            loginLock.lock();
            try {
                UserInfo user = loginState.getUser();
            } finally {
                loginLock.unlock();
            }

            final HttpRequest request_new = new HttpRequest(API_URL + "user/getUserInfo");
            request_new.addParameter("token", getAccessToken(null));
            request_new.setRequestTag("processUpdate");

            try {
                if (processUserInfo(oauthVolleyPostString.process(request_new), request_new.getUrl()).code() == DataModule.CodeSucess) {
                    return rsp;
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            context.sendBroadcast(new Intent(ACTION_USER_LOGIN_CHANGED));
        }
        return rsp;
    }


    /***************************************各个逻辑模块的网络请求(接口url均为举例，请自行添加)******************************************/

    /**
     * 登陆事务
     */
    public TaskHandle arrangeLogin(String requestTag, String phone, String password, String clientcode) {
        HttpRequest request = new HttpRequest(API_URL + "user/userLogin");
        request.addParameter("phone", phone);
        request.addParameter("password", password);
        request.addParameter("clientcode", clientcode);
        request.setRequestTag(requestTag);
        return center.arrange(request, new NameVolleyPostString("processLogin", volleyPostString));
    }

    /**
     * 得到登陆的用户信息
     */
    public UserInfo getLoginUser() {
        loginLock.lock();
        try {
            return null == loginState ? null : loginState.getUser();
        } finally {
            loginLock.unlock();
        }
    }

    /**
     * 退出登陆
     */
    public void logoutUser() {
        loginLock.lock();
        try {
            loginState = null;
        } finally {
            loginLock.unlock();
        }
        sendLoginChangeBroadcase();
    }

    /**
     * 发送登陆状态改变的广播
     */
    public void sendLoginChangeBroadcase() {
        context.sendBroadcast(new Intent(ACTION_USER_LOGIN_CHANGED));
    }


    /**
     * 上传图片文件，获得图片的相对地址
     */
    public TaskHandle arrangeUploadImg(String requestTag, String imagePath) {
        HttpRequest request = new HttpRequest(API_URL + "img/imgUpload");
        try {
            request.addParameter("token", getAccessToken(null));
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }
        request.addFile("imgFile", imagePath);
        request.setRequestTag(requestTag);
        return center.arrange(request, volleyPostMultipart);
    }


    /**
     * 修改登录密码
     */
    public TaskHandle arrangEeditPassword(String requestTag, String oldPassword, String newPassword) {
        HttpRequest request = new HttpRequest(API_URL + "user/editPassword");
        request.addParameter("oldPassword", oldPassword);
        request.addParameter("newPassword", newPassword);
        request.setRequestTag(requestTag);
        return center.arrange(request, oauthVolleyPostString);
    }


    /**
     * 查看新闻列表
     */
    public TaskHandle arrangeGetNewsList(String requestTag, String city, String category, int page, int row, String title) {
        HttpRequest request = new HttpRequest(API_URL + "news/getNews");
        request.addParameter("city", city);
        if (category != null)
            request.addParameter("category", category);
        request.addParameter("page", Integer.toString(page));
        request.addParameter("row", Integer.toString(row));
        if (title != null)
            request.addParameter("title", title);
        request.setRequestTag(requestTag);
        return center.arrange(request, volleyPostString);
    }


}
