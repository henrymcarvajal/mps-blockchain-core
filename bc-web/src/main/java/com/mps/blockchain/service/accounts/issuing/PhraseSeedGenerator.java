package com.mps.blockchain.service.accounts.issuing;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.stream.IntStream;

public class PhraseSeedGenerator {
    
    private static final int PHRASE_SEED = 12;
    
    public String generateSeed() {
        StringBuilder builder = new StringBuilder();
        IntStream wordIndexes = new SecureRandom().ints(PHRASE_SEED, 0, BIP39WordList.BIP39_UPPER_LIMIT);
        Iterator<Integer> it = wordIndexes.iterator();
        while (it.hasNext()) {
            builder.append(BIP39WordList.get(it.next()));
            builder.append(" ");
        }
        return builder.toString().trim();
    }
    
}
