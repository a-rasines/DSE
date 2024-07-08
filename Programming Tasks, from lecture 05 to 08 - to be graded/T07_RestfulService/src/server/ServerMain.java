package server;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import server.api.IdentificationHandler;
import server.api.ManageDoodleHandler;
import server.api.ManageMeetingHandler;
import server.api.ManageMeetingPeopleHandler;

public class ServerMain {
	public static void main(String[] args) {
		try {
	        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	        server.createContext("/auth", new IdentificationHandler());
	        server.createContext("/doodle", new ManageDoodleHandler());
	        server.createContext("/meeting/manage", new ManageMeetingHandler());
	        server.createContext("/meeting/people", new ManageMeetingPeopleHandler());
	        server.setExecutor(null); // creates a default executor
	        server.start();
	        System.out.println("Server started at 127.0.0.1:8000");
		}catch(Exception e) {
			System.err.println("Error starting the server: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
