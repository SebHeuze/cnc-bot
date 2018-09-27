package org.cnc.cncbot.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Class of utils for HTTP calls
 * @author sheuze
 *
 */
public class HttpUtils {
	/**
	 * Get parameters list of URL
	 * @param url
	 * @return
	 */
	public static Map<String, String> queryToMap(String query) {
	    Map<String, String> result = new HashMap<>();
	    for (String param : query.split("&")) {
	        String[] entry = param.split("=");
	        if (entry.length > 1) {
	            result.put(entry[0], entry[1]);
	        }else{
	            result.put(entry[0], "");
	        }
	    }
	    return result;
	}
}
