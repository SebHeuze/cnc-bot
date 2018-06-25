package org.cnc.cncbot.map.utils;

import java.net.URL;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;


public class HttpUtils {
	/**
	 * Get param list of URL
	 * @param url
	 * @return
	 */
	public static Map<String, List<String>> splitQuery(URL url) {
	    if (StringUtils.isEmpty(url.getQuery())) {
	        return Collections.emptyMap();
	    }
	    return Arrays.stream(url.getQuery().split("&"))
	            .map(HttpUtils::splitQueryParameter)
	            .collect(Collectors.groupingBy(SimpleImmutableEntry::getKey, LinkedHashMap::new, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}

	public static SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
	    final int idx = it.indexOf("=");
	    final String key = idx > 0 ? it.substring(0, idx) : it;
	    final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
	    return new SimpleImmutableEntry<>(key, value);
	}
}
