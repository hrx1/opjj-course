package hr.fer.zemris.java.tecaj_13.web.servlets.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utity methods used in servlets
 * 
 * @author Hrvoje
 *
 */
public class Utils {
	
	/** SHA buffer size */
	private final static int SHA_BUFFER_SIZE = 1024;
	
	/**
	 * Hashes string with SHA-256
	 * 
	 * @param string
	 * @return
	 */
	public static String hash(String string) {
		try {
			return new String(getStreamDigestSHA(new ByteArrayInputStream(string.getBytes())));
		} catch (NoSuchAlgorithmException | IOException ignorable) {
			ignorable.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Hashes input with SHA-256
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static byte[] getStreamDigestSHA(InputStream input) throws NoSuchAlgorithmException, IOException {
		
		MessageDigest sha = MessageDigest.getInstance("SHA-256");

		byte[] tmp = new byte[SHA_BUFFER_SIZE];
		int length;
		while (true) {
			length = input.read(tmp, 0, tmp.length);
			
			if(length == -1) break;
			
			sha.update(tmp, 0, length);
		}

		return sha.digest();
	}

}
