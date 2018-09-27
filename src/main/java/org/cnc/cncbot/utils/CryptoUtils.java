package org.cnc.cncbot.utils;

import java.util.ArrayList;
import java.util.List;

import org.cnc.cncbot.map.dto.DecryptResult;

import lombok.extern.slf4j.Slf4j;



/**
 * Helper pour crypter décrypter le base 91.
 * @author SEB
 *
 */
@Slf4j
public final class CryptoUtils {
    /**
     * Tableau correspondance base 91.
     */
    public static final List<Character> ENCODING_TABLE = new ArrayList<Character>();

    public static final char CNC_POLL_RESPONSE_SEPARATOR = '-';
    static {
        /**
         * String correspondance base 91
         */
        String base91Correspondance = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&()*+,. :;<=>?@[]^_`{|}~'";
        for (final char c : base91Correspondance.toCharArray()) {
            ENCODING_TABLE.add(c);
        }
    }

    private CryptoUtils() {

    }
 
    /**
     * Décrypt result.
     * @param data
     * @param indexDebut
     * @param profondeur
     * @return
     */
    public static DecryptResult base91Decode(String data, int indexDebut, int profondeur) {
        int retour = 0;
        int rang = 1;
        int currentIndex = 0;
        while (currentIndex <= profondeur - 1 && (indexDebut + currentIndex) < data.length()) {

            final char currentChar = data.charAt(indexDebut + currentIndex);
            if (currentChar == CNC_POLL_RESPONSE_SEPARATOR) {
                currentIndex++;
                break;
            }
            final int charValue = ENCODING_TABLE.indexOf(currentChar);
            retour = retour + (rang * charValue);
            currentIndex++;
            rang *= 91;
        }
        final int index = indexDebut + currentIndex;
        return new DecryptResult(index, retour);
    }

    /**
     * Encoder en base 91.
     * @param intToCrypt
     * @return
     */
    public static String base91Encode(int intToCrypt) {
      //log.debug("Crypt : {}", intToCrypt);
        StringBuilder cryptResult = new StringBuilder();
        int index = 0;
        while (index < 5) {
            if (intToCrypt == 0 && index == 0) {
                cryptResult.append("AA");
                break;
            } else if (intToCrypt == 0 && index == 1) {
                cryptResult.append('A');
                break;
            } else if (intToCrypt == 0) {
                break;
            }
            cryptResult.append(ENCODING_TABLE.get(intToCrypt % 91));
            intToCrypt = intToCrypt / 91;
            index++;
        }

        //log.debug("Crypted  : {}", cryptResult);
        return cryptResult.toString();
    }
}
