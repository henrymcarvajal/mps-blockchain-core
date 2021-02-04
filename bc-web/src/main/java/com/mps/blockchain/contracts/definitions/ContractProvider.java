package com.mps.blockchain.contracts.definitions;

public interface ContractProvider {
	
	OperationInvoker getInvoker();
	String getProviderName();
}
