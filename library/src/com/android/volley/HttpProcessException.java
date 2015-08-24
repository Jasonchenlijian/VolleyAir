package com.android.volley;

/**
 * Created by Jason Chen on 2015/8/13.
 */
public class HttpProcessException extends Exception{
    /******************** public static part *********************/
    private static final long serialVersionUID = 3894113148180342612L;

    private final int ErrorType;

    public static final int RequestCancelled = -1;
    public static final int FailOpenConnection = 1;
    public static final int ConnectionTimeOut = 2;
    public static final int WriteRequestError = 3;
    public static final int ReadResponseError = 4;
    public static final int OtherReason = 5;
    private static int CustomCode = OtherReason;

    public static int createCustomErrorCode(){
        synchronized(HttpProcessException.class){
            return CustomCode ++;
        }
    }

    public static HttpProcessException cancelExce(){
        return new HttpProcessException("request is cancelled.", RequestCancelled);
    }
    /******************** class inner part *********************/

    public HttpProcessException(String msg, int code){
        super(msg);
        ErrorType = code;
    }


    public HttpProcessException(Throwable wrapped, int type){
        this(wrapped, wrapped.getMessage(), type);
    }

    public HttpProcessException(Throwable wrapped, String des, int type){
        super(des, wrapped);
        ErrorType = type;
    }

    public int getErrorType(){
        return ErrorType;
    }
}