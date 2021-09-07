package hr.fer.zemris.java.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import static hr.fer.zemris.java.hw07.crypto.CryptoAlgorithms.*;

/**
 * Crypto class allows users to encrypt/decrypt given file using the AES
 * cryptoalgorithm and the 128-bit encryption key or calculate and check the
 * SHA-256 file digest.
 * 
 * First parameter passed to main method should be checksha, encrypt or decrypt.
 * 
 * Checksha
 *  - parameter to main method is path to file
 *  - asks for expected SHA value, and compares it to the actual value of a given file.
 * 
 * Encrypt / Decrypt
 *  - parameters to main methods are path to original and encrypted file
 * 
 * @author Hrvoje
 *
 */
public class Crypto {

	/**
	 * First parameter passed to main method should be checksha, encrypt or decrypt.
	 * 
	 * Checksha - parameter to main method is path to file - asks for expected SHA
	 * value, and compares it to the actual value of a given file.
	 * 
	 * Encrypt / Decrypt - parameters to main methods are path to original and
	 * encrypted file
	 * 
	 * @param args method to use, and it's parameters
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Operation must be passed as an argument to the Cipher");
			return;
		}
		
		SupportedOperations oper;
		try {
			oper = SupportedOperations.valueOf(args[0]);
		}catch(IllegalArgumentException e) {
			System.out.println(args[0] + " is not valid operation. Valid operations are: " + Arrays.asList(SupportedOperations.values()));
			return ;
		}
	
		switch(oper) {
		
			case checksha:
				if(args.length != 2) {
					System.out.println("checksha requires only 1 argument.");
					return;
				}
				checkSHA(args[1]);
				break;
				
			case decrypt:
				if(args.length != 3) {
					System.out.println("decrypt requires only 2 arguments.");
				}
				cipherAES(args[1], args[2], false);
				break;
				
			case encrypt:
				if(args.length != 3) {
					System.out.println("encrypt requires only 2 arguments.");
				}
				cipherAES(args[1], args[2], true);
				break;
		}

	}
	
	/**
	 * Performs AES cipher encryption/decryption over source and destination depending on encrypt flag.
	 * @param source of a file
	 * @param destination of a file
	 * @param encrypt encrypts if <code>true</code>, decrypts otherwise
	 */
	private static void cipherAES(String source, String destination, boolean encrypt) {
		Path originalFile = Paths.get(source);
		Path processedFile = Paths.get(destination);
		
		if(!Files.isReadable(originalFile)) {
			System.out.println("Error while reading from a file: " + source);
			return ;
		}
		
		try {
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
			String keyText = sc.nextLine();
			System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
			String initVectorText = sc.nextLine();
			
			sc.close();
			
			InputStream input = new BufferedInputStream(new FileInputStream(originalFile.toFile()));
			OutputStream output = new BufferedOutputStream(new FileOutputStream(processedFile.toFile()));
			
			CryptoAlgorithms.cipherStreamAES(input, output, keyText, initVectorText, encrypt);
			
			input.close();
			output.close();
			
			System.out.format("%s completed! Generated file %s based on %s.%n", (encrypt)?"Encryption ":"Decryption", destination, source);
		}catch(Exception e) {
			return ;
		}
 	}
	
	/**
	 * Chechs SHA value of a file. Requires expected SHA value from user.
	 * @param filePath of a file
	 */
	private static void checkSHA(String filePath) {
		//Prvo probaj ucitati datoteku jer mozda ne postoji:
		Path file = Paths.get(filePath);
		
		byte[] resultDigest;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(file.toFile()));
			resultDigest = getStreamDigestSHA(is);
			is.close();
		} catch (NoSuchAlgorithmException nsa) {
			System.out.println("SHA-256 algorithm not supported.");
			return ;
		} catch (IOException e) {
			System.out.println("Error while reading from a file: " + filePath);
			return ;
		}
		
		//trazi unos korisnika:
		System.out.println("Please provide expected sha-256 digest for " + filePath + ":");
		
		Scanner sc = new Scanner(System.in);	
		String expectedDigest = sc.nextLine();
		sc.close();
		
		System.out.format("Digesting completed. Digest of a %s %s.%nExpected was: %s%n", 
					filePath, 
					(Arrays.equals(CryptoUtil.hexToByte(expectedDigest), resultDigest) ? "matches":"does not match"),
					CryptoUtil.byteToHex(resultDigest));
		
	}
	
	/**
	 * Supported operations
	 * @author Hrvoje
	 *
	 */
	private static enum SupportedOperations {
		checksha,
		encrypt,
		decrypt;
	}
}
