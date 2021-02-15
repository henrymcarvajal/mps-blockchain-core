package com.mps.blockchain.commons.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
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
 * <p>Generated with web3j version 4.6.4.
 */
@SuppressWarnings("rawtypes")
public class ReturnableSaleV1 extends Contract {
	
	public static final UUID CONTRACT_ID = UUID.fromString("ebd1a631-c08d-4294-aebb-3762c39afc90");
	
    public static final String BINARY = "60806040526040516111a93803806111a98339818101604052604081101561002657600080fd5b5080516020909101513461006b5760405162461bcd60e51b81526004018080602001828103825260248152602001806111856024913960400191505060405180910390fd5b600080546001600160a01b031990811633178255346008819055600955600180546001600160a01b03958616908316179055600280549390941692169190911790915560078054600383905560048390556005839055600683905565ffffffffffff191690556110a49081906100e190396000f3fe6080604052600436106100f35760003560e01c8063838ca3461161008a578063c19d93fb11610059578063c19d93fb14610269578063ca3361e81461028e578063d0e30db014610296578063dd9f111e146102ab576100f3565b8063838ca346146101f457806385dd688d14610209578063a0c97bce1461023f578063b79550be14610254576100f3565b80635cef5b19116100c65780635cef5b191461018e5780636f9fb98a146101a35780637150d8ae146101ca57806377c93662146101df576100f3565b806308551a53146100f8578063235a9aa7146101295780632afc2b4d1461014057806336d950be14610155575b600080fd5b34801561010457600080fd5b5061010d6102b3565b604080516001600160a01b039092168252519081900360200190f35b34801561013557600080fd5b5061013e6102c2565b005b34801561014c57600080fd5b5061013e61039e565b34801561016157600080fd5b5061013e6004803603606081101561017857600080fd5b5060ff813516906020810135906040013561066d565b34801561019a57600080fd5b5061013e61081e565b3480156101af57600080fd5b506101b8610aa7565b60408051918252519081900360200190f35b3480156101d657600080fd5b5061010d610aab565b3480156101eb57600080fd5b5061010d610aba565b34801561020057600080fd5b506101b8610ac9565b34801561021557600080fd5b5061021e610acf565b6040518082600281111561022e57fe5b815260200191505060405180910390f35b34801561024b57600080fd5b5061013e610adf565b34801561026057600080fd5b5061013e610c0b565b34801561027557600080fd5b5061027e610c96565b6040518082600381111561022e57fe5b61013e610ca6565b3480156102a257600080fd5b506101b8610e37565b61013e610e3d565b6001546001600160a01b031681565b600080600754600160281b900460ff1660038111156102dd57fe5b1461031f576040805162461bcd60e51b815260206004820152600d60248201526c496e76616c696420737461746560981b604482015290519081900360640190fd5b6000546001600160a01b031633146103685760405162461bcd60e51b81526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b6103726001610fbf565b6040517f40f2bce380935f17ba0d1bce2ba1e9139f86b7b6abec546eb28e087a2091332890600090a150565b6001600281600754600160281b900460ff1660038111156103bb57fe5b14806103e857508060038111156103ce57fe5b600754600160281b900460ff1660038111156103e657fe5b145b61042a576040805162461bcd60e51b815260206004820152600e60248201526d496e76616c69642073746174657360901b604482015290519081900360640190fd5b6002546001600160a01b03163314610489576040805162461bcd60e51b815260206004820181905260248201527f496e76616c69642063616c6c65723a2072657175697265642027627579657227604482015290519081900360640190fd5b600160028054600160a01b900460ff16908111156104a357fe5b14806104c557506002808054600160a01b900460ff16908111156104c357fe5b145b610516576040805162461bcd60e51b815260206004820152601f60248201527f496e76616c69642070617274793a207265717569726564202762757965722700604482015290519081900360640190fd5b6000600160028054600160a01b900460ff169081111561053257fe5b1415610597576001600754600160281b900460ff16600381111561055257fe5b14156105615750600854610592565b6002600754600160281b900460ff16600381111561057b57fe5b141561059257506006546003546004546008540101035b6105ce565b6002808054600160a01b900460ff16908111156105b057fe5b14156105ce576006546004546002600854816105c857fe5b04010390505b6002546040516001600160a01b039091169082156108fc029083906000818181858888f19350505050158015610608573d6000803e3d6000fd5b5060098054829003905560075462010000900460ff161561066857600780546003919060ff60281b1916600160281b835b02179055506040517f40f2bce380935f17ba0d1bce2ba1e9139f86b7b6abec546eb28e087a2091332890600090a15b505050565b6001600281600754600160281b900460ff16600381111561068a57fe5b14806106b7575080600381111561069d57fe5b600754600160281b900460ff1660038111156106b557fe5b145b6106f9576040805162461bcd60e51b815260206004820152600e60248201526d496e76616c69642073746174657360901b604482015290519081900360640190fd5b6000546001600160a01b031633146107425760405162461bcd60e51b81526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b600754640100000000900460ff16156107a2576040805162461bcd60e51b815260206004820152601760248201527f4368617267657320616c7265616479206170706c696564000000000000000000604482015290519081900360640190fd5b6002805486919060ff60a01b1916600160a01b83838111156107c057fe5b02179055506005849055600683905560008054604051858701926001600160a01b039092169183156108fc02918491818181858888f1935050505015801561080c573d6000803e3d6000fd5b50600980549190910390555050505050565b6001600281600754600160281b900460ff16600381111561083b57fe5b1480610868575080600381111561084e57fe5b600754600160281b900460ff16600381111561086657fe5b145b6108aa576040805162461bcd60e51b815260206004820152600e60248201526d496e76616c69642073746174657360901b604482015290519081900360640190fd5b6001546001600160a01b031633146108f35760405162461bcd60e51b815260040180806020018281038252602181526020018061102a6021913960400191505060405180910390fd5b600060028054600160a01b900460ff169081111561090d57fe5b148061092f57506002808054600160a01b900460ff169081111561092d57fe5b145b610980576040805162461bcd60e51b815260206004820181905260248201527f496e76616c69642070617274793a207265717569726564202773656c6c657227604482015290519081900360640190fd5b60008060028054600160a01b900460ff169081111561099b57fe5b1415610a00576001600754600160281b900460ff1660038111156109bb57fe5b14156109ca57506008546109fb565b6002600754600160281b900460ff1660038111156109e457fe5b14156109fb57506005546003546004546008540101035b610a37565b6002808054600160a01b900460ff1690811115610a1957fe5b1415610a3757600554600354600260085481610a3157fe5b04010390505b6001546040516001600160a01b039091169082156108fc029083906000818181858888f19350505050158015610a71573d6000803e3d6000fd5b506009805482900390556007546301000000900460ff161561066857600780546003919060ff60281b1916600160281b83610639565b4790565b6002546001600160a01b031681565b6000546001600160a01b031681565b60095481565b600254600160a01b900460ff1681565b600080600754600160281b900460ff166003811115610afa57fe5b14610b3c576040805162461bcd60e51b815260206004820152600d60248201526c496e76616c696420737461746560981b604482015290519081900360640190fd5b6001546001600160a01b03163314610b855760405162461bcd60e51b8152600401808060200182810382526021815260200180610fe76021913960400191505060405180910390fd5b6008546009805482900390556007805460ff60281b1916650300000000001790556001546040516001600160a01b039091169180156108fc02916000818181858888f19350505050158015610bde573d6000803e3d6000fd5b506040517f40f2bce380935f17ba0d1bce2ba1e9139f86b7b6abec546eb28e087a2091332890600090a150565b6000546001600160a01b03163314610c545760405162461bcd60e51b81526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b6000546001600160a01b03166108fc610c6b610aa7565b6040518115909202916000818181858888f19350505050158015610c93573d6000803e3d6000fd5b50565b600754600160281b900460ff1681565b6001600281600754600160281b900460ff166003811115610cc357fe5b1480610cf05750806003811115610cd657fe5b600754600160281b900460ff166003811115610cee57fe5b145b610d32576040805162461bcd60e51b815260206004820152600e60248201526d496e76616c69642073746174657360901b604482015290519081900360640190fd5b6000546001600160a01b03163314610d7b5760405162461bcd60e51b81526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b60003411610dba5760405162461bcd60e51b815260040180806020018281038252602481526020018061104b6024913960400191505060405180910390fd5b600754610100900460ff1615610e0b576040805162461bcd60e51b815260206004820152601160248201527011995948185b1c9958591e481c185e5959607a1b604482015290519081900360640190fd5b6007805461ff001916610100179055346004819055600980549091019055610e336002610fbf565b5050565b60085481565b6001600281600754600160281b900460ff166003811115610e5a57fe5b1480610e875750806003811115610e6d57fe5b600754600160281b900460ff166003811115610e8557fe5b145b610ec9576040805162461bcd60e51b815260206004820152600e60248201526d496e76616c69642073746174657360901b604482015290519081900360640190fd5b6000546001600160a01b03163314610f125760405162461bcd60e51b81526004018080602001828103825260228152602001806110086022913960400191505060405180910390fd5b60003411610f515760405162461bcd60e51b815260040180806020018281038252602481526020018061104b6024913960400191505060405180910390fd5b60075460ff1615610f9d576040805162461bcd60e51b815260206004820152601160248201527011995948185b1c9958591e481c185e5959607a1b604482015290519081900360640190fd5b6007805460ff19166001179055346003819055600980549091019055610e3360025b6007805482919060ff60281b1916600160281b836003811115610fde57fe5b02179055505056fe496e76616c6964206973737565723a207265717569726564202773656c6c657227496e76616c6964206973737565723a207265717569726564202773706f6e736f7227496e76616c69642063616c6c65723a207265717569726564202773656c6c657227496e76616c6964206665653a2072657175697265642067726561746572207468616e2030a2646970667358221220f710a183cc5bfec64087c1dd4dabb6867f98775d972a0d45b83547229ab189c364736f6c63430007010033696e697469616c2066756e6473206d7573742062652067726561746572207468616e2030";

