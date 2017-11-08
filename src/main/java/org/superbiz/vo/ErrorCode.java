package org.superbiz.vo;

public enum ErrorCode {
    EC_REG_MISSING_ARGUMENT("Missing argument"),
    EC_REG_COMPUTATION_EXCEEDED("Computation time exceeded"),

    EC_REG_UNKNOWN("Unknown error");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
