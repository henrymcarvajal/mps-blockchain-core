package com.mps.blockchain.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.accounts.AccountIssuer;
import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.persistence.repository.BuyerAccountRepository;
import com.mps.blockchain.persistence.repository.SellerAccountRepository;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;

@Service
public class AccountManager {

	@Autowired
	private BlockchainAccountRepositoryService blockchainAccountRepositoryService;

	@Autowired
	private SellerAccountRepository sellerAccountRepository;

	@Autowired
	private BuyerAccountRepository buyerAccountRepository;

	@Autowired
	private AccountIssuer accountIssuer;

	public SellerAccount getSellerAccount(UUID id) {
		Optional<SellerAccount> optional = sellerAccountRepository.findByMpsSellerId(id);
		if (!optional.isEmpty()) {
			return optional.get();
		}

		DecryptedBlockchainAccount blockchainAccount = accountIssuer.issueAccount();
		blockchainAccountRepositoryService.create(blockchainAccount);

		SellerAccount sellerAccount = new SellerAccount();
		sellerAccount.setMpsSellerId(id);
		sellerAccount.setBlockchainAccountId(blockchainAccount.getId());

		sellerAccountRepository.save(sellerAccount);

		return sellerAccount;
	}

	public BuyerAccount getBuyerAccount(UUID id) {
		Optional<BuyerAccount> optional = buyerAccountRepository.findByMpsBuyerId(id);
		if (!optional.isEmpty()) {
			return optional.get();
		}

		DecryptedBlockchainAccount blockchainAccount = accountIssuer.issueAccount();
		blockchainAccountRepositoryService.create(blockchainAccount);

		BuyerAccount buyerAccount = new BuyerAccount();
		buyerAccount.setMpsBuyerId(id);
		buyerAccount.setBlockchainAccountId(blockchainAccount.getId());

		buyerAccountRepository.save(buyerAccount);

		return buyerAccount;
	}
}
