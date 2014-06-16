package mcts.integration.stub.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("serial")
public class PubsubException extends Exception {

	private PubsubErrors pubsubErrors;
	private String reason;

	public PubsubException(PubsubErrors pubsubErrors) {
		super(pubsubErrors.getMessage());
		this.pubsubErrors = pubsubErrors;
	}

	public PubsubException(PubsubErrors pubsubErrors, String reason) {
		super(pubsubErrors.getMessage());
		this.pubsubErrors = pubsubErrors;
		this.reason = reason;
	}

	public PubsubException(PubsubErrors pubsubErrors, Throwable throwable) {
		super(pubsubErrors.getMessage(), throwable);
		this.pubsubErrors = pubsubErrors;
	}

	public PubsubException(PubsubErrors pubsubErrors, Throwable throwable,
			String reason) {
		super(pubsubErrors.getMessage(), throwable);
		this.pubsubErrors = pubsubErrors;
		this.reason = reason;
	}

	public int getErrorCode() {
		return pubsubErrors.getCode();
	}

	public String getErrorMessage() {
		if (reason == null || reason.length() < 1) {
			return this.getMessage();
		} else {
			return this.getMessage() + ". Reason: " + reason;
		}
	}

	public PubsubErrors getError() {
		return pubsubErrors;
	}

	public String getErrorMessageDetails() {

		return getStackTraceString();
	}

	private String getStackTraceString() {
		String stackTrace = null;

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		this.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		stackTrace = stringWriter.toString();

		return stackTrace;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}