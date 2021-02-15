package com.mps.blockchain.service.accounts.issuing;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import com.mps.blockchain.model.BlockchainAccount;

@Component
public class AccountIssuer {
    
    // Derivation path wanted (Etherium derivation path): // m/44'/60'/0'/0
    private static final int[] derivationPath = { 44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT,
            0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0 };
    
    public BlockchainAccount issueAccount() {
        String password = new SecurePasswordGenerator().generatePassword();
        String seedPhrase = new PhraseSeedGenerator().generateSeed();
        
        Bip32ECKeyPair derivedKeyPair = generateKeyPair(password, seedPhrase);
        
        BlockchainAccount account = new BlockchainAccount();
        account.setPassword(password);
        account.setSeedPhrase(seedPhrase);
        account.setPrivateKey(derivedKeyPair.getPrivateKey().toString(16));
        account.setPublicKey(derivedKeyPair.getPublicKey().toString(16));
        account.setAddress(getAddress(derivedKeyPair));
        
        return account;
    }
    
    private Bip32ECKeyPair generateKeyPair(String password, String mnemonic) {
        // Generate a BIP32 master keypair from the mnemonic phrase
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonic, password));
        
        // Derived the key using the derivation path
        return Bip32ECKeyPair.deriveKeyPair(masterKeypair, derivationPath);
    }
    
    private String getAddress(Bip32ECKeyPair derivedKeyPair) {
        // Load the wallet for the derived key
        Credentials credentials = Credentials.create(derivedKeyPair);
        
        // Return generated address
        return credentials.getAddress();
    }
    
}
