package org.cnc.cncbot.exception;

/**
 * Auth Exception.
 * @author SEB
 *
 */
public class AuthException extends RuntimeException {
    

    /**
	 * Generated serial 
	 */
	private static final long serialVersionUID = -8240656342001000101L;

	/**
     * Constructeur.
     * @param message
     */
    public AuthException(String message) {
        super(message);
    }
}