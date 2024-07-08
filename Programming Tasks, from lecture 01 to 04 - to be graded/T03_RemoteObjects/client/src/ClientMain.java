import remote.ServiceLocator;
import ui.MainWindow;

public class ClientMain {
	public static void main(String[] args) {
		System.setProperty("java.security.policy","security/java.policy");
		ServiceLocator.changeInstance("127.0.0.1", "1099", "Server");
		new MainWindow().setVisible(true);
	}
}
