package com.mps.blockchain.contracts.definitions.compraventa.operations.releasefunds;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.contracts.definitions.compraventa.Compraventa;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.persistence.repository.BuyerAccountRepository;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;
import com.mps.blockchain.utils.StringUtils;

@Component
public class CompraventaReleaseFunds implements ContractOperation {

	private static final String OPERATION_NAME = "ReleaseFunds";

	@Autowired
	private BuyerAccountRepository buyerAccountRepository;

	@Autowired
	private DeployedContractsRepositoryService deployedContractsRepositoryService;

	@Autowired
	private BlockchainAccountRepositoryService blockchainAccountRepositoryService;

	@Value("${blockchain.network.endpoint}")
	private String networkEndpoint;

	@Override
	public String getOperationName() {
		return OPERATION_NAME;
	}

	private InputParameters inputParameters;

	@Override
	public void buildInputs(Map<String, String> inputs) throws MissingInputException {
		inputParameters = InputParameters.build(inputs);
	}

	@Override
	public OperationResult execute(Map<String, String> outputs) {

		Optional<BuyerAccount> buyerAccountOptional = buyerAccountRepository
				.findByMpsBuyerId(inputParameters.getBuyerId());
		if (buyerAccountOptional.isEmpty()) {
			return OperationResult.ERROR;
		}

		BuyerAccount buyerAccount = buyerAccountOptional.get();

		Optional<DecryptedBlockchainAccount> blockchainAccountO = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (blockchainAccountO.isEmpty()) {
			return OperationResult.ERROR;
		}

		DecryptedBlockchainAccount buyerBlockchainAccount = blockchainAccountO.get();

		Optional<DeployedContract> deployedContractO = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());
		if (deployedContractO.isEmpty()) {
			return OperationResult.ERROR;
		}

		DeployedContract deployedContract = deployedContractO.get();

		Web3j web3 = Web3j.build(new HttpService(networkEndpoint));
		Credentials credentials = Credentials.create(buyerBlockchainAccount.getPrivateKey(),
				buyerBlockchainAccount.getPublicKey());

		try {
			Compraventa compraventa = Compraventa.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = compraventa.liberarFondos();
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt.toString());
		} catch (Exception e) {
			outputs.put("error", StringUtils.toString(e));
		}
		return OperationResult.SUCCESS;
	}
}