    public static final String FUNC_BUYER = "buyer";

    public static final String FUNC_CURRENTFUNDS = "currentFunds";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_DISPUTEWINNER = "disputeWinner";

    public static final String FUNC_FUNDBUYERFEE = "fundBuyerFee";

    public static final String FUNC_FUNDSELLERFEE = "fundSellerFee";

    public static final String FUNC_GETCONTRACTBALANCE = "getContractBalance";

    public static final String FUNC_PAYSELLER = "paySeller";

    public static final String FUNC_RECOVERFUNDS = "recoverFunds";

    public static final String FUNC_REIMBURSEBUYER = "reimburseBuyer";

    public static final String FUNC_REIMBURSESELLER = "reimburseSeller";

    public static final String FUNC_SELLER = "seller";

    public static final String FUNC_SETDISPUTEONPHASE1 = "setDisputeOnPhase1";

    public static final String FUNC_SETDISPUTEWINNER = "setDisputeWinner";

    public static final String FUNC_SPONSOR = "sponsor";

    public static final String FUNC_STATE = "state";

    public static final Event STATECHANGED_EVENT = new Event("StateChanged", 
            Arrays.<TypeReference<?>>asList());

    protected ReturnableSaleV1(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected ReturnableSaleV1(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<StateChangedEventResponse> getStateChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STATECHANGED_EVENT, transactionReceipt);
        ArrayList<StateChangedEventResponse> responses = new ArrayList<>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StateChangedEventResponse typedResponse = new StateChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StateChangedEventResponse> stateChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map((Function<Log, StateChangedEventResponse>) log -> {
                StateChangedEventResponse typedResponse = new StateChangedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        );
    }

    public Flowable<StateChangedEventResponse> stateChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STATECHANGED_EVENT));
        return stateChangedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> buyer() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BUYER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> currentFunds() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CURRENTFUNDS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> deposit() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> disputeWinner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DISPUTEWINNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> fundBuyerFee(BigInteger buyerFee) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FUNDBUYERFEE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, buyerFee);
    }

    public RemoteFunctionCall<TransactionReceipt> fundSellerFee(BigInteger sellerFee) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FUNDSELLERFEE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, sellerFee);
    }

    public RemoteFunctionCall<BigInteger> getContractBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCONTRACTBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> paySeller() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYSELLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> recoverFunds() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RECOVERFUNDS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reimburseBuyer() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REIMBURSEBUYER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reimburseSeller() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REIMBURSESELLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> seller() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SELLER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setDisputeOnPhase1() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETDISPUTEONPHASE1, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setDisputeWinner(BigInteger winner, BigInteger sellerCharges, BigInteger buyerCharges) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETDISPUTEWINNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(winner), 
                new org.web3j.abi.datatypes.generated.Uint256(sellerCharges), 
                new org.web3j.abi.datatypes.generated.Uint256(buyerCharges)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> sponsor() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SPONSOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> state() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static ReturnableSaleV1 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ReturnableSaleV1(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ReturnableSaleV1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ReturnableSaleV1(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ReturnableSaleV1> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String seller, String buyer, BigInteger value) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, seller), 
                new org.web3j.abi.datatypes.Address(160, buyer)));
        return deployRemoteCall(ReturnableSaleV1.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor, value);
    }

    public static RemoteCall<ReturnableSaleV1> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String seller, String buyer, BigInteger value) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, seller), 
                new org.web3j.abi.datatypes.Address(160, buyer)));
        return deployRemoteCall(ReturnableSaleV1.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor, value);
    }
    
    public static class StateChangedEventResponse extends BaseEventResponse {
    }
}
