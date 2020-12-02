package com.mps.blockchain.contracts.definitions.returnablesalev1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;

@Service
public class ReturnableSaleProvider implements ContractProvider {

	private static String PROVIDER_NAME = "ReturnableSaleV1";

	@Autowired
	private ReturnableSaleOperationInvoker invoker;

	@Override
	public OperationInvoker getInvoker() {
		return invoker;
	}

	@Override
	public String getProviderName() {
		return ReturnableSaleProvider.PROVIDER_NAME;
	}
}
