package domain;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encriptor {
	public static class Builder{
		public final PrivateKey privateKey;
		public final Encriptor encriptor;
		public static Encriptor fromKey(PublicKey key) {
			return new Encriptor(key);
		}
		
		public Builder(){
			
		      
		    KeyPair pair = generateKeys();
		    privateKey = pair.getPrivate();
		    
		    encriptor = new Encriptor(pair.getPublic());
			
		}
	}
	public static KeyPair generateKeys() {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	      
	    keyPairGen.initialize(2048);
	    return keyPairGen.generateKeyPair();
	}
	private final Cipher cipher;
	private final PublicKey key;
	private Encriptor(PublicKey key) {
		try {
			
			this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			this.cipher.init(Cipher.ENCRYPT_MODE, key);
			this.key = key; 
		
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public PublicKey getKey() {
		return key;
	}
	
	public String encrypt(String in) {
		cipher.update(in.getBytes());
		try {
			return new String(Base64.getEncoder().encode(cipher.doFinal()));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}
