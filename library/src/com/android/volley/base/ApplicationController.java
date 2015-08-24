package com.android.volley.base;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.toolbox.Volley;

public class ApplicationController extends Application {

	public static final String TAG = "VolleyPatterns";

	private static RequestQueue mRequestQueue;

	private static ApplicationController sInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		sInstance = this;
	}

	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}

	public static RequestQueue getRequestQueue() {

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	public static void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}



}