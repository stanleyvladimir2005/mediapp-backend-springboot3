package com.mitocode.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {

	private LocalDateTime dateTime;
	private String message;
	private String details;
}