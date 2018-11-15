package org.cnc.cncbot.utils;

import lombok.extern.slf4j.Slf4j;


/**
 * Class of utils for HTTP calls
 * @author SebHeuze
 *
 */
@Slf4j
public class CncUtils {
	
	/**
     * Construire la requête Poll.
     */
    public static String buildPollRequest(int mapX, int maxY) {
        StringBuilder requestString = new StringBuilder();
        log.debug("Génération String pour la requête X :{}", mapX);
        for (int y = 0; y < maxY; y++) {
            requestString.append(CryptoUtils.base91Encode((y << 8) | mapX) + "A-");
        }
        log.debug("Requête générée :{}", requestString);
        String finalRequest = "UA\fWC:A\fTIME:" + System.currentTimeMillis() + "\fCHAT:\fWORLD:" + requestString.toString() + "\fGIFT:\fACS:0\fASS:0\fCAT:0\f";
        log.debug("Requête finale :{}", finalRequest);
        return finalRequest;
    }
}
