package com.nodedynamics.userservices.common;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {
	
	public static class PasswordGen
	{
		private static final int ITERATIONS=1000;
		private static final int KEY_LENGTH=192;
		private static final Random RANDOM = new SecureRandom();
		
		public static String EncPassword(String password, String salt)
		{
			char[] passwordChars = password.toCharArray();
		    byte[] saltBytes = salt.getBytes();
		    SecretKeyFactory key;
		    byte[] hashedPassword = null;

		    PBEKeySpec spec = new PBEKeySpec(
		        passwordChars,
		        saltBytes,
		        ITERATIONS,
		        KEY_LENGTH
		    );
		    
			try {
				key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
				hashedPassword = key.generateSecret(spec).getEncoded();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    return String.format("%x", new BigInteger(hashedPassword));
		}
		
		public static String genSalt()
		{
			byte[] salt = new byte[16];
			RANDOM.nextBytes(salt);
			
			return salt.toString();
		}
		
	}

}
