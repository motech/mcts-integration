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
	KEY_FIELD_DOESNOT_EXIST(3006,"Some field(s) is/are missing ",HttpStatus.INTERNAL_SERVER_ERROR),
	FIELD_MISMATCH(3007,"received field and existing field doesn't match",HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_ARGUMENT(3008,"Argument Value received does not satisfy the constraints", HttpStatus.INTERNAL_SERVER_ERROR),
	CONNECTION_ERROR(3009,"Cannot create connection with third party",HttpStatus.INTERNAL_SERVER_ERROR),
	CLASS_AND_OBJECT_DOES_NOT_MATCH(3010,"Class and Object are not of same type",HttpStatus.INTERNAL_SERVER_ERROR),
	NUMBERS_MISMATCH(3011,"Number types do not match",HttpStatus.INTERNAL_SERVER_ERROR),
	FOUND_EMPTY_STRING(3012, "String is Empty",HttpStatus.INTERNAL_SERVER_ERROR),
	JAXB_PARSING_ERROR(3013,"Invalid Content Received", HttpStatus.INTERNAL_SERVER_ERROR),
	URI_SYNTAX_ERROR(3014,"String could not be parsed as URI reference", HttpStatus.INTERNAL_SERVER_ERROR),
	JAXB_ERROR(5001,"JAX_B error",HttpStatus.INTERNAL_SERVER_ERROR),
	
	PROPERTY_ERROR(7001,"Property Error",HttpStatus.INTERNAL_SERVER_ERROR);
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
