package com.chenlijian.volleyair.bean;


/**
 * 
 *	如果你的app中需要用户登陆
 */


public class UserInfo {

    private String login_phone;
    private int             id;

    public String getLogin_phone() {
        return login_phone;
    }

    public void setLogin_phone(String login_phone) {
        this.login_phone = login_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
