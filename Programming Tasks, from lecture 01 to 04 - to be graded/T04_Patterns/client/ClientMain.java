package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain{

	public static void main(String[] args) throws IOException {
		RequestHandler.setInstance(6600);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Select your name: ");
		String name = reader.readLine();
		while(true) {
			System.out.print(
				"Select a function to use:\n"+
				"1. Hello\n"+
				"2. Goodbye\n"+
				"3. Non existing function\n"+
				"Type the number here: "
			);
			switch(reader.readLine()) {
				case "1":
					System.out.println(Proxy.hello(name));
					break;
				case "2":
					System.out.println(Proxy.goodbye(name));
					break;
				case "3":
					System.out.println(Proxy.teapot(name));
				default:
					System.out.println("Please, select a valid option");
			}
		}
	}
}
