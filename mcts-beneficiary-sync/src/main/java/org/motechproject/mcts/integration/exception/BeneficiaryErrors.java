package org.motechproject.mcts.integration.exception;

import org.springframework.http.HttpStatus;

public interface BeneficiaryErrors {
	
	
	
	String getMessage();
	int getCode();
	HttpStatus getHttpStatus();
}
