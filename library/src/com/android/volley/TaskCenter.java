package com.android.volley;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason Chen on 2015/8/18.
 * 网络线程管理者，通过Handler调度结果消息
 */
public class TaskCenter implements Executor {

    public static class CenterSpec{
        public int processors;
        public int keepAliveTime;
        public boolean timeoutCore;
    }

    public static CenterSpec createSpec(){
        CenterSpec spec = new CenterSpec();
        spec.processors = 6;
        spec.keepAliveTime = 60;
        spec.timeoutCore = true;
        return spec;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "engine-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


    /******************************************************************************************/


    private Handler handler;
    private LinkedBlockingQueue<Runnable> runQueue;
    private ThreadPoolExecutor engine;

    public TaskCenter(){
        this(createSpec());
    }

    public TaskCenter(CenterSpec spec){
        runQueue = new LinkedBlockingQueue<Runnable>();
        if(spec == null){
            spec = createSpec();
        }
        engine = new ThreadPoolExecutor(spec.processors, spec.processors,
                spec.keepAliveTime, TimeUnit.SECONDS,
                runQueue, new DefaultThreadFactory());
        engine.allowCoreThreadTimeOut(spec.timeoutCore);
        handler = new Handler(Looper.getMainLooper());
    }

    Handler getHandler(){
        return handler;
    }

    public void post(Runnable run){
        if(null == run)
            throw new NullPointerException(Helper.shouldntNull("run"));

        handler.post(run);
    }

    public void postDelay(Runnable run, long delay){
        if(null == run)
            throw new NullPointerException(Helper.shouldntNull("run"));
        handler.postDelayed(run, delay);
    }

    public TaskHandle arrange(HttpRequest request, Processor processor){
        if(null == processor)
            throw new NullPointerException(Helper.shouldntNull("processor"));
        return new TaskHandle(this, request, processor);
    }

    @Override
    public void execute(Runnable run) {
        engine.execute(run);
    }
}
