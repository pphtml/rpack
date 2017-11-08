package org.superbiz.vo;

public class ProcessingError {
    private final ErrorCode errorCode;
    private final String errorMessage;
    private final String errorDetail;

    public ProcessingError(ErrorCode errorCode, String errorDetail) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
        this.errorDetail = errorDetail;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorDetail() {
        return errorDetail;
    }
}
