package com.volley.air;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason Chen on 2015/8/18.
 * 网络线程处理者
 */
public class TaskHandle {

    public static final byte RESULT_NOT_DOING = 0;
    public static final byte RESULT_NOT_BORN = 1;
    public static final byte RESULT_COME_OUT = 2;
    public static final byte RESULT_EXCEPTION = -1;

    protected static final byte IDLE = 0;
    protected static final byte WAITTING = 1;
    protected static final byte RUNNING = 2;
    protected static final byte RAN_NORMAL = 3;
    protected static final byte HANDLING = 4;
    protected static final byte DONE = 5;
    protected static final byte RAN_EXCEPTIONAL = 6;
    protected static final byte CANCELLED = 7;


    private class FinalRunner implements Runnable {

        private final byte type;

        FinalRunner(byte runType) {
            type = runType;
        }

        @Override
        public void run() {
            Receiver rec = receiver;
            Object result = outcome;

            if (type == RAN_NORMAL) {
                if (state.compareAndSet(RAN_NORMAL, HANDLING)) {
                    rec.onSuccess(TaskHandle.this, result);
                    if (state.compareAndSet(HANDLING, DONE)) {
                        onFinal();
                    }
                }
            } else if (type == RAN_EXCEPTIONAL) {
                if (state.compareAndSet(RAN_EXCEPTIONAL, RAN_EXCEPTIONAL)) {
                    rec.onError(TaskHandle.this, (Throwable) result);
                }
            }
        }
    }


    /*********************************************************************************************/

    protected AtomicInteger state;

    private Executor executor;
    private HttpRequest request;
    private Receiver<?> receiver;
    private Processor processor;
    private Handler handler;
    private int id;
    private byte rs;
    private Object outcome;


    public TaskHandle(TaskCenter center, HttpRequest request, Processor processor) {
        this(center, center.getHandler(), request, processor);
    }

    public TaskHandle(Executor executor, Handler handler,  HttpRequest request, Processor processor) {
        this.executor = executor;
        this.request = request;
        this.processor = processor;
        this.handler = handler;

        state = new AtomicInteger(IDLE);
        rs = RESULT_NOT_BORN;
        outcome = null;
    }


    //Task结束，重置参数
    protected void onFinal() {
        executor = null;
        handler = null;
        receiver = null;
        request = null;
        processor = null;
    }

    private boolean isCancelled() {
        for (int s; ; ) {
            s = state.get();
            if (s >= DONE)
                return true;
            if (state.compareAndSet(s, s)) {
                return false;
            }
        }
    }

    /**
     * View给自己的网络请求线程设置id
     */
    public void setId(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    /**
     * View给自己的网络请求线程设置结果接收器，一般是调用者View自己
     */
    public boolean setReceiver(Receiver<?> receiver) {
        if (receiver == null)
            throw new IllegalArgumentException(Helper.shouldntNull("receiver"));

        for (int s; ; ) {
            s = state.get();                     // 获取AtomicInteger当前的值(表示任务进展到了哪一步)
            if (s >= DONE) {                     // DONE，异常，或者cancel
                if (rs != RESULT_COME_OUT)        // not run over.
                    return false;
                Receiver rec = receiver;
                rec.onSuccess(this, outcome);
                return true;
            }
            if (state.compareAndSet(s, s)) {
                this.receiver = receiver;
                return true;
            }
        }
    }

    /**
     * 取消线程
     */
    public boolean cancel() {
        for (int s; ; ) {
            s = state.get();
            if (s >= DONE)             // 如果任务已经进行到了5,6,7，则不能被cancel
                return false;
            if (state.compareAndSet(s, CANCELLED)) {
                onFinal();            //清空TaskHandle所有成员变量，取消任务
                return true;
            }
        }
    }


    /**
     * View给自己的网络请求线程推进，启动Volley的请求
     */
    public boolean pullTrigger() {
        Executor et = executor;
        if (state.compareAndSet(IDLE, WAITTING)) {
            et.execute(new Runnable() {
                @Override
                public void run() {
                    Processor pro = processor;
                    if (state.compareAndSet(WAITTING, RUNNING)) {
                        if (null != pro) {
                            try {
                                setFinal(RAN_NORMAL, pro.process(request));
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                                setFinal(RAN_EXCEPTIONAL, ex);
                            }
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }


    /**
     * 根据Volley进程得到的结果，通知接收器处理结果
     * 通过Handler读取消息队列中的请求，当读取到网络请求结果的消息，则通知Receiver处理结果
     */
    private void setFinal(byte newState, Object data) {
        outcome = data;
        rs = newState == RAN_NORMAL ? RESULT_COME_OUT : RESULT_EXCEPTION;

        if (state.compareAndSet(RUNNING, newState)) {
            if (receiver != null) {
                handler.post(new FinalRunner(newState));
                executor = null;
            } else {
                onFinal();
            }
        }
    }

}
