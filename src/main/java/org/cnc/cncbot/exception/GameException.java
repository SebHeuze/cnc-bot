package org.cnc.cncbot.exception;

/**
 * Game Exception.
 * @author SEB
 *
 */
public class GameException extends RuntimeException {
    
	/**
	 * Generated Serial
	 */
	private static final long serialVersionUID = -8255281427946087587L;

	/**
     * Constructeur.
     * @param message
     */
    public GameException(String message) {
        super(message);
    }
}