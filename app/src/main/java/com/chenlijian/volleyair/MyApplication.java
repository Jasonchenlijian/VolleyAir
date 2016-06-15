package com.chenlijian.volleyair;


public class MyApplication extends com.volley.air.base.ApplicationController {


	private static MyApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
	}

	public static MyApplication getInstance() {
		return instance;
	}

}