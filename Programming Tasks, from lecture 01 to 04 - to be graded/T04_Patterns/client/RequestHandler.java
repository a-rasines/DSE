package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestHandler {
	
	private static RequestHandler instance;
	public static void setInstance(int port) throws UnknownHostException, IOException {
		instance = new RequestHandler(port);
	}
	
	public static RequestHandler getInstace() {
		return instance;
	}
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private RequestHandler(int port) throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", port);
		out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}
	public String sendMessage(String message) throws IOException {
		System.out.println("[Request Handler] Handling send");
		out.println(message);
		return in.readLine();
	}
	
	public void endConnection() throws IOException {
		out.close();
		in.close();
		socket.close();
	}
}
