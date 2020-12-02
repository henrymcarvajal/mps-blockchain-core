package com.mps.blockchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockchainPasswords {
	
	@Value("${blockchain.account.password.password}")
	private String passwordForAccountPasswords;

	@Value("${blockchain.account.address.password}")
	private String passwordForAccountAddresses;
	
	@Value("${blockchain.account.seedphrase.password}")
	private String passwordForAccountSeedPhrases;

	@Value("${blockchain.account.privatekey.password}")
	private String passwordForAccountPrivateKeys;

	@Value("${blockchain.account.publickey.password}")
	private String passwordForAccountPublicKeys;

	@Value("${escrow.seller.account.password}")
	private String passwordForSellerAccounts;
	
	@Value("${escrow.buyer.account.password}")
	private String passwordForBuyerAccounts;

	public String forAccountPasswords() {
		return passwordForAccountPasswords;
	}

	public String forAccountAddresses() {
		return passwordForAccountAddresses;
	}

	public String forAccountSeedPhrases() {
		return passwordForAccountSeedPhrases;
	}

	public String forAccountPrivateKeys() {
		return passwordForAccountPrivateKeys;
	}

	public String forAccountPublicKeys() {
		return passwordForAccountPublicKeys;
	}

	public String forSellerAccounts() {
		return passwordForSellerAccounts;
	}

	public String forBuyerAccounts() {
		return passwordForBuyerAccounts;
	}
	
	
}
