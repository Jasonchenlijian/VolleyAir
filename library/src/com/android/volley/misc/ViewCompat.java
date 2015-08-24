/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
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

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;


public class ViewCompat {
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackground(View view, Drawable drawable){
		if(Utils.hasJellyBean()){
			view.setBackground(drawable);
		}
		else{
			view.setBackgroundDrawable(drawable);
		}
	}
}
