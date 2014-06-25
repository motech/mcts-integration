package org.motechproject.mcts.integration.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationErrors implements BeneficiaryErrors {

	BAD_REQUEST(1001, "One or more input parameter(s) may be wrong", HttpStatus.BAD_REQUEST),
	FILE_NOT_FOUND(3001,"UNable to find the file",HttpStatus.INTERNAL_SERVER_ERROR),
	DATABASE_OPERATION_FAILED(3003,"Error in querying database",HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_CLOSING_FAILED(3004,"Error in closing the file",HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_READING_WRTING_FAILED(3002,"Error while reading from or writing to file",HttpStatus.INTERNAL_SERVER_ERROR),
	CSV_FILE_DOES_NOT_MATCH_WITH_HEADERS(3004,"Error while matching csv file header with the DTO",HttpStatus.INTERNAL_SERVER_ERROR),
	NUMBER_OF_ARGUMENTS_DOES_NOT_MATCH(3005,"Error while matching number of arguments of csv with the DTO",HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_ARGUMENT(3006,"Argument Value received does not satisfy the constraints", HttpStatus.INTERNAL_SERVER_ERROR),
	CONNECTION_ERROR(3007,"Cannot create connection with third party",HttpStatus.INTERNAL_SERVER_ERROR);
	
	private final int code;
	private String message;
	private HttpStatus httpStatus;

	private ApplicationErrors() {
		code = 1000;
	}
	private ApplicationErrors(int code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
