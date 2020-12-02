package com.mps.blockchain.contracts.exceptions;

public class MissingInputException extends Exception {

	private static final long serialVersionUID = 5557596892015510328L;
	
	private final String fieldName;

	public MissingInputException(String fieldName) {
		super(fieldName);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
