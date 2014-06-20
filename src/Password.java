import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Password implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5074126984308457819L;
	private byte[] ePassword;
	private byte[] key, src, A;

	public Password(byte[] pass, boolean encrypted) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated constructor stub

		if (encrypted)
			ePassword = pass;
		else {
			ePassword = encrypte(pass);
		}
	}

	public byte[] getEncryptedPass() {

		return ePassword;
	}

	private byte[] encrypte(byte[] plainText) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/EAX/NoPadding", "BC");
		SecretKeySpec key = new SecretKeySpec(this.key, "AES");

		// GCMParameterSpec mapped to AEADParameters and overrides default MAC
		// size
		GCMParameterSpec spec = new GCMParameterSpec(128, this.src);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);

		cipher.updateAAD(this.A);
		byte[] cipherText = cipher.doFinal(plainText);
		return cipherText;
	}

	private byte[] decrypte() throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/EAX/NoPadding", "BC");
		SecretKeySpec key = new SecretKeySpec(this.key, "AES");

		// GCMParameterSpec mapped to AEADParameters and overrides default MAC
		// size
		GCMParameterSpec spec = new GCMParameterSpec(128, this.src);
		cipher.init(Cipher.DECRYPT_MODE, key, spec);

		cipher.updateAAD(this.A);
		byte[] plainText = cipher.doFinal(ePassword);
		return plainText;
	}

	public byte[] getPass() throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// TODO

		return decrypte();
	}

}