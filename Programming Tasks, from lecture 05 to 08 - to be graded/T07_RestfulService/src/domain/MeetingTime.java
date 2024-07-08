package domain;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class MeetingTime {
	
	public static MeetingTime fromJSON(String s) {
		try {
			return fromJSON(new JSONObject(s));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	@Override
	public String toString() {
		return "MeetingTime [id=" + id + ", doodleId=" + doodleId + ", startingHour=" + startingHour
				+ ", startingMinute=" + startingMinute + ", finishHour=" + finishHour + ", finishMinute=" + finishMinute
				+ ", day=" + day + ", month=" + month + ", year=" + year + ", userCount=" + userCount + "]";
	}
	public static MeetingTime fromJSON(JSONObject obj) {
		try {
			MeetingTime mt = new MeetingTime(
								obj.getString("doodleId"),
								obj.getInt("startingHour"), 
								obj.getInt("startingMinute"), 
								obj.getInt("finishHour"), 
								obj.getInt("finishMinute"),
								obj.getInt("day"),
								obj.getInt("month"),
								obj.getInt("year"));
			if(obj.has("userCount"))
				mt.userCount = obj.getInt("userCount");
			return mt;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public int id;
	public String doodleId;
	public int startingHour;
	public int startingMinute;
	public int finishHour;
	public int finishMinute;
	public int day;
	public int month;
	public int year;
	public final transient Set<String> users = new HashSet<>();
	public int userCount = 0;
	public MeetingTime(String doodleId, int startingHour, int startingMinute, int finishHour, int finishMinute, int day, int month, int year) {
		super();
		this.doodleId = doodleId;
		this.startingHour = startingHour;
		this.startingMinute = startingMinute;
		this.finishHour = finishHour;
		this.finishMinute = finishMinute;
		this.day = day;
		this.month = month;
		this.year = year;
	}
	public MeetingTime(int id, String doodleId, int startingHour, int startingMinute, int finishHour, int finishMinute, int day, int month, int year) {
		super();
		this.doodleId = doodleId;
		this.startingHour = startingHour;
		this.startingMinute = startingMinute;
		this.finishHour = finishHour;
		this.finishMinute = finishMinute;
		this.day = day;
		this.month = month;
		this.year = year;
	}
	private boolean vt(int h, int m) {
		return h >= 0 && h <= m;
	}
	public boolean addPerson(String id) {
		boolean b = this.users.add(id);
		if(b) this.userCount++;
		return b;
	}
	public boolean removePerson(String id) {
		boolean b = this.users.remove(id);
		if(b) this.userCount--;
		return b;
	}
	
	public boolean verify() {
		return vt(startingHour, 23) && 
			   vt(finishHour, 23) && 
			   vt(startingMinute, 59) && 
			   vt(finishMinute, 59) &&
			   vt(day, 31) &&
			   vt(month, 12) &&
			   (startingHour * 60 + startingMinute) < (finishHour * 60 + finishMinute);
	}
	
}
