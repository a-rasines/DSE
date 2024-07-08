package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Doodle;
import domain.MeetingTime;

public class ServerState {
	public static Map<String, Doodle> doodleList = new HashMap<>();
	
	public static Map<String, List<Doodle>> userList = new HashMap<>();
	
	static {
		doodleList.put(
				"test", 
				new Doodle("test")
					.addMeeting(new MeetingTime("test", 12, 0, 15, 0, 1, 1, 2023))
					.addMeeting(new MeetingTime("test", 16, 50, 18, 0, 2, 1, 2023))
		);
	}
	
	public static boolean userExists(String user) {
		return userList.containsKey(user);
	}
	
	public static void registerDoodle(String owner, Doodle doodle) {
		doodleList.put(doodle.id, doodle);
		userList.get(owner).add(doodle);
	}
}
