package server.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import imported.LogEntry;

public class BulkLogManager {
	public static BlockingQueue<List<LogEntry>> reservedMemory = new LinkedBlockingQueue<List<LogEntry>>();
	
	public static void reserveMemory(int q) {
		reservedMemory.add(new ArrayList<>(q));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
