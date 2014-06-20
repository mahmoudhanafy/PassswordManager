import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
	private String errorMessage = "User Not Login";
	private String masterPasswordFile = "MasterPass.txt";
	private String mapFile = "DomainMap";
	private String userName;
	private HashMap<Domain, Password> map;
	private boolean isLogIn = false;

	private String salt, encryptedMasterPassword;
	private int noOfIterations;

	/**
	 * if it is the first time to create this user account should enter the
	 * password value else enter any value to password field
	 * 
	 * @param userName
	 * @param password
	 * @param firstTime
	 *            first time to create this User Account
	 */
	public User(String userName, String password, boolean firstTime) {
		this.userName = userName;
		if (firstTime) {
			// create new user
			map = new HashMap<Domain, Password>();
			writeUserPassword(password);
			writeObject(this.userName + mapFile, map);

		} else {
			// load meta data for that user
			ReadMetaFile(this.userName + masterPasswordFile, this.userName
					+ mapFile);
		}
	}

	public boolean login(String masterPassword) {
		return isLogIn = checkPassword(masterPassword);
	}

	private void writeUserPassword(String password) {
		try {
			int iterations = (int) (Math.random() * 10000); // Generate random
															// no of iterations
			String encreptedPassword = generateStorngPasswordHash(iterations,
					getSalt(), password);

			System.out.println("Encrepted Password = " + encreptedPassword);
			System.out.println("Encrepted Password length = "
					+ encreptedPassword.length());
			Domain dom = new Domain("PasswordManager", false, encreptedPassword);
			// write the encrypted password to file
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					userName+masterPasswordFile));
			bw.write(dom.getEncrypted());
			bw.flush();
			bw.close();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException
				| IOException e) {
			e.printStackTrace();
		}

	}

	private boolean checkPassword(String password) {
		try {
			String encryptedPassword = generateStorngPasswordHash(
					noOfIterations, salt, password);
			Domain dom = new Domain("PasswordManager", false, encryptedPassword);
			if (dom.getEncrypted().equals(encryptedMasterPassword)) {
				encryptedPassword = encryptedPassword;
				return true;
			}
			return false;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void ReadMetaFile(String passwordFile, String mapFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(passwordFile));
			String s = br.readLine();
			String[] fields = s.split(":");

			noOfIterations = Integer.parseInt(fields[0]);
			salt = fields[1];
			encryptedMasterPassword = fields[2];

			br.close();

			map = (HashMap<Domain, Password>) readObject(mapFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String generateStorngPasswordHash(int iterations, String saltt,
			String password) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		char[] chars = password.toCharArray();
		byte[] salt = saltt.getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	private String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	public byte[] getPassword(String dom) throws Exception {
		if (!isLogIn)
			throw new Exception(errorMessage);

		Domain domain = new Domain(dom, false, encryptedMasterPassword);
		if (map.containsKey(domain)) {
			// TODO
			return map.get(domain).getPass(adjustKey(encryptedMasterPassword));
		} else {
			return null;
		}
	}

	public boolean modifyPassword(String dom, byte[] oldPass, byte[] newPass)
			throws Exception {
		if (!isLogIn)
			throw new Exception(errorMessage);

		Domain domain = new Domain(dom, false, encryptedMasterPassword);
		if (map.containsKey(domain)) {
			Password pass = map.get(domain);
			// TODO
			if (Arrays.equals(oldPass,
					pass.getPass(encryptedMasterPassword.getBytes()))) {
				map.put(domain, new Password(newPass, false,
						encryptedMasterPassword.getBytes()));
				writeObject(userName + mapFile, map);

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean deleteDomain(String dom) throws Exception {
		if (!isLogIn)
			throw new Exception(errorMessage);

		Domain domain = new Domain(dom, false, encryptedMasterPassword);
		if (map.containsKey(domain)) {
			map.remove(domain);
			writeObject(userName + mapFile, map);

			return true;
		} else {
			return false;
		}
	}

	public boolean addDomain(String domain, byte[] pass) throws Exception {
		if (!isLogIn)
			throw new Exception(errorMessage);

		// TODO
		// KEY = ??
		map.put(new Domain(domain, true, "KEY"), new Password(pass, false,
				encryptedMasterPassword.getBytes()));
		writeObject(userName + mapFile, map);

		return false;
	}

	private void writeObject(String directory, Object map) {
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

	private Object readObject(String directory) throws IOException,
			ClassNotFoundException {
		FileInputStream fin = new FileInputStream(directory);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object map = ois.readObject();

		// TODO Step 7
		ois.close();

		return map;
	}

	private byte[] adjustKey(String key) {
		byte[] bKey = key.getBytes();
		byte[] resKey = new byte[16];
		if (bKey.length > 16) {
			for (int i = 0; i < 16; i++)
				resKey[i] = bKey[i];
		} else {
			for (int i = 0; i < bKey.length; i++)
				resKey[i] = bKey[i];
			for (int i = bKey.length; i < 16; i++)
				resKey[i] = (byte) 0;
		}
		return resKey;
	}

	public static void main(String[] args) {
		User usr = new User("Zonkoly", "012", true);
		User usr1 = new User("Zonkoly", "012", false);
	}
}