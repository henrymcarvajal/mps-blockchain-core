package com.mps.blockchain.security;

public class DecryptionException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6362590903887090723L;

    public DecryptionException() {
        super();
    }

    public DecryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(Throwable cause) {
        super(cause);
    }
}
