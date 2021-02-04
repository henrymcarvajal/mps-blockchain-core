package com.mps.blockchain.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mps.blockchain.contracts.exceptions.MissingInputException;

@RestControllerAdvice
public class MissingInputExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingInputException.class)
	public Map<String, String> handleMIssingInputExceptions(MissingInputException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("missing input", ex.getFieldName());
		return errors;
	}
}