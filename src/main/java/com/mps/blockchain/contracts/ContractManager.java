package com.mps.blockchain.contracts;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.contracts.definitions.ContractProvider;

@Service
public class ContractManager {

	private Map<String, ContractProvider> availableContracts;

	@Autowired
	public ContractManager(List<ContractProvider> providers) {
		availableContracts = providers.stream()
				.collect(Collectors.toMap(ContractProvider::getProviderName, Function.identity()));
	}

	public ContractProvider getContractProvider(String providerName) {
		if (availableContracts.containsKey(providerName)) {
			return availableContracts.get(providerName);
		}
		return null;
	}
}
