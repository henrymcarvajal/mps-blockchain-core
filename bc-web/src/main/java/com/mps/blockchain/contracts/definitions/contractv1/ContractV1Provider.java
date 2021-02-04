package com.mps.blockchain.contracts.definitions.contractv1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;

@Service
public class ContractV1Provider implements ContractProvider {

	private static final String PROVIDER_NAME = "ContractV1";

	@Autowired
	private ContractV1OperationInvoker invoker;

	@Override
	public OperationInvoker getInvoker() {
		return invoker;
	}

	@Override
	public String getProviderName() {
		return ContractV1Provider.PROVIDER_NAME;
	}
}
