package com.mps.blockchain.contracts.definitions.compraventa.operations.sendbuyerdeposit;

import java.math.BigDecimal;
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
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.mps.blockchain.contracts.definitions.ContractOperation;
import com.mps.blockchain.contracts.definitions.compraventa.Compraventa;
import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.DeployedContract;
import com.mps.blockchain.persistence.repository.BuyerAccountRepository;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.persistence.services.DeployedContractsRepositoryService;

@Component
public class CompraventaSendBuyerDeposit implements ContractOperation {

	private static String OPERATION_NAME = "SendBuyerDeposit";

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
	public void execute(Map<String, Object> outputs) {

		Optional<BuyerAccount> buyerAccountOptional = buyerAccountRepository
				.findByMpsBuyerId(inputParameters.getBuyerId());
		if (buyerAccountOptional.isEmpty()) {
			return;
		}

		BuyerAccount buyerAccount = buyerAccountOptional.get();

		Optional<DecryptedBlockchainAccount> blockchainAccountO = blockchainAccountRepositoryService
				.findById(buyerAccount.getBlockchainAccountId());
		if (blockchainAccountO.isEmpty()) {
			return;
		}

		DecryptedBlockchainAccount buyerBlockchainAccount = blockchainAccountO.get();

		Optional<DeployedContract> deployedContractO = deployedContractsRepositoryService
				.findById(inputParameters.getContractId());
		if (deployedContractO.isEmpty()) {
			return;
		}

		DeployedContract deployedContract = deployedContractO.get();

		Web3j web3 = Web3j.build(new HttpService(networkEndpoint));
		Credentials credentials = Credentials.create(buyerBlockchainAccount.getPrivateKey(),
				buyerBlockchainAccount.getPublicKey());

		try {
			Compraventa compraventa = Compraventa.load(deployedContract.getAddress(), web3, credentials,
					new DefaultGasProvider());

			RemoteCall<TransactionReceipt> transaction = compraventa.enviarFondosComprador(Convert.toWei(new BigDecimal(inputParameters.getContractFee()), Unit.ETHER).toBigInteger());
			TransactionReceipt transactionReceipt = transaction.send();
			outputs.put("receipt", transactionReceipt);
		} catch (Exception e) {
			outputs.put("error", e);
		}
	}
}
