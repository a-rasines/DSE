package imported;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogEntry implements Serializable{
	private static final long serialVersionUID = -3311641501412113769L;
	private final String logEntry;
	private static final List<LogEntry> LOGS = new LinkedList<>();
	
	public static void addLog(LogEntry log) {
		List<LogEntry> temp = Collections.synchronizedList(LOGS);
		temp.add(log);
		System.out.println("log  {" + log + "} stored, "+temp.size()+" logs stored");
	}
	
	
	public static void removeLogs(int q) {
		List<LogEntry> sync = Collections.synchronizedList(LOGS);
		if(q >= sync.size()) {
			sync.clear();
			System.out.println("All logs removed. " + sync.size() + " logs left");
		} else {
			List<LogEntry> temp = getLogs().subList(q, LOGS.size());
			sync.clear();
			sync.addAll(temp);
			System.out.println("" + q + " logs removed. " + sync.size() + " logs left");
		}
	}
	
	public static List<LogEntry> getLogs() {
		return new ArrayList<LogEntry>(LOGS);//LinkedList is better to add values while ArrayList is better to iterate
	}
	
	@Override
	public String toString() {
		return "logEntry=" + logEntry;
	}

	public LogEntry(String logEntry) {
		super();
		assert (logEntry != null);
		this.logEntry = logEntry;
	}

	public String getLogEntry() {
		return logEntry;
	}

	public boolean matchesSearchTerm(String searchTerm) {
		return logEntry.contains(searchTerm);
	}
}