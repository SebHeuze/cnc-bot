package org.cnc.cncbot.config;

public class DBContext {
	 private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	  public static void setSchema(String schema) {
	    CONTEXT.set(schema);
	  }
	  
	  public static String getSchema() {
		    return CONTEXT.get();
	  }

}
