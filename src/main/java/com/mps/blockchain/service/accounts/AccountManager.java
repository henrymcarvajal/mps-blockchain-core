package com.mps.blockchain.service.accounts;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.BuyerAccount;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.SellerAccount;
import com.mps.blockchain.persistence.repository.BuyerAccountRepository;
import com.mps.blockchain.persistence.repository.SellerAccountRepository;
import com.mps.blockchain.persistence.services.BlockchainAccountRepositoryService;
import com.mps.blockchain.service.accounts.issuing.AccountIssuer;

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
		SellerAccount sellerAccount = null;
		DecryptedBlockchainAccount blockchainAccount = null;

		Optional<SellerAccount> optional = sellerAccountRepository.findByMpsSellerId(id);
		if (!optional.isEmpty()) {
			sellerAccount = optional.get();
			Optional<DecryptedBlockchainAccount> blockchainAccountOptional = blockchainAccountRepositoryService
					.findById(sellerAccount.getBlockchainAccountId());
			if (blockchainAccountOptional.isPresent()) {
				blockchainAccount = blockchainAccountOptional.get();
			}
		} else {
			blockchainAccount = accountIssuer.issueAccount();
			blockchainAccountRepositoryService.create(blockchainAccount);

			sellerAccount = new SellerAccount();
			sellerAccount.setMpsSellerId(id);
			sellerAccount.setBlockchainAccountId(blockchainAccount.getId());

			sellerAccountRepository.save(sellerAccount);
		}

		return sellerAccount;
	}

	public BuyerAccount getBuyerAccount(UUID id) {
		BuyerAccount buyerAccount = null;
		DecryptedBlockchainAccount blockchainAccount = null;

		Optional<BuyerAccount> optional = buyerAccountRepository.findByMpsBuyerId(id);
		if (!optional.isEmpty()) {
			buyerAccount = optional.get();
			Optional<DecryptedBlockchainAccount> blockchainAccountOptional = blockchainAccountRepositoryService
					.findById(buyerAccount.getBlockchainAccountId());
			if (blockchainAccountOptional.isPresent()) {
				blockchainAccount = blockchainAccountOptional.get();
			}
		} else {
			blockchainAccount = accountIssuer.issueAccount();
			blockchainAccountRepositoryService.create(blockchainAccount);

			buyerAccount = new BuyerAccount();
			buyerAccount.setMpsBuyerId(id);
			buyerAccount.setBlockchainAccountId(blockchainAccount.getId());

			buyerAccountRepository.save(buyerAccount);
		}

		return buyerAccount;
	}
}
