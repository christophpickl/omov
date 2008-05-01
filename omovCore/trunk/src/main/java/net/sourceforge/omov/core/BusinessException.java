package net.sourceforge.omov.core;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 7338461595250391128L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Exception cause) {
        super(message, cause);
    }

}
