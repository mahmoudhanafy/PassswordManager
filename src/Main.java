import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Master UserName :");
		String userName = br.readLine();

		System.out.println("Enter Master Password : ");
		String masterPassword = br.readLine();

		User user = new User(userName, masterPassword, true);
		System.out.println(user.login(masterPassword));
		
		while (true) {
			System.out.println("Enter :");
			System.out.println("1) Get Domain Password");
			System.out.println("2) Modify Domain Password");
			System.out.println("3) Add New Domain Password");
			System.out.println("4) Delete Domain");

			int k = Integer.parseInt(br.readLine());

			System.out.println("Enter Domain Name :");
			String domainName = br.readLine();
			
			switch (k) {
			case 1:
				byte[] password = user.getPassword(domainName);
				System.out.println("Password is : " + new String(password));
				
				break;
			case 2:
				System.out.println("Enter Old Password :");
				String oldPassword = br.readLine();
				
				System.out.println("Enter New Password :");
				String newPassword = br.readLine();
				
				boolean success = user.modifyPassword(domainName, oldPassword.getBytes(), newPassword.getBytes());
				System.out.println(success ? "Password Changed Successfully" : "Can't Change Password");
				
				break;
			case 3:
				System.out.println("Enter Domain Password");
				String nPassword = br.readLine();
				
				boolean added = user.addDomain(domainName, nPassword.getBytes());
				System.out.println(added ? "Domain Added Successfully" : "Can't Add this Domain");
				break;
			case 4:
				boolean delete = user.deleteDomain(domainName);
				System.out.println(delete ? "Domain Deleted Successfully" : "Can't Delete this Domain");
				
				break;
			}
		}
	}
}