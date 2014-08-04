package org.motechproject.mcts.integration.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final long STATUS = 500;

    private String reason;

    private BeneficiaryException batchException;

    public RestException(BeneficiaryException exception, List<String> errors) {
        this(exception, errors.toString());
    }

    public RestException(BeneficiaryException batchException, String reason) {
        super("HttpStatus:" + batchException.getError().getHttpStatus()
                + " reason:" + reason);
        this.reason = reason;
        this.batchException = batchException;
    }

    public RestException(String reason) {
        super("HttpStatus:" + STATUS + " reason:" + reason);
        this.reason = reason;
    }

    public HttpStatus getHttpStatus() {
        return getBatchException().getError().getHttpStatus();
    }

    public String getReason() {
        return reason;
    }

    public BeneficiaryException getBatchException() {
        return batchException;
    }
}
