package com.mps.blockchain.service.accounts;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.network.NetworkProvider;

@Component
public class AccountFunder {

	@Value("${blockchain.funds.seller.amount}")
	private Long sellerFundsAmount;

	@Value("${blockchain.funds.buyer.amount}")
	private Long buyerFundsAmount;

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	public OperationResult fundSellerAddress(String address, Map<String, Object> outputs) {
		return fundAddress(address, sellerFundsAmount, outputs);
	}

	public OperationResult fundBuyerAddress(String address, Map<String, Object> outputs) {
		return fundAddress(address, buyerFundsAmount, outputs);
	}

	private OperationResult fundAddress(String address, long amount, Map<String, Object> outputs) {
		try {
			Web3j web3j = networkProvider.getBlockchainNetwork();

			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(credentialsProvider.getMainAddress(), DefaultBlockParameterName.LATEST)
					.send();

			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			BigInteger value = BigInteger.valueOf(amount);

			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE,
					DefaultGasProvider.GAS_LIMIT, address, value);

			// Sign the transaction
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,
					credentialsProvider.getMainCredentials());
			String hexSignedMessage = Numeric.toHexString(signedMessage);

			EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexSignedMessage).send();

			String transactionHash = ethSendTransaction.getTransactionHash();

			if (transactionHash == null) {
				outputs.put("error", "transaction hash was null for funding account " + address);
				return OperationResult.ERROR;
			}

			Optional<TransactionReceipt> transactionReceipt;
			do {
				EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(transactionHash)
						.send();
				transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
				Thread.sleep(3000); // Wait 3 sec
			} while (!transactionReceipt.isPresent());

			return OperationResult.SUCCESS;
		} catch (Exception e) {
			outputs.put("error", e);
			return OperationResult.ERROR;
		}
	}
}
