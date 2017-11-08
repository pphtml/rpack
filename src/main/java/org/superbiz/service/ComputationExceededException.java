package org.superbiz.service;

public class ComputationExceededException extends Exception {
    public ComputationExceededException(String message) {
        super(message);
    }
}
