package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imported.KnownMethods;
import imported.LogEntry;
import imported.ResponseMessage;

public class Proxy {
	
	public static void addLog(String log) {
		try {
			Requestor.generateRequest(KnownMethods.singleLog, new LogEntry(log), Void.class).send(); //Server won't acknowledge or respond (Fire and Forget)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearLogs(Integer q) {
		try {
			if(Requestor.generateRequest(KnownMethods.removeOldLogs, q, String.class).get().getResponseData().startsWith("200"))
				System.out.println("Logs deleted correctly");
			else
				System.out.println("Error deleting logs");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A little control unit to send the logs whenever both systems are ready to handle the logs
	 */
	private static class BulkControlUnit {
		
		private boolean isServerReady = false;
		private boolean isClientReady = false;
		private boolean isExecuted = false;
		
		/**
		 * Changes the corresponding flag, basically a synchronized setter of both flags so they can't change at the same time and lock in place
		 * @param client whether the flag to change is the client's one (true) or the server's one (false)
		 * @return true if both flags have been triggered
		 */
		public synchronized boolean switchFlag(boolean client) {
			if(client)
				isClientReady = true;
			else
				isServerReady = true;
			
			return isClientReady && isServerReady;
		}
		
		/**
		 * This function sends the logs to the server and prints the response sent asynchronously
		 * 
		 * The function is synchronized in case both the client and server finish at the same exact time
		 * @param l
		 */
		public synchronized void sendLogs(List<LogEntry> l) {
			if(isExecuted) return;
			else isExecuted = true;
			try {
				Requestor.generateRequest(KnownMethods.addLogsInBulk, l, String.class).then((v)->System.out.print("Bulk add returned a response: " + v.getResponseData())).queue();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void bulkAdd(List<String> logs) {
		List<LogEntry> l = new ArrayList<LogEntry>(logs.size());
		logs.forEach(v -> l.add(new LogEntry(v)));
		final BulkControlUnit cu = new BulkControlUnit();
		new Thread(() -> {
			try {
				ResponseMessage<String> rm = Requestor.generateRequest(KnownMethods.increaseStorageSpace, logs.size(), String.class).get();
				if(rm.getResponseData().startsWith("100")) {
					if(cu.switchFlag(false))
						cu.sendLogs(l);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start(); //Prepare server 
		
		try {
			Thread.sleep(l.size() * 100); //Prepare client
			if (cu.switchFlag(true))
				cu.sendLogs(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void searchLogs(String term) {
		try {
			ResponseMessage<?> rm = Requestor.generateRequest(KnownMethods.searchLogs, term, Object.class).get();
			System.out.println("\n".repeat(10)+"Retrieved logs:");
			for(LogEntry log : (List<LogEntry>)rm.getResponseData())
				System.out.println(log.getLogEntry());
			System.out.println("---------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
