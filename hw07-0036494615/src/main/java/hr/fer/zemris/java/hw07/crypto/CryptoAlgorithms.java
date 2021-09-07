package hr.fer.zemris.java.hw07.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class provides 2 crypto algorithms. 
 * @author Hrvoje
 *
 */
public class CryptoAlgorithms {
	/** SHA buffer size used in diagest calculation **/
	final static int SHA_BUFFER_SIZE = 1024;
	
	/** AES buffer size used in cipher calculation **/
	final static int AES_BUFFER_SIZE = 1024;
	
	/**
	 * Returns SHA digest of a stream. Method doesn't close stream.
	 * @param input stream
	 * @return SHA digest of a stream
	 * @throws NoSuchAlgorithmException if there's no SHA algorithm
	 * @throws IOException if cannot read from stream
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
	
	/**
	 * Outputs AES cipher/decipher of a stream to output stream. Uses key text and initialization vector.
	 * Ciphers if flag encrypt is <code>true</code>, deciphers otherwise
	 * 
	 * @param input stream
	 * @param output stream
	 * @param keyText for AES
	 * @param initVectorText for AES
	 * @param encrypt flag, cipher if true, deciphers otherwise
	 * @throws NoSuchAlgorithmException if there is no AES algorithm
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException if Key is invalid
	 * @throws InvalidAlgorithmParameterException if wrong parameters are passed
	 * @throws IllegalBlockSizeException if block size is wrong
	 * @throws BadPaddingException if padding is wrong
	 * @throws IOException if you can't read/write to input/output stream
	 */
	public static void cipherStreamAES(InputStream input, OutputStream output, String keyText, String initVectorText, boolean encrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		SecretKeySpec keySpec = new SecretKeySpec(CryptoUtil.hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(CryptoUtil.hexToByte(initVectorText));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		
		byte[] tmp = new byte[AES_BUFFER_SIZE];
		byte[] processedTmp;
		int length;
		while(true) {
			length = input.read(tmp, 0, tmp.length);
			
			if(length == -1) break;
			
			processedTmp = cipher.update(tmp, 0, length);
			
			output.write(processedTmp);
		}
		
		output.write(cipher.doFinal());
	}

}
