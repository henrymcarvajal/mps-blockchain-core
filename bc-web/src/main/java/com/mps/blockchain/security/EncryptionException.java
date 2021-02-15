package com.mps.blockchain.security;

public class EncryptionException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3021175729755080331L;
    
    public EncryptionException() {
        super();
    }
    
    public EncryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EncryptionException(String message) {
        super(message);
    }
    
    public EncryptionException(Throwable cause) {
        super(cause);
    }
}
