package com.dollop.userauth.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionhandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgsNotvalidException(MethodArgumentNotValidException ex) {
		Map<String, String> res = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			res.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAlreadyExists.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceAlreadyExists ex) {
		String message = ex.getMessage();
		ApiErrorResponse response = ApiErrorResponse.builder().message(message).status( HttpStatus.BAD_REQUEST.toString()).success(false).build();
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataNotProvideException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(DataNotProvideException ex) {
		String message = ex.getMessage();
		ApiErrorResponse response = ApiErrorResponse.builder().message(message).status( HttpStatus.NOT_ACCEPTABLE.toString()).success(false).build();
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(PasswordNotMatchException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(PasswordNotMatchException ex) {
		String message = ex.getMessage();
		ApiErrorResponse response = ApiErrorResponse.builder().message(message).status( HttpStatus.NOT_ACCEPTABLE.toString()).success(false).build();
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(UserNotFoundException ex) {
		String message = ex.getMessage();
		ApiErrorResponse response = ApiErrorResponse.builder().message(message).status( HttpStatus.NOT_FOUND.toString()).success(false).build();
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<?> authenticationFailedException(AuthenticationFailedException ex) {
		String message = ex.getMessage();
		ApiErrorResponse response = ApiErrorResponse.builder().message(message).status( HttpStatus.UNAUTHORIZED.toString()).success(false).build();
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.UNAUTHORIZED);
	}

}
