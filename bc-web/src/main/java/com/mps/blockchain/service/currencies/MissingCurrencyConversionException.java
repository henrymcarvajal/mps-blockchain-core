package com.mps.blockchain.service.currencies;

public class MissingCurrencyConversionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8682562765694787984L;

    public MissingCurrencyConversionException() {
        super();
    }

    public MissingCurrencyConversionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MissingCurrencyConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingCurrencyConversionException(String message) {
        super(message);
    }

    public MissingCurrencyConversionException(Throwable cause) {
        super(cause);
    }
}
