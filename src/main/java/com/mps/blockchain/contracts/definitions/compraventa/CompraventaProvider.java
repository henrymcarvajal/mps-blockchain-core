package com.mps.blockchain.contracts.definitions.compraventa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractProvider;
import com.mps.blockchain.contracts.definitions.OperationInvoker;

@Service
public class CompraventaProvider implements ContractProvider {

	private static String PROVIDER_NAME = "Compraventa";

	@Autowired
	private CompraventaOperationInvoker invoker;

	@Override
	public OperationInvoker getInvoker() {
		return invoker;
	}

	@Override
	public String getProviderName() {
		return CompraventaProvider.PROVIDER_NAME;
	}
}
