package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.List;

import imported.IMarshall;
import imported.LogEntry;
import imported.RequestMessage;
import imported.ResponseMessage;

public class RequestHandler {
	
	@SuppressWarnings("unchecked")
	public static void handleRequest(Socket clientSocket) {
		@SuppressWarnings("rawtypes")
		RequestMessage rm;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)){
			while(!clientSocket.isClosed()) {
				System.out.print("Request received for ");
				rm = IMarshall.unmarshall(Base64.getDecoder().decode(in.readLine()));
				System.out.println(rm.getMethod());
				switch (rm.getMethod()) {
				
					case singleLog: //Fire and forget
						
						Invoker.log((LogEntry)rm.getRequestData()); 
						
						break;
						
					case removeOldLogs://Sync with server
						
						System.out.println("remove "+rm.getRequestData()+" logs command received");
						try {
							out.println(new String(Base64.getEncoder().encode(new ResponseMessage<String>("200 OK").marshall())));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
						
						Invoker.clear((Integer)rm.getRequestData()); //Fulfill the request
						break;
					
					case addLogsInBulk: //Result Callback
						System.out.println("add " + ((List<LogEntry>)rm.getRequestData()).size() + " logs command received");
						Invoker.bulk(out, (List<LogEntry>)rm.getRequestData());
						break;
						
					case increaseStorageSpace: //Result Callback
						Invoker.reserveMemory(out, (Integer)rm.getRequestData());
						break;
						
					case searchLogs: //Polling
						
						try {
							out.println(new String(Base64.getEncoder().encode(Invoker.search((String)rm.getRequestData()).marshall())));
						} catch (Exception e) {
							e.printStackTrace();
						} //Get the result and send it
						
						break;
					default:
						//try {	clientSocket.close();	} catch (IOException e) {e.printStackTrace();}
				}
			}
		} catch (ClassNotFoundException | IOException e) {e.printStackTrace();return;}
		
	}
	
	
}
