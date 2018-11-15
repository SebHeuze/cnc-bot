package org.cnc.cncbot.exception;

/**
 * EA Auth Exception.
 * @author SebHeuze
 *
 */
public class EAAuthException extends RuntimeException {
    

    /**
	 * Generated serial 
	 */
	private static final long serialVersionUID = -8240656342001000101L;

	/**
     * Constructeur.
     * @param message
     */
    public EAAuthException(String message) {
        super(message);
    }
}