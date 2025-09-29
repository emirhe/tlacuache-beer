
package com.emirhg.sistema.ecommerce.controllers.exceptions;


public class DomainConstraintException extends RuntimeException {
    public DomainConstraintException(String message) { super(message); }
    public DomainConstraintException(String message, Throwable cause) { super(message, cause); }
}

