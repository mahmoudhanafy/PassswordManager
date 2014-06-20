import java.io.Serializable;


public class Domain  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126057441259313915L;
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
