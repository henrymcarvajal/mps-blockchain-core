package com.mps.blockchain.network;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Service
public class NetworkProvider {

    @Value("${blockchain.network.endpoint}")
    private String networkEndpoint;

    public Web3j getBlockchainNetwork() {
        return Web3j.build(new HttpService(networkEndpoint));
    }

    public String getBlockchainConnectionURL() {
        return networkEndpoint;
    }

}
