package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.fundbuyerfee;

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
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.CredentialsProvider;
import com.mps.blockchain.service.currencies.Currency;
import com.mps.blockchain.service.currencies.CurrencyConvertionService;
import com.mps.blockchain.utils.StringUtils;

@Component
public class FundBuyerFee implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	@Autowired
	private CurrencyConvertionService currencyConvertionService;
	
	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	private FundBuyerFeeInputParameters inputParameters;

	@Override
	public String getOperationName() {
		return FundBuyerFee.class.getSimpleName();
	}

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = FundBuyerFeeInputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, String> outputs) {

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
			BigDecimal amount = currencyConvertionService.convert(Currency.FIAT.COLOMBIAN_PESO, Currency.CRYPTO.XDAI, inputParameters.getBuyerFee());
			
			BigDecimal weiAmount = Convert.toWei(amount, Unit.ETHER);

			RemoteCall<TransactionReceipt> transaction = returnableSaleV1.fundBuyerFee(weiAmount.toBigIntegerExact());
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt.toString());
			result = OperationResult.SUCCESS;
		} catch (Exception e) {
			outputs.put("error", StringUtils.toString(e));
			result = OperationResult.ERROR;
		}
		return result;
	}
}
