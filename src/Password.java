import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Password implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5074126984308457819L;
	private static final int length = 32;
	private byte[] ePassword, ivBytes;
	private SecretKeySpec key ;
	private static final byte[] special = "$".getBytes();
	public Password(byte[] pass, boolean encrypted) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated constructor stub

		if (encrypted)
			ePassword = pass;
		else {
			
			byte[] plainText = new byte[length];
			for(int i=0;i<pass.length;i++)
				plainText[i]=pass[i];
			for(int i=pass.length;i<length;i++)
				plainText[i] = special[0];
			ePassword = encrypte(plainText);
		}
	}

	public byte[] getEncryptedPass() {

		return ePassword;
	}

	private byte[] encrypte(byte[] plainText) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128);
		SecretKey sk = kg.generateKey();
		key = new SecretKeySpec(sk.getEncoded(), "AES");

		// generate random Initial Vector with length 15
		SecureRandom random = new SecureRandom();
		 ivBytes = new byte[16];
		random.nextBytes(ivBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);	
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");

		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] cipherText = cipher.doFinal(plainText);
		return cipherText;
	}

	private byte[] decrypte() throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
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
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		for(int i=0;i<1000;i+=100)
		{
			String password = "Password is "+i;
			Password pass = new Password(password.getBytes(), false);
			System.out.println("**************************************************");
			System.out.println("Original  = "+password);
			System.out.println("Decrypted = "+new String(pass.decrypte()));
		}
	}

}