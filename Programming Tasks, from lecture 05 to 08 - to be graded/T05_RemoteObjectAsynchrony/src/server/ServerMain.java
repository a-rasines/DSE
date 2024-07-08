package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain{
		
		public static void main(String[] args) {
			
			ExecutorService executorService = Executors.newCachedThreadPool();
	        try (ServerSocket serverSocket = new ServerSocket(6600);) {
	            System.out.println("Server listening on port 6600");

	            while (true) {
	                Socket clientSocket = serverSocket.accept();
	                System.out.println("Socket accepted");
	                executorService.submit(()->RequestHandler.handleRequest(clientSocket));
	                
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	        	executorService.shutdown();
	        }
		}
}
