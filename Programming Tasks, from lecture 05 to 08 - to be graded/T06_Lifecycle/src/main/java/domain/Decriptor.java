package domain;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Decriptor {
	public static class Builder {
		public static Decriptor fromKey(PrivateKey key) {
			return new Decriptor(key);
		}
		public final PublicKey key;
		public final Decriptor dec;
		public Builder() {
			KeyPair pair = Encriptor.generateKeys();
			dec = new Decriptor(pair.getPrivate());
			key = pair.getPublic();
					
		}
	}
	private final Cipher cipher;
	private final PrivateKey key;
	private Decriptor (PrivateKey key) {
		try {
			
			this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			this.cipher.init(Cipher.DECRYPT_MODE, key);
			this.key = key; 
		
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public PrivateKey getKey() {
		return key;
	}
	
	public String decrypt(String in) {
		try {
			return new String(cipher.doFinal(Base64.getDecoder().decode(in)));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
