
package com.emirhg.sistema.ecommerce.controllers.exceptions;


public class ReferentialIntegrityException extends RuntimeException {
    
    private final int REFERENCING_COUNT;

    public ReferentialIntegrityException(String message, int REFERENCING_COUNT) {
        super(message);
        this.REFERENCING_COUNT = REFERENCING_COUNT;
    }

    public int getREFERENCING_COUNT() {
        return REFERENCING_COUNT;
    }
    
    
    
    
    
}
