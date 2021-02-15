package com.mps.blockchain.service.accounts;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.model.BuyerAccount;
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
    
    public SellerAccount getSellerAccount(UUID mpsSellerId) {
        return getSellerAccount(mpsSellerId, false);
    }
    
    public SellerAccount getSellerAccount(UUID mpsSellerId, boolean register) {
        SellerAccount sellerAccount = null;
        BlockchainAccount blockchainAccount = null;
        
        Optional<SellerAccount> optional = sellerAccountRepository.findByMpsSellerId(mpsSellerId);
        if (!optional.isEmpty()) {
            sellerAccount = optional.get();
        } else {
            if (register) {
                blockchainAccount = accountIssuer.issueAccount();
                blockchainAccountRepositoryService.create(blockchainAccount);
                
                sellerAccount = new SellerAccount();
                sellerAccount.setMpsSellerId(mpsSellerId);
                sellerAccount.setBlockchainAccountId(blockchainAccount.getId());
                
                sellerAccountRepository.save(sellerAccount);
            }
        }
        
        return sellerAccount;
    }
    
    public BuyerAccount getBuyerAccount(UUID mpsBuyerId) {
        return getBuyerAccount(mpsBuyerId, false);
    }
    
    public BuyerAccount getBuyerAccount(UUID mpsBuyerId, boolean register) {
        BuyerAccount buyerAccount = null;
        BlockchainAccount blockchainAccount = null;
        
        Optional<BuyerAccount> optional = buyerAccountRepository.findByMpsBuyerId(mpsBuyerId);
        if (!optional.isEmpty()) {
            buyerAccount = optional.get();
        } else {
            if (register) {
                blockchainAccount = accountIssuer.issueAccount();
                blockchainAccountRepositoryService.create(blockchainAccount);
                
                buyerAccount = new BuyerAccount();
                buyerAccount.setMpsBuyerId(mpsBuyerId);
                buyerAccount.setBlockchainAccountId(blockchainAccount.getId());
                
                buyerAccountRepository.save(buyerAccount);
            }
        }
        
        return buyerAccount;
    }
}
