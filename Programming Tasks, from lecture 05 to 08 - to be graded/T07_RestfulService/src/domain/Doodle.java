package domain;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Doodle {
	
	public static Doodle fromJSON(String json) {
		try {
			return fromJSON(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static Doodle fromJSON(JSONObject json) {
		Doodle result;
		try {
			result = new Doodle(json.getString("id"));
			if(json.has("meetingTimes")) {
				JSONArray iHateTheDeveloperOfThatLibrary = json.getJSONArray("meetingTimes");
				for(int i = 0; i < iHateTheDeveloperOfThatLibrary.length(); i++)
					result.addMeeting(MeetingTime.fromJSON(iHateTheDeveloperOfThatLibrary.getJSONObject(i)));
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private List<MeetingTime> meetingTimes = new LinkedList<>();
	public transient int currentId = 0;
	public String id;
	
	public Doodle(String id) {
		this.id = id;
	}
	
	public Doodle addMeeting(MeetingTime mt) {
		mt.doodleId = this.id;
		mt.id = currentId++;
		meetingTimes.add(mt);
		return this;
	}
	
	public void clearMeetingTimes() {
		this.meetingTimes.clear();
		this.currentId = 0;
	}
	
	public List<MeetingTime> getMeetingTimes() {
		return this.meetingTimes;
	}
	
	@Override
	public String toString() {
		return "Doodle [meetingTimes=" + meetingTimes + ", id=" + id + "]";
	}

	public void removeMeeting(int meeting) {
		currentId = 0;
		meetingTimes.remove(meeting);
		meetingTimes.forEach(v -> v.id = currentId++);
		
	}

}
