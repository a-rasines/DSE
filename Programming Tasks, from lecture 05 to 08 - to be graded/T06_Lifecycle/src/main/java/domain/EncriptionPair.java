package domain;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncriptionPair {
	
	public final Encriptor encriptor;
	public final Decriptor decriptor;
	
	public static EncriptionPair fromKeyPair(KeyPair pair) {
		return new EncriptionPair(
				Encriptor.Builder.fromKey(pair.publicKey),
				Decriptor.Builder.fromKey(pair.privateKey));		
	}
	
	public static EncriptionPair fromKeyStrings(byte[] publicKey, byte[] privateKey) {
	     try {
	    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey puk = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
			PrivateKey prk = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
			
			return fromKeyPair(new KeyPair(puk, prk));

		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	public EncriptionPair(Encriptor e, Decriptor d) {
		this.encriptor = e;
		this.decriptor = d;
	}
	
	public static class KeyPair {
		
		public final PublicKey publicKey;
		public final PrivateKey privateKey;
		
		public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
			super();
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}
		
	}
}
