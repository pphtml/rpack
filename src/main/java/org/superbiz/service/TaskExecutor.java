package org.superbiz.service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public interface TaskExecutor {
    <T> T compute(Callable<T> task, long timeout) throws TimeoutException;
}
