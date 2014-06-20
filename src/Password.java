import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

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
	public static final int length = 32, tagLength = 32;
	private byte[] ePassword, ivBytes;
	private static final byte[] special = "$".getBytes();

	public Password(byte[] pass, boolean encrypted, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		// TODO Auto-generated constructor stub

		if (encrypted)
			ePassword = pass;
		else {

			byte[] plainText = new byte[length+tagLength];
			for (int i = 0; i < pass.length; i++)
				plainText[i] = pass[i];
			for (int i = pass.length; i < length+tagLength; i++)
				plainText[i] = special[0];
			ePassword = encrypte(plainText, key);
		}
	}

	public byte[] getEncryptedPass() {

		return ePassword;
	}

	private byte[] encrypte(byte[] plainText, byte[] KEY)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {

		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128);
		SecretKeySpec key = new SecretKeySpec(KEY, "AES");

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

	private byte[] decrypte(byte[] KEY) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec key = new SecretKeySpec(KEY, "AES");

		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] plainText = cipher.doFinal(ePassword);
		return plainText;
	}

	public byte[] getPass(byte[] key) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// TODO

		byte[] decrypte = decrypte(key);
		byte[] password =Arrays.copyOfRange(decrypte, tagLength, decrypte.length);
		return password;
	}
	public byte[] getTag(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		byte[] decrypte = decrypte(key);
		byte[] tag =Arrays.copyOfRange(decrypte, 0,tagLength);
		return tag;
	}

	private static void writeObject(String directory, Object map) {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(directory);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(map);

			oos.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Object readObject(String directory) throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(directory);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object map = ois.readObject();
		ois.close();

		return map;
	}


}