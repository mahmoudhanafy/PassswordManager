import java.io.Serializable;


public class Password implements Serializable {

	private byte[] ePassword ;
	
	
	public Password(byte[] pass , boolean encrypted){
		// TODO Auto-generated constructor stub
		ePassword = pass;
	}
	
	public byte[] getEncryptedPass()
	{
		//TODO
		return null;
	}
	
	
	public byte[] getPass()
	{
		//TODO
		return null;
	}
	
	
	
}
