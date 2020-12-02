package com.mps.blockchain.contracts.definitions.compraventa;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
@SuppressWarnings("rawtypes")
public class Compraventa extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516020806106058339810160405251600181905560028190556003556000805460ff191681556105bc90819061004990396000f3006080604052600436106100985763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630f50b6e7811461009d5780633182237f146100db5780634a335792146100f05780635e5046fd14610117578063688e34671461012e578063866e0aea146101675780639e66e0f31461016f578063c00c635714610184578063d3436a1a14610199575b600080fd5b3480156100a957600080fd5b506100b26101a1565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156100e757600080fd5b506100b26101bd565b3480156100fc57600080fd5b506101056101d9565b60408051918252519081900360200190f35b34801561012357600080fd5b5061012c6101df565b005b34801561013a57600080fd5b5061014361035e565b6040518082600381111561015357fe5b60ff16815260200191505060405180910390f35b61012c610367565b34801561017b57600080fd5b5061010561048f565b34801561019057600080fd5b50610105610495565b61012c61049b565b60045473ffffffffffffffffffffffffffffffffffffffff1681565b60055473ffffffffffffffffffffffffffffffffffffffff1681565b60025481565b60028060005460ff1660038111156101f357fe5b14610248576040805160e560020a62461bcd02815260206004820152601160248201527f45737461646f20696e636f72726563746f000000000000000000000000000000604482015290519081900360640190fd5b60045473ffffffffffffffffffffffffffffffffffffffff1633146102b7576040805160e560020a62461bcd02815260206004820152601160248201527f456d69736f7220696e636f72726563746f000000000000000000000000000000604482015290519081900360640190fd5b6000805460ff1916600317815560055460015460025460405173ffffffffffffffffffffffffffffffffffffffff9093169391019160208082019291908185875af1505060045460035460405173ffffffffffffffffffffffffffffffffffffffff909216935091506020808201916000918185875af150506040517f9b7773924d4070b258985e15698ed85342422dd0e8f7fe6f5bb782c3ff9b0fb5925060009150a150565b60005460ff1681565b60018060005460ff16600381111561037b57fe5b146103d0576040805160e560020a62461bcd02815260206004820152601160248201527f45737461646f20696e636f72726563746f000000000000000000000000000000604482015290519081900360640190fd5b6001546003540134811461042e576040805160e560020a62461bcd02815260206004820152601260248201527f466f6e646f7320696e636f72726563746f730000000000000000000000000000604482015290519081900360640190fd5b6004805473ffffffffffffffffffffffffffffffffffffffff191633179055600080546002919060ff19166001835b02179055506040517f9b7773924d4070b258985e15698ed85342422dd0e8f7fe6f5bb782c3ff9b0fb590600090a15050565b60015481565b60035481565b60008060005460ff1660038111156104af57fe5b14610504576040805160e560020a62461bcd02815260206004820152601160248201527f45737461646f20696e636f72726563746f000000000000000000000000000000604482015290519081900360640190fd5b60025434811461055e576040805160e560020a62461bcd02815260206004820152601260248201527f466f6e646f7320696e636f72726563746f730000000000000000000000000000604482015290519081900360640190fd5b6005805473ffffffffffffffffffffffffffffffffffffffff191633179055600080546001919060ff1916828061045d5600a165627a7a7230582048cad6b22f35ecec5b7a950ee43d76b044ff393d4381951a5576ef2e0bc45e270029";

    public static final String FUNC_COMPRADOR = "comprador";

    public static final String FUNC_VENDEDOR = "vendedor";

    public static final String FUNC_DEPOSITOVENDEDOR = "depositoVendedor";

    public static final String FUNC_LIBERARFONDOS = "liberarFondos";

    public static final String FUNC_ESTADO = "estado";

    public static final String FUNC_ENVIARFONDOSCOMPRADOR = "enviarFondosComprador";

    public static final String FUNC_PRECIO = "precio";

    public static final String FUNC_DEPOSITOCOMPRADOR = "depositoComprador";

    public static final String FUNC_ENVIARFONDOSVENDEDOR = "enviarFondosVendedor";

    public static final Event CAMBIOESTADO_EVENT = new Event("CambioEstado", 
            Arrays.<TypeReference<?>>asList());
    ;

    @Deprecated
    protected Compraventa(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Compraventa(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Compraventa(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Compraventa(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> comprador() {
        final Function function = new Function(FUNC_COMPRADOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> vendedor() {
        final Function function = new Function(FUNC_VENDEDOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> depositoVendedor() {
        final Function function = new Function(FUNC_DEPOSITOVENDEDOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> liberarFondos() {
        final Function function = new Function(
                FUNC_LIBERARFONDOS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> estado() {
        final Function function = new Function(FUNC_ESTADO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> enviarFondosComprador(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ENVIARFONDOSCOMPRADOR, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> precio() {
        final Function function = new Function(FUNC_PRECIO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> depositoComprador() {
        final Function function = new Function(FUNC_DEPOSITOCOMPRADOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> enviarFondosVendedor(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ENVIARFONDOSVENDEDOR, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public List<CambioEstadoEventResponse> getCambioEstadoEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CAMBIOESTADO_EVENT, transactionReceipt);
        ArrayList<CambioEstadoEventResponse> responses = new ArrayList<CambioEstadoEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CambioEstadoEventResponse typedResponse = new CambioEstadoEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CambioEstadoEventResponse> cambioEstadoEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, CambioEstadoEventResponse>() {
            @Override
            public CambioEstadoEventResponse apply(Log log) {
                //Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CAMBIOESTADO_EVENT, log);
                CambioEstadoEventResponse typedResponse = new CambioEstadoEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<CambioEstadoEventResponse> cambioEstadoEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CAMBIOESTADO_EVENT));
        return cambioEstadoEventFlowable(filter);
    }

    @Deprecated
    public static Compraventa load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Compraventa(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Compraventa load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Compraventa(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Compraventa load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Compraventa(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Compraventa load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Compraventa(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Compraventa> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _precio) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_precio)));
        return deployRemoteCall(Compraventa.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Compraventa> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _precio) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_precio)));
        return deployRemoteCall(Compraventa.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Compraventa> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _precio) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_precio)));
        return deployRemoteCall(Compraventa.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Compraventa> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _precio) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_precio)));
        return deployRemoteCall(Compraventa.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class CambioEstadoEventResponse {
        public Log log;
    }
}
