package com.mps.blockchain.service.currencies;

/**
 * Currency codes (ISO 4217)
 * 
 * @author Henry Carvajal
 */
public final class Currency {
    
    private Currency() {
        // Empty implementation
    }
    
    public static final class FIAT {
        
        private FIAT() {
            // Empty implementation
        }
        
        public static final String COLOMBIAN_PESO = "COP";
        public static final String US_DOLLAR = "USD";
    }
    
    public static final class CRYPTO {
        
        private CRYPTO() {
            // Empty implementation
        }
        
        public static final String XDAI = "XDAI";
    }
}
