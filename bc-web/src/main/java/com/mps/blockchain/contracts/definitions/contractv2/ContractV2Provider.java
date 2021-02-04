package com.mps.blockchain.contracts.definitions.contractv2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;

@Service
public class ContractV2Provider implements ContractProvider {

	private static final String PROVIDER_NAME = "ContractV2";

	@Autowired
	private ContractV2OperationInvoker invoker;

	@Override
	public OperationInvoker getInvoker() {
		return invoker;
	}

	@Override
	public String getProviderName() {
		return ContractV2Provider.PROVIDER_NAME;
	}
}
