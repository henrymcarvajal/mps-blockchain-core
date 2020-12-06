package com.mps.blockchain.contracts.definitions;

public enum OperationResult {
	
	ERROR("error"), SUCCESS("success");
	
	private final String value;
	
	private OperationResult(String value)  {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
