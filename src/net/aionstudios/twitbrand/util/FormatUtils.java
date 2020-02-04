package net.aionstudios.twitbrand.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Winter Roberts
 * An extra utility class for formatting not defined easily in a broader
 * terminology.
 */
public class FormatUtils {
	
	/**
	 * URL encodes the given UTF-8 String. Allowing it to be written into a GET
	 * or POST statement safely.
	 * 
	 * @param value The value to be encoded.
	 * @return The URL encoded equivalent of the input String.
	 */
	public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}
