package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.setdisputewinner;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.CredentialsProvider;
import com.mps.blockchain.service.currencies.Currency;
import com.mps.blockchain.service.currencies.CurrencyConvertionService;

@Component
public class SetDisputeWinner implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;
	
	@Autowired
	private CurrencyConvertionService currencyConvertionService;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	private SetDisputeWinnerInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return SetDisputeWinner.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = SetDisputeWinnerInputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {

		Optional<DeployedContract> deployedContractOptional = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());

		if (deployedContractOptional.isEmpty()) {
			outputs.put("error", "contract not found: " + inputParameters.getContractId());
			return OperationResult.ERROR;
		}
		DeployedContract deployedContract = deployedContractOptional.get();

		OperationResult result;
		try {
			Web3j web3 = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getMainCredentials();

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			BigDecimal weiSellerAmount = convertCurrencyToWei(inputParameters.getSellerCharges());
			BigDecimal weiBuyerAmount = convertCurrencyToWei(inputParameters.getBuyerCharges());
			
			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.setDisputeWinner(inputParameters.getDisputeWinner(), weiSellerAmount.toBigIntegerExact(), weiBuyerAmount.toBigIntegerExact());
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt);
			result = OperationResult.SUCCESS;
		} catch (Exception e) {
			outputs.put("error", e);
		    result = OperationResult.ERROR;
		}
		return result;
	}
	
	private BigDecimal convertCurrencyToWei(BigDecimal amount) {
		BigDecimal etherAmount = currencyConvertionService.convert(Currency.FIAT.COLOMBIAN_PESO, Currency.CRYPTO.XDAI,
				amount);
		return Convert.toWei(etherAmount, Unit.ETHER);
	}
}
