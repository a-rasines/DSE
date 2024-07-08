import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerMain extends Thread{
		
		public static void main(String[] args) {
			List<ServerMain> sockets = new ArrayList<>(4);
			for (int i = 6600; i < 6604; i++) {
				ServerMain s = new ServerMain(i);
				s.start();
				sockets.add(s);
			}
		}
		
		private int port;
		public ServerMain(int port) {
			this.port = port;
		}
		
	    public void run() {
			try (ServerSocket serverSocket = new ServerSocket(port);
				 Socket clientSocket = serverSocket.accept();
				 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
		        
		        System.out.println("Started server on port "+port);
		        while(true) {
		        	String[] params;
		        	try {
		        		params = in.readLine().split(";");
		        	} catch(NullPointerException e) {
		        		return;
		        	}
			        try {
			        	int number = Integer.parseInt(params[0]);
			        	System.out.println("["+port+"] "+params[1]+" sent "+number+" on " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(Long.parseLong(params[2]))));
			        	out.println(++number);
			        } catch(NumberFormatException e) {
		        		System.out.println("["+port+"] Received "+params[0].length()/1000+"MiB load, returning to sender");
		        		out.println(params[0]);
			        	
			        }
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
}
