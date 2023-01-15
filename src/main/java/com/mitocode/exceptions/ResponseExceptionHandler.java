package com.mitocode.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handlerAllExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ModelNotFoundException.class)
	public final ResponseEntity<Object> handlerModelExceptions(ModelNotFoundException ex, WebRequest request){
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String msg = ex.getBindingResult().getAllErrors().stream().map(
				e -> e.getDefaultMessage().concat(",")).collect(Collectors.joining());

		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), msg, request.getDescription(false));
		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
	}
}