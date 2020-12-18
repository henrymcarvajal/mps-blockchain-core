package com.mps.blockchain.contracts.definitions.compraventa.operations.deploy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.contracts.definitions.compraventa.Compraventa;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.service.accounts.AccountManager;

@Component
public class CompraventaDeploy implements ContractOperation {

	private static final String OPERATION_NAME = "Deploy";
	private static final UUID CONTRACT_ID = UUID.fromString("ebd1a631-c08d-4294-aebb-3762c39afc90");

	@Value("${blockchain.network.endpoint}")
	private String networkEndpoint;

	@Value("${blockchain.mps.account.privk}")
	private String mpsAccountPrivateK;

	@Value("${blockchain.mps.account.pubk}")
	private String mpsAccountPublicK;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	@Override
	public String getOperationName() {
		return OPERATION_NAME;
	}

	private InputParameters inputParameters;

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = InputParameters.build(inputs);
	}

	private void setupAccounts() {
		UUID buyerId = inputParameters.getBuyerId();
		BuyerAccount buyerAccount = null;
		if (buyerId != null) {
			buyerAccount = accountManager.getBuyerAccount(buyerId, true);
			inputParameters.setBuyerAccount(buyerAccount);
		}

		UUID sellerId = inputParameters.getSellerId();
		SellerAccount sellerAccount = null;
		if (sellerId != null) {
			sellerAccount = accountManager.getSellerAccount(sellerId, true);
			inputParameters.setSellerAccount(sellerAccount);
		}
	}

	@Override
	public OperationResult execute(Map<String, Object> outputs) {
		setupAccounts();
		Web3j web3 = Web3j.build(new HttpService(networkEndpoint));
		Credentials credentials = Credentials.create(mpsAccountPrivateK, mpsAccountPublicK);

		String contractAddress = "";
		String receipt = null;
		String deploymentResult = "success";
		try {
			Compraventa compraventa = Compraventa
					.deploy(web3, credentials, new DefaultGasProvider(), Convert
							.toWei(new BigDecimal(inputParameters.getContractValue()), Unit.ETHER).toBigInteger())
					.send();
			contractAddress = compraventa.getContractAddress();
			Optional<TransactionReceipt> optionalReceipt = compraventa.getTransactionReceipt();
			if (optionalReceipt.isPresent()) {
				TransactionReceipt transactionReceipt = optionalReceipt.get();
				receipt = toString(transactionReceipt);
			}
		} catch (Exception e) {
			deploymentResult = "error";
			receipt = toString(e);
		}
		DeployedContract contract = new DeployedContract();
		contract.setIdContract(CONTRACT_ID);
		contract.setAddress(contractAddress);
		contract.setSellerAccountId(inputParameters.getSellerAccount().getId());
		contract.setBuyerAccountId(inputParameters.getBuyerAccount().getId());
		contract.setReceipt(receipt);
		contract.setDeploymentResult(deploymentResult);
		deployedContractsRepositoryService.create(contract);

		outputs.put("contractId", contract.getId());
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
