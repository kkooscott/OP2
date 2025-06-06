package npa.op.util;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class MessageUtil {
    protected static Logger log = Logger.getLogger(MessageUtil.class);
    
    public static final String PROPERTY_FILE = "messages";
    public static ResourceBundle rb = ResourceBundle.getBundle(PROPERTY_FILE);

    public static String getString(String key) {
	String value = "";
	try {
	    value = rb.getString(key).trim();
	} catch (Exception e) {
	    log.error(ExceptionUtil.toString(e));
	}
	return value;
    }

    public static int getInt(String key) throws NumberFormatException {
	return Integer.parseInt(getString(key));
    }

    public static int getShort(String key) throws NumberFormatException {
	return Short.parseShort(getString(key));
    }

    public static boolean getBoolean(String key) {
	return Boolean.parseBoolean(getString(key));
    }
}

