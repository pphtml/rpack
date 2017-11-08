package org.superbiz.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskExecutorImpl implements TaskExecutor {
//    @Inject
//    Logger logger;

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    public <T> T compute(Callable<T> task, long timeout) throws TimeoutException {
        try {
            final Future<T> future = executor.submit(task);
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw e;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
