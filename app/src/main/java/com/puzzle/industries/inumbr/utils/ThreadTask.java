package com.puzzle.industries.inumbr.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTask {

    private ExecutorService service;
    private boolean isRunning = false;

    public void execute() {
        isRunning = true;
        service = Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable) {
        if (service == null || isShutdown()) {
            execute();
        }
        service.execute(runnable);
    }

    public void stop() {
        isRunning = false;
        service.shutdownNow();
    }

    public boolean isShutdown() {
        return !isRunning();
    }

    public boolean isRunning() {
        return isRunning;
    }
}