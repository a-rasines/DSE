import java.rmi.registry.LocateRegistry;

import domain.BankAccount;
import remote.RemoteFacade;

public class ServerMain {
	
	public static void main(String[] args) {
		for(int x = 0; x < 10; x++)
			BankAccount.addAccount(new BankAccount(10000));
		System.setProperty("java.security.policy","security/java.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(1099).rebind("Server", new RemoteFacade());
			System.out.println("Server started on port 1099");
		} catch (Exception e) {
			System.err.println("Couldn't start server on port 1099");
			e.printStackTrace();
		}
	}
}
