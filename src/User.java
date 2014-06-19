import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

	private String userName;

	private HashMap<Domain, Password> map;

	
	public User(String userName, String metaFile) {
		this.userName=userName;
		ReadMetaFile(metaFile);
		map = new HashMap<Domain, Password>();
	}

	
	private void ReadMetaFile(String metaFile)
	{
		//TODO
	}
	public byte[] getPassword(Domain domain) throws Exception {
		//TODO
		throw new Exception("Unimplemented method");

	}

	public boolean modifyPassword(Domain domain, byte[] oldPass, byte[] newPass) throws Exception {
		//TODO
		throw new Exception("Unimplemented method");

	}
	
	public boolean deleteDomain(Domain domain) throws Exception
	{
		//TODO
		throw new Exception("Unimplemented method");

	}
	class Cell {
		private Domain domain;
		private Password pass;
		
		public Cell(Domain domain, Password pass) {
			// TODO Auto-generated constructor stub
			this.domain=domain;
			this.pass = pass;
		}
	}
}
