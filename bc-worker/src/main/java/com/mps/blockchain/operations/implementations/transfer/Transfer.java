package com.mps.blockchain.operations.implementations.transfer;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.commons.operations.definitions.TransferOperationMetadata;
import com.mps.blockchain.commons.queue.operations.messages.transfer.TransferMessageInputParameters;
import com.mps.blockchain.operations.Operation;
import com.mps.blockchain.utils.StringUtils;

public class Transfer implements Operation {

    private Map<String, String> inputs;

    @Override
    public String getName() {
        return TransferOperationMetadata.NAME;
    }

    @Override
    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }

    @Override
    public Map<String, String> getInputs() {
        return this.inputs;
    }

    @Override
    public OperationResult execute(Map<String, String> outputs) {
        
        String networkEndpoint = inputs.get(TransferMessageInputParameters.NETWORK_ENDPOINT);
        String mpsMainAddress = inputs.get(TransferMessageInputParameters.MAIN_ACCOUNT_ADDRESS);
        String mpsAccountPrivateK = inputs.get(TransferMessageInputParameters.MAIN_ACCOUNT_PRIVATE_K);
        String mpsAccountPublicK = inputs.get(TransferMessageInputParameters.MAIN_ACCOUNT_PUBLIC_K);
        String transferAddress = inputs.get(TransferMessageInputParameters.TRANSFER_ADDRESS);
        String transferAmount = inputs.get(TransferMessageInputParameters.TRANSFER_AMOUNT);
        
        try {
            Web3j web3j = Web3j.build(new HttpService(networkEndpoint));
            Credentials credentials = Credentials.create(mpsAccountPrivateK, mpsAccountPublicK);

            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(mpsMainAddress, DefaultBlockParameterName.LATEST).send();

            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            BigInteger value = BigInteger.valueOf(Long.parseLong(transferAmount));

            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT, transferAddress, value);

            // Sign the transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexSignedMessage = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexSignedMessage).send();

            String transactionHash = ethSendTransaction.getTransactionHash();

            if (transactionHash == null) {
                outputs.put("error", "transaction hash was null for transfer account " + transferAddress);
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
            e.printStackTrace();
            outputs.put("error", StringUtils.toString(e));
            return OperationResult.ERROR;
        }
    }
}
