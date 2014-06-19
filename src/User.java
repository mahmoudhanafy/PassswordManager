import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

	private String userName;
	private char[] masterPassword;
	private HashMap<Domain, Password> map;
	private boolean isLogIn = false;

	public User(String userName, char[] masterPassword) {
		this.userName = userName;
		map = new HashMap<Domain, Password>();
		this.masterPassword = masterPassword;
	}

	public boolean login() {
		// TODO
		return false;
	}

	private void ReadMetaFile(String metaFile) {
		// TODO
	}

	public byte[] getPassword(Domain domain) throws Exception {
		// TODO
		throw new Exception("Unimplemented method");

	}

	public boolean modifyPassword(Domain domain, byte[] oldPass, byte[] newPass)
			throws Exception {
		// TODO
		throw new Exception("Unimplemented method");

	}

	public boolean deleteDomain(Domain domain) throws Exception {
		// TODO
		throw new Exception("Unimplemented method");

	}

	public boolean addDomain(String domain, byte[] pass) throws Exception {
		// TODO
		return false;
	}
}
