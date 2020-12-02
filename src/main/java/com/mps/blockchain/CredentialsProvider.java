package com.mps.blockchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;

import com.mps.blockchain.model.DecryptedBlockchainAccount;

@Service
public class CredentialsProvider {
	
	@Value("${blockchain.mps.account.privk}")
	private String mpsAccountPrivateK;

	@Value("${blockchain.mps.account.pubk}")
	private String mpsAccountPublicK;

	
	public Credentials getMainCredentials() {
		return Credentials.create(mpsAccountPrivateK, mpsAccountPublicK);
	}

	public Credentials getCredentials(DecryptedBlockchainAccount decryptedBlockchainAccount) {
		return Credentials.create(decryptedBlockchainAccount.getPrivateKey());//, decryptedBlockchainAccount.getPublicKey());
	}
}
