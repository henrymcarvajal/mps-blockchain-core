package com.mps.blockchain.contracts.definitions.returnablesalev1.operations.deploy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.returnablesalev1.ReturnableSaleV1;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.network.NetworkProvider;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.AccountFunder;
import com.mps.blockchain.service.accounts.AccountManager;
import com.mps.blockchain.service.accounts.CredentialsProvider;
import com.mps.blockchain.service.currencies.Currency;
import com.mps.blockchain.service.currencies.CurrencyConvertionService;

@Component
public class Deploy implements ContractOperation {

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private AccountFunder accountFunder;

	@Autowired
	private CurrencyConvertionService currencyConvertionService;

	@Autowired
	private BlockchainAccountRepositoryService blockchainAccountRepositoryService;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	@Override
	public String getOperationName() {
		return Deploy.class.getSimpleName();
	}

	private DeployInputParameters inputParameters;

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = DeployInputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		UUID sellerId = inputParameters.getSellerId();
		if (sellerId == null) {
			throw new IllegalStateException("Call to buildInputs required: missing sellerId");
		}
		
		SellerAccount sellerAccount;
		sellerAccount = accountManager.getSellerAccount(sellerId, true);
		
		UUID buyerId = inputParameters.getBuyerId();
		if (buyerId == null) {
			throw new IllegalStateException("Call to buildInputs required: missing buyerId");
		}
		
		BuyerAccount buyerAccount = null;
		buyerAccount = accountManager.getBuyerAccount(buyerId, true);
		
		BigDecimal contractValue = inputParameters.getContractValue();
		if (contractValue == null) {
			throw new IllegalStateException("Call to buildInputs required: missing contractValue");
		}
		
		Optional<DecryptedBlockchainAccount> sellerAccountOptional = blockchainAccountRepositoryService
				.findById(sellerAccount.getBlockchainAccountId());
		if (sellerAccountOptional.isEmpty()) {
			outputs.put("error", "seller account not found: " + sellerAccount.getBlockchainAccountId());
			return OperationResult.ERROR;
		}
		DecryptedBlockchainAccount sellerBlockchainAccount = sellerAccountOptional.get();

		Optional<DecryptedBlockchainAccount> buyerAccountOptional = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (buyerAccountOptional.isEmpty()) {
			outputs.put("error", "buyer account not found: " + sellerAccount.getBlockchainAccountId());
			return OperationResult.ERROR;
		}
		DecryptedBlockchainAccount buyerBlockchainAccount = buyerAccountOptional.get();

		try {
			Web3j web3j = networkProvider.getBlockchainNetwork();
			Credentials credentials = credentialsProvider.getMainCredentials();

			BigDecimal amount = currencyConvertionService.convert(Currency.FIAT.COLOMBIAN_PESO, Currency.CRYPTO.XDAI,
					contractValue);

			BigDecimal weiAmount = Convert.toWei(amount, Unit.ETHER);

			ReturnableSaleV1 returnableSaleV1 = ReturnableSaleV1
					.deploy(web3j, credentials, new DefaultGasProvider(), sellerBlockchainAccount.getAddress(),
							buyerBlockchainAccount.getAddress(), weiAmount.toBigIntegerExact())
					.send();
			String contractAddress = returnableSaleV1.getContractAddress();
			Optional<TransactionReceipt> optionalReceipt = returnableSaleV1.getTransactionReceipt();
			String receipt = null;
			if (optionalReceipt.isPresent()) {
				TransactionReceipt transactionReceipt = optionalReceipt.get();
				receipt = toString(transactionReceipt);
			}
			DeployedContract contract = new DeployedContract();
			contract.setIdContract(ReturnableSaleV1.CONTRACT_ID);
			contract.setAddress(contractAddress);
			contract.setSellerAccountId(sellerAccount.getId());
			contract.setBuyerAccountId(buyerAccount.getId());
			contract.setReceipt(receipt);
			contract.setDeploymentResult(OperationResult.SUCCESS.getValue());
			deployedContractsRepositoryService.create(contract);

			outputs.put("contractId", contract.getId());
		} catch (Exception e) {
			outputs.put("error", e);
			return OperationResult.ERROR;
		}

		if (accountFunder.fundSellerAddress(sellerBlockchainAccount.getAddress(), outputs) == OperationResult.ERROR) {
			outputs.put("error", "seller account could not be funded: " + outputs.get("error"));
			return OperationResult.ERROR;
		}

		if (accountFunder.fundBuyerAddress(buyerBlockchainAccount.getAddress(), outputs) == OperationResult.ERROR) {
			outputs.put("error", "buyer account could not be funded: " + outputs.get("error"));
			return OperationResult.ERROR;
		}

		return OperationResult.SUCCESS;
	}

	private String toString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
