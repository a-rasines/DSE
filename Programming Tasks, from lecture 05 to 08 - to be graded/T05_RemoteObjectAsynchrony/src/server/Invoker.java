package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import imported.IMarshall;
import imported.LogEntry;
import imported.RequestMessage;
import imported.ResponseMessage;
import server.domain.BulkLogManager;

public class Invoker {
	
	public static void log(LogEntry log) {
		LogEntry.addLog(log);
	}
	
	public static void clear(Integer q) {
		LogEntry.removeLogs(q);
	}
	
	public static void reserveMemory(PrintWriter out, int q) {
		try {
			System.out.println("Bulk add command received, preparing server");
			BulkLogManager.reserveMemory(q);
			System.out.println("Server ready, notifying");
			out.println(new String(Base64.getEncoder().encode(new ResponseMessage<String>("100 Continue").marshall()))); //Warn the client that the server's ready for receiving the payload
			System.out.println("Notified");
		} catch(Exception e) {
			e.printStackTrace();
			try {	out.println(new String(Base64.getEncoder().encode(new ResponseMessage<String>("400 Bad Request").marshall())));	} catch (Exception e1) {e1.printStackTrace();}
		}
	}
	public static void bulk(PrintWriter out, List<LogEntry> data) {
		try {
			System.out.println("Bulk logs received");
			System.out.println("Logs received:");
			for(LogEntry entry : data) {//Registers each log
				System.out.println(entry.getLogEntry());
				LogEntry.addLog(entry);
			}
			System.out.println("---End of logs---");
			out.println(new String(Base64.getEncoder().encode(new ResponseMessage<String>("200 OK").marshall())));//Notifies the client
			
		} catch(Exception e) {
			e.printStackTrace();
			try {	out.println(new String(Base64.getEncoder().encode(new ResponseMessage<String>("400 Bad Request").marshall())));	} catch (Exception e1) {e1.printStackTrace();}
		}
	}
	
	public static ResponseMessage<List<LogEntry>> search(String term) {
		List<LogEntry> results = new LinkedList<>();
		System.out.println("Search command received. term="+term);
		for(LogEntry le : LogEntry.getLogs())
			if(le.matchesSearchTerm(term))
				results.add(le);
		System.out.println(""+results.size()+" logs founds. Retrieving");
		return new ResponseMessage<List<LogEntry>>(results);
		
	}
}
