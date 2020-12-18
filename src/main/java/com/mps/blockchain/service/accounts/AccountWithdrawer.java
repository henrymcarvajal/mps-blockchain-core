package com.mps.blockchain.service.accounts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert.Unit;

import com.mps.blockchain.contracts.definitions.OperationResult;
import com.mps.blockchain.network.NetworkProvider;

@Component
public class AccountWithdrawer {
	
	private static final BigInteger DEFAULT_TRANSFER_GAS = BigInteger.valueOf(21_000L);

	@Value("${blockchain.funds.seller.amount}")
	private Long sellerFundsAmount;

	@Value("${blockchain.funds.buyer.amount}")
	private Long buyerFundsAmount;
	
	@Value("${blockchain.collections.seller.gasprice}")
	private Long sellerCollectionsGasPrice;
	
	@Value("${blockchain.collections.buyer.gasprice}")
	private Long buyerCollectionsGasPrice;

	@Autowired
	private NetworkProvider networkProvider;

	@Autowired
	private CredentialsProvider credentialsProvider;

	private static final String SELLER_ACCOUNT_TYPE = "SELLER_ACCOUNT_TYPE";

	private static final String BUYER_ACCOUNT_TYPE = "BUYER_ACCOUNT_TYPE";

	public OperationResult withdrawFromSellerAddress(Credentials credentials, Map<String, Object> outputs) {
		return withdrawFromAddress(SELLER_ACCOUNT_TYPE, credentials, outputs);
	}

	public OperationResult withdrawFromBuyerAddress(Credentials credentials, Map<String, Object> outputs) {
		return withdrawFromAddress(BUYER_ACCOUNT_TYPE, credentials, outputs);
	}

	private OperationResult withdrawFromAddress(String accountType, Credentials credentials,
			Map<String, Object> outputs) {

		Map<String, Object> getBalanceOutput = new HashMap<>();
		if (isAccountWithdrawable(accountType, credentials, getBalanceOutput) == OperationResult.SUCCESS) {
			try {
				Web3j web3j = networkProvider.getBlockchainNetwork();

				BigInteger amountToCollect = (BigInteger) getBalanceOutput.get("amountToCollect");

				TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, credentialsProvider.getMainAddress(), new BigDecimal(amountToCollect), Unit.WEI).send();

				outputs.put("receipt", transactionReceipt);
				return OperationResult.SUCCESS;
			} catch (Exception e) {
				outputs.put("error", e);
				return OperationResult.ERROR;
			}
		} else {
			outputs.put("error", getBalanceOutput.get("error"));
			return OperationResult.ERROR;
		}
	}

	private OperationResult isAccountWithdrawable(String accountType, Credentials credentials,
			Map<String, Object> outputs) {
		try {
			Web3j web3j = networkProvider.getBlockchainNetwork();

			String address = credentials.getAddress();

			EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

			BigInteger amountToCollect = BigInteger.ZERO;

			if (accountType == SELLER_ACCOUNT_TYPE) {
				amountToCollect = moneyCollectionPolicyForSeller(ethGetBalance.getBalance());
			} else if (accountType == BUYER_ACCOUNT_TYPE) {
				amountToCollect = moneyCollectionPolicyForBuyer(ethGetBalance.getBalance());
			}

			if (amountToCollect.compareTo(BigInteger.ZERO) <= 0) {
				outputs.put("error", "not enough funds to withdraw from " + credentials.getAddress());
				return OperationResult.ERROR;
			} else {
				outputs.put("amountToCollect", amountToCollect);
				return OperationResult.SUCCESS;
			}

		} catch (Exception e) {
			outputs.put("error", e);
			return OperationResult.ERROR;
		}

	}

	private BigInteger moneyCollectionPolicyForSeller(BigInteger amount) {
		//BigInteger minimumTransferAmount = DEFAULT_TRANSFER_GAS.multiply(DefaultGasProvider.GAS_PRICE);41_000_000_000L
		
		//BigInteger minimumTransferAmount = DEFAULT_TRANSFER_GAS.multiply(DefaultGasProvider.GAS_PRICE).multiply(BigInteger.TEN).divide(BigInteger.valueOf(4L));
		//BigInteger minimumTransferAmount = DEFAULT_TRANSFER_GAS.multiply(BigInteger.valueOf(4_000_000_000L));

		BigInteger minimumAccountAmountforTransfer = DEFAULT_TRANSFER_GAS.multiply(BigInteger.valueOf(sellerCollectionsGasPrice));
		return amount.subtract(minimumAccountAmountforTransfer);
	}

	private BigInteger moneyCollectionPolicyForBuyer(BigInteger amount) {		
		BigInteger minimumAccountAmountforTransfer = DEFAULT_TRANSFER_GAS.multiply(BigInteger.valueOf(buyerCollectionsGasPrice));
		return amount.subtract(minimumAccountAmountforTransfer);
	}
}
