import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Domain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126057441259313915L;
	String eDomain;

	public Domain(String domain, boolean encrypted, String key) {
		// TODO Auto-generated constructor stub

		if (!encrypted) {
			try {
				eDomain = encrypte(domain, key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			eDomain = domain;
		}
	}

	public boolean authenticate() {
		// TODO
		return false;
	}

	public String getEncrypted() {
		return eDomain;
	}

	public String getDomain(String key) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		// TODO
		// return decrypte(eDomain, key);
		return null;
	}

	private String encrypte(String plainText, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
				"HmacSHA512");
		Mac mac = Mac.getInstance("HmacSHA512");
		mac.init(signingKey);
		return toHexString(mac.doFinal(plainText.getBytes()));
	}

	// private String decrypte(String cipherText, String key) throws
	// NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	// IllegalBlockSizeException, BadPaddingException
	// {
	// byte[] plaintext = new byte[0];
	// SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
	// "HmacSHA512");
	// Cipher cipher = Cipher.getInstance("HmacSHA512");
	// cipher.init(Cipher.DECRYPT_MODE, signingKey);
	// plaintext = cipher.doFinal(cipherText.getBytes());
	// return new String(plaintext);
	// }

	private String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();

		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		String res = formatter.toString();
		formatter.close();
		return res;
	}

	public static void main(String[] args) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		for (int i = 0; i < 1; i++) {
			Domain dom = new Domain("domain", false, "key");
			System.out.println(dom.getEncrypted());
			System.out.println(dom.getDomain("key"));

		}

	}
}
