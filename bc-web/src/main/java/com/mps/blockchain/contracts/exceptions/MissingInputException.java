package com.mps.blockchain.contracts.exceptions;

public class MissingInputException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3045000605885934006L;
    
    private final String fieldName;
    
    public MissingInputException() {
        super();
        this.fieldName = "";
    }
    
    public MissingInputException(String fieldName) {
        super(fieldName);
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
}
