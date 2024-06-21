package com.mitocode.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleModelNotFoundException(ModelNotFoundException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Model Not Found");
        problemDetail.setType(URI.create("/not-found"));
        return problemDetail;
    }

    /*	@ExceptionHandler(ModelNotFoundException.class) //Esta version equivale  pero sin problemDetail
	public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex, WebRequest req){
		return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
				.title("Model not found")
				.type(URI.create(req.getContextPath()))
				.property("test", "value-test")
				.property("age", 32)
				.build();
	}*/

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<CustomErrorResponse> handleSQLException(SQLException ex, WebRequest req){
		var res = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(res, CONFLICT);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
																  HttpStatusCode status, WebRequest request) {
		var msg = ex.getBindingResult().getAllErrors()
				.stream()
				.map(e -> Objects.requireNonNull(e.getCode()).concat(":")
						.concat(Objects.requireNonNull(e.getDefaultMessage())))
				.collect(joining());
		var err = new CustomErrorResponse(LocalDateTime.now(), msg, request.getDescription(false));
		return new ResponseEntity<>(err, BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleAllException(ModelNotFoundException ex, WebRequest request) {
		var err = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(err, INTERNAL_SERVER_ERROR);
	}
}