package net.sourceforge.omov.core;

public class FatalException extends RuntimeException {

    private static final long serialVersionUID = 5469867679628436702L;

    public FatalException(String msg) {
        super(msg);
    }
    
    public FatalException(String msg, Exception t) {
        super(msg, t);
    }
}
