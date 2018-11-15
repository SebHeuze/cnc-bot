package org.cnc.cncbot.exception;

/**
 * Batch Exception.
 * @author SebHeuze
 *
 */
public class BatchException extends RuntimeException {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7504815016405157467L;

    /**
     * Constructeur.
     * @param message
     */
    public BatchException(String message) {
        super(message);
    }
}