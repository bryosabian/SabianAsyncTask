package com.sabiantools.utilities.threads;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utility thread tool for multithreading. Alternative to {@link android.os.AsyncTask}
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this program and associated documentation files (the
//  "Software"), to deal in the Software without restriction, including
//  without limitation the rights to use, copy, modify, merge, publish,
//  distribute, sublicense, and/or sell copies of the program, and to
//  permit persons to whom the Software is furnished to do so, subject to
//  the following conditions:
//
//  The above copyright notice and this permission notice shall be included
//  in all copies or substantial portions of the program.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
//  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
//  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
//  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
//  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
//  PROGRAM OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class SabianAsyncTask {

    /**
     * The mutliple thread executor
     */
    public static final String SERVICE_TYPE_MULTI = "multi";

    /**
     * The single thread executor
     */
    public static final String SERVICE_TYPE_SINGLE = "single";

    /**
     * The default mutli thread pool executor. Usable for multiple threading like network services e.t.c
     */
    private final Executor MULTIPLE_THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(5, 128, 1,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    /**
     * The default single thread pool executor
     */
    private final Executor SINGLE_THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();


    /**
     * Our executor
     */
    private Executor executor;

    /**
     * Our handler
     */
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * The service collections
     */
    private HashMap<String, Executor> services;


    /**
     * The current service to be used
     * See {@link SabianAsyncTask#SERVICE_TYPE_MULTI} or {@link SabianAsyncTask#SERVICE_TYPE_SINGLE}
     */
    private String service = SERVICE_TYPE_SINGLE;

    /**
     * Init all services
     */
    public SabianAsyncTask() {
        initServices();
        executor = services.get(service);
    }

    /**
     * Inits all the services
     */
    private void initServices() {
        if (services != null)
            return;
        services = new HashMap<>();
        services.put(SERVICE_TYPE_MULTI, MULTIPLE_THREAD_POOL_EXECUTOR);
        services.put(SERVICE_TYPE_SINGLE, SINGLE_THREAD_POOL_EXECUTOR);
    }

    /**
     * Registers a new service to the thread pool collection
     * @param name
     * @param service
     * @return
     */
    public SabianAsyncTask registerService(String name,Executor service){
        this.services.put(name,service);
        return this;
    }

    /**
     * Sets the default thread service to be used
     * See {@link SabianAsyncTask#SERVICE_TYPE_MULTI} or {@link SabianAsyncTask#SERVICE_TYPE_SINGLE}
     * You can register a custom one using {@link SabianAsyncTask#registerService(String, Executor)}
     *
     * @param service
     * @return
     */
    public SabianAsyncTask setService(String service) {
        this.service = service;
        executor = services.get(service);
        return this;
    }

    /**
     * Executes a background thread
     *
     * @param callable
     * @param callback
     * @param <R>
     */
    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {

        callback.onBefore();

        executor.execute(() -> {

            R result = null;
            Throwable throwable = null;

            try {
                result = callable.call();
            } catch (Exception e) {
                throwable = e;
            }

            boolean isSuccess = throwable == null;
            R finalResult = result;
            Throwable error = throwable;

            handler.post(() -> {
                if (isSuccess)
                    callback.onComplete(finalResult);
                else {
                    callback.onError(error);
                }
            });
        });
    }
}
