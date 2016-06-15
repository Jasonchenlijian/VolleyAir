package com.volley.air;

import android.content.Context;

import com.volley.air.base.ApplicationController;
import com.volley.air.base.NetworkResponse;
import com.volley.air.toolbox.VolleyTickle;

/**
 * 网络请求工具类
 */
public class VolleyAir {

    private Context context;
    private TaskCenter center;

    private VolleyPostString volleyPostString;

    public VolleyAir(ApplicationController app) {
        context = app.getApplicationContext();
        center = new TaskCenter();

        volleyPostString = new VolleyPostString();
    }

    public TaskHandle arrange(HttpRequest request) {

        return center.arrange(request, volleyPostString);
    }

    private class VolleyPostString implements Processor<String> {

        @Override
        public String process(final HttpRequest request) throws HttpProcessException {

            NetworkResponse response = VolleyRequest.RequestPostTickle(
                    context,
                    request.getUrl(),
                    request.getRequestTag(),
                    request.getHashMap(),
                    null);

            if (response.statusCode == 200) {
                return VolleyTickle.parseResponse(response);
            }
            return null;
        }
    }


}
