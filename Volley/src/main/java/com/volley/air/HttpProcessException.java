package com.volley.air;

/**
 * Created by Jason Chen on 2015/8/13.
 * 异常类
 */
public class HttpProcessException extends Exception{
    /******************** public static part *********************/
    private static final long serialVersionUID = 3894113148180342612L;

    private final int ErrorType;

    public static final int REQUEST_CANCELLED = -1;
    public static final int FAIL_OPEN_CONNECTION = 1;
    public static final int CONNECTION_TIME_OUT = 2;
    public static final int WRITE_REQUEST_ERROR = 3;
    public static final int READ_RESPONSE_ERROR = 4;
    public static final int OTHER_REASON = 5;
    private static int CustomCode = OTHER_REASON;

    public static int createCustomErrorCode(){
        synchronized(HttpProcessException.class){
            return CustomCode ++;
        }
    }

    public static HttpProcessException cancelExce(){
        return new HttpProcessException("request is cancelled.", REQUEST_CANCELLED);
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