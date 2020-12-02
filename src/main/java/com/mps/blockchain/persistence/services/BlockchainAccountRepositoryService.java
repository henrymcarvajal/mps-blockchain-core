package com.mps.blockchain.persistence.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.mps.blockchain.config.BlockchainPasswords;
import com.mps.blockchain.model.DecryptedBlockchainAccount;
import com.mps.blockchain.model.BlockchainAccount;
import com.mps.blockchain.persistence.repository.BlockchainAccountRepository;
//import com.mps.blockchain.security.EncryptorAesGcmPassword;

@Service
public class BlockchainAccountRepositoryService {

	/*@Autowired
	private BlockchainPasswords blockChainPasswords;*/

	@Autowired
	private BlockchainAccountRepository repository;

	public Optional<DecryptedBlockchainAccount> findById(UUID id) {
		Optional<BlockchainAccount> optional = repository.findById(id);
		if (optional.isPresent()) {
			DecryptedBlockchainAccount decryptedBlockchainAccount = decryptValues(optional.get());
			return Optional.of(decryptedBlockchainAccount);
		}
		return Optional.empty();
	}

	public void create(DecryptedBlockchainAccount decryptedBlockchainAccount) {
		if (decryptedBlockchainAccount == null) {
			throw new IllegalArgumentException("decryptedBlockchainAccount is null");
		}
		BlockchainAccount encryptedBlockchainAccount = encryptValues(decryptedBlockchainAccount);
		encryptedBlockchainAccount.setCreatedDate(LocalDateTime.now());
		repository.save(encryptedBlockchainAccount);
		decryptedBlockchainAccount.setId(encryptedBlockchainAccount.getId());
		decryptedBlockchainAccount.setCreatedDate(encryptedBlockchainAccount.getCreatedDate());
		decryptedBlockchainAccount.setModifiedDate(encryptedBlockchainAccount.getModifiedDate());
	}

	private BlockchainAccount encryptValues(DecryptedBlockchainAccount decryptedBlockchainAccount) {
		BlockchainAccount encryptedBlockchainAccount = new BlockchainAccount();
		encryptedBlockchainAccount.setId(decryptedBlockchainAccount.getId());
		encryptedBlockchainAccount.setCreatedDate(decryptedBlockchainAccount.getCreatedDate());
		encryptedBlockchainAccount.setModifiedDate(decryptedBlockchainAccount.getModifiedDate());

		try {
			/*String encPassword = EncryptorAesGcmPassword.encrypt(
					decryptedBlockchainAccount.getPassword().getBytes("UTF-8"),
					blockChainPasswords.forAccountPasswords());*/
			// encryptedBlockchainAccount.setPassword(encPassword);
			encryptedBlockchainAccount.setPassword(decryptedBlockchainAccount.getPassword());
			//encPassword = null;

			/*String encSeedPhrase = EncryptorAesGcmPassword.encrypt(
					decryptedBlockchainAccount.getSeedPhrase().getBytes("UTF-8"),
					blockChainPasswords.forAccountSeedPhrases());*/
			encryptedBlockchainAccount.setSeedPhrase(decryptedBlockchainAccount.getSeedPhrase());
			//encSeedPhrase = null;

			/*String encPrivateKey = EncryptorAesGcmPassword.encrypt(
					decryptedBlockchainAccount.getPrivateKey().getBytes("UTF-8"),
					blockChainPasswords.forAccountPrivateKeys());*/
			encryptedBlockchainAccount.setPrivateKey(decryptedBlockchainAccount.getPrivateKey());
			//encPrivateKey = null;

			/*String encPublicKey = EncryptorAesGcmPassword.encrypt(
					decryptedBlockchainAccount.getPublicKey().getBytes("UTF-8"),
					blockChainPasswords.forAccountPublicKeys());*/
			encryptedBlockchainAccount.setPublicKey(decryptedBlockchainAccount.getPublicKey());
			//encPublicKey = null;

			/*String encAddress = EncryptorAesGcmPassword.encrypt(
					decryptedBlockchainAccount.getAddress().getBytes("UTF-8"),
					blockChainPasswords.forAccountAddresses());*/
			encryptedBlockchainAccount.setAddress(decryptedBlockchainAccount.getAddress());
			//encAddress = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encryptedBlockchainAccount;
	}

	private DecryptedBlockchainAccount decryptValues(BlockchainAccount encryptedBlockchainAccount) {
		DecryptedBlockchainAccount decryptedBlockchainAccount = new DecryptedBlockchainAccount();
		decryptedBlockchainAccount.setId(encryptedBlockchainAccount.getId());
		decryptedBlockchainAccount.setCreatedDate(encryptedBlockchainAccount.getCreatedDate());
		decryptedBlockchainAccount.setModifiedDate(encryptedBlockchainAccount.getModifiedDate());

		try {
			// String decPassword = EncryptorAesGcmPassword.decrypt(encryptedBlockchainAccount.getPassword(), blockChainPasswords.forAccountPasswords());
			// decryptedBlockchainAccount.setPassword(decPassword);
			decryptedBlockchainAccount.setPassword(encryptedBlockchainAccount.getPassword());
			//decPassword = null;

			/*String decSeedPhrase = EncryptorAesGcmPassword.decrypt(encryptedBlockchainAccount.getSeedPhrase(),					blockChainPasswords.forAccountSeedPhrases());
			decryptedBlockchainAccount.setSeedPhrase(decSeedPhrase);*/
			decryptedBlockchainAccount.setSeedPhrase(encryptedBlockchainAccount.getSeedPhrase());
			//decSeedPhrase = null;

			//String decPrivateKey = EncryptorAesGcmPassword.decrypt(encryptedBlockchainAccount.getPrivateKey(),
			//		blockChainPasswords.forAccountPrivateKeys());
			//decryptedBlockchainAccount.setPrivateKey(decPrivateKey);
			decryptedBlockchainAccount.setPrivateKey(encryptedBlockchainAccount.getPrivateKey());
			//decPrivateKey = null;

			//String decPublicKey = EncryptorAesGcmPassword.decrypt(encryptedBlockchainAccount.getPublicKey(),
			//		blockChainPasswords.forAccountPublicKeys());
			//decryptedBlockchainAccount.setPublicKey(decPublicKey);
			decryptedBlockchainAccount.setPublicKey(encryptedBlockchainAccount.getPublicKey());
			//decPublicKey = null;

			//String decAddress = EncryptorAesGcmPassword.decrypt(encryptedBlockchainAccount.getAddress(),
			//		blockChainPasswords.forAccountAddresses());
			//decryptedBlockchainAccount.setAddress(decAddress);
			decryptedBlockchainAccount.setAddress(encryptedBlockchainAccount.getAddress());
			//decAddress = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return decryptedBlockchainAccount;
	}
}
