package com.volley.air.toolbox.multipart;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlEncodingHelper {

    public static String encode(final String content, final String encoding) {
        try {
            return URLEncoder.encode(
                content, 
                encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET
            );
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }
    
}
