import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Domain implements Serializable , Comparable<Domain>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126057441259313915L;
	String eDomain;
	private static final String algorithm = "HMac-SHA256";

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

	
	public byte[] getTag()
	{
		byte[] tag = new byte[Password.tagLength];
		if(eDomain.length()>Password.tagLength)
		{
			tag = eDomain.substring(0, Password.tagLength).getBytes();
			return tag;
		}
		if(eDomain.length()==Password.tagLength)
			return eDomain.getBytes();
		byte[] domain = eDomain.getBytes();
		for(int i=0;i<domain.length;i++)
		{
			tag[i]=domain[i];
		}
		for(int i=domain.length;i<Password.tagLength;i++)
			tag[i]=(byte)0;
		
		return tag;
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
		return decrypte(eDomain, key);
		// return null;
	}

	private String encrypte(String plainText, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {

		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
		Mac mac = Mac.getInstance(algorithm);
		mac.init(signingKey);
		
		return toHexString(mac.doFinal(plainText.getBytes()));
	}

	private String decrypte(String cipherText, String key)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] plaintext = new byte[0];
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
		
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, signingKey);
		plaintext = cipher.doFinal(cipherText.getBytes());
		
		return new String(plaintext);
	}

	private String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();

		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		String res = formatter.toString();
		formatter.close();
		return res;
	}
	

	@Override
	public int compareTo(Domain o) {
		// TODO Auto-generated method stub
		return eDomain.compareTo(o.eDomain);
	}

}
