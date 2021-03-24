package com.puzzle.industries.inumbr;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class iNumber extends Application {

    private static Handler applicationHandler;

    private static iNumber instance;

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, long delay) {
        if (delay == 0) {
            iNumber.applicationHandler.post(runnable);
        } else {
            iNumber.applicationHandler.postDelayed(runnable, delay);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //execute all operations here before creating the main screen
        applicationHandler = new Handler(getMainLooper());
    }
}
