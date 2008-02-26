package at.ac.tuwien.e0525580.omov2;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 7338461595250391128L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Exception cause) {
        super(message, cause);
    }

}
