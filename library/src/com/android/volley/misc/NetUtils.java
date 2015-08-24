/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.misc;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class NetUtils {
    private static final String TAG = "NetUtils";
    private static String mUserAgent = null;

    public static String getUserAgent(Context mContext) {
        if (mUserAgent == null) {
            mUserAgent = "volley/0";
            try {
                String packageName = mContext.getPackageName();
                PackageInfo info = mContext.getPackageManager().getPackageInfo(packageName, 0);
                mUserAgent = packageName + "/" + info.versionCode;
                
/*                String version = mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
                mUserAgent = mUserAgent + " (" + packageName + "/" + version + ")";*/
                Log.d(TAG, "User agent set to: " + mUserAgent);
            } catch (PackageManager.NameNotFoundException e) {
            	Log.d(TAG, "Unable to find self by package name", e);
            }
        }
        return mUserAgent;
    }
}
