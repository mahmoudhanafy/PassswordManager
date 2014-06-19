import java.io.Serializable;


public class Domain  implements Serializable{

	String eDomain;
	
	public Domain(String domain, boolean encrypted) {
		// TODO Auto-generated constructor stub
		eDomain = domain;	
	}
	
	public boolean authenticate()
	{
		//TODO
		return false;
	}
	
	public String getEncrypted()
	{
		return eDomain;
	}
	
	public String getDomain()
	{
		//TODO
		return null;
	}
	
	private String encrypte()
	{
		//TODO
		return null;
	}
}
