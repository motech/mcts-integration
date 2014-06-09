package mcts.integration.stub.exception;

import org.springframework.http.HttpStatus;

public interface PubsubErrors {
	
	public String getMessage();
	public int getCode();
	public HttpStatus getHttpStatus();
}
