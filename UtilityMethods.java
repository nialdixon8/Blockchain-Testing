import java.security.MessageDigest;
import java.util.Base64;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Key;
import java.security.Signature;


public class UtilityMethods {
	private static long uniqueNumber = 0;
	
	public static long getUniqueNumber() {
		return UtilityMethods.uniqueNumber++;
	}
	
	public static byte[] messageDigestSHA256_toBytes(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(message.getBytes());
			return md.digest();
		}catch(java.security.NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	public static String messageDigestSHA256_toString(String message) {
		return Base64.getEncoder().encodeToString(messageDigestSHA256_toBytes(message));
	}
	public static long getTimeStamp() {
		return java.util.Calendar.getInstance().getTimeInMillis();
	}
	public static boolean hashMeetsDifficultyLevel(String hash, int difficultyLevel) {
		char[] c = hash.toCharArray();
		for(int i=0; i<difficultyLevel; i++) {
			if(c[i] != '0') {
				return false;
			}
		}
		return true;
	}
	public static String toBinaryString(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<hash.length; i++) {
			int x = ((int)hash[i])+128;
			String s = Integer.toBinaryString(x);
			while(s.length() < 8) {
				s = "0" +s;
			}
			sb.append(s);
		}
		return sb.toString();
	}
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair pair = kpg.generateKeyPair();
			return pair;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static byte[] generateSignature(PrivateKey privateKey, String message) {
		try {
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(privateKey);
			sig.update(message.getBytes());
			return sig.sign();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static boolean verifySignature(PublicKey publicKey, byte[] signature, String message) {
		try {
			Signature sig2 = Signature.getInstance("SHA256withRSA");
			sig2.initVerify(publicKey);
			sig2.update(message.getBytes());
			return sig2.verify(signature);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String getKeyString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	

}
