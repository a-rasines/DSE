package server.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import domain.Doodle;
import domain.MeetingTime;
import domain.Request;
import server.BetterHttpHandler;
import server.ServerState;
import server.marshalling.JSONObject;

public class ManageMeetingHandler implements BetterHttpHandler{

	@Override
	public void handleGET(HttpExchange exchange) {
		String[] params = exchange.getRequestURI().getPath().split("/");
		if(params.length < 4) {
			throwError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Missing parameters");
		}
		String doodle = params[2];
		int meeting;
		try {
			meeting = Integer.parseInt(params[3]);
		} catch(NumberFormatException e) {
			throwError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Invalid meeting id");
			return;
		}
		Doodle d = ServerState.doodleList.get(doodle);
		if(d == null) {
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid doodle id");
			return;
		} if(d.getMeetingTimes().size() >= meeting) {
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid meeting id");
		}
		respond(exchange, JSONObject.from(d.getMeetingTimes().get(meeting)));
		
		
	}

	@Override
	public void handlePOST(HttpExchange exchange) {
		System.out.println("--- Add Meeting Time ---");
		try {
			Request<MeetingTime> r = Request.fromJSON(MeetingTime.class, new String(exchange.getRequestBody().readAllBytes()));
			if(!ServerState.userExists(r.id)) {
				throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid user");
				return;
			}
			
			MeetingTime mt = r.body;
			if(!mt.verify()) {
				throwError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Invalid meeting data");
				return;
			}
			List<Doodle> doodles = ServerState.userList.get(r.id);
			for(Doodle doodle : doodles)
				if(doodle.id.equals(mt.doodleId)) {
					doodle.addMeeting(mt);
					respond(exchange, JSONObject.from(mt)); //Returns the meeting time for update on client
					return;
				}
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Doodle does not exist or not owned");
		} catch (IOException e) {
			e.printStackTrace();
			throwError(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Something went wrong");
		}
		
	}

	@Override
	public void handleDELETE(HttpExchange exchange) {
		System.out.println("--- Remove Meeting Time ---");
		String userId;
		try {
			userId = exchange.getRequestHeaders().get("user_id").get(0);
			if(!ServerState.userExists(userId)) {
				throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid user");
				return;
			}
		} catch(IndexOutOfBoundsException | NullPointerException e) {
			throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "User needed");
			return;
		}
		
		int meeting;
		String doodle;
		try {
			meeting = Integer.parseInt(exchange.getRequestHeaders().get("meeting_id").get(0));
			doodle = exchange.getRequestHeaders().get("doodle_id").get(0);
		} catch(NumberFormatException e) {
			throwError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Invalid meeting id");
			return;
		} catch(NullPointerException | IndexOutOfBoundsException e) {
			throwError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Insuficient Data");
			return;
		}
		
		List<Doodle> doodles = ServerState.userList.get(userId);
		for(Doodle d : doodles)
			if(d.id.equals(doodle))
				if(d.getMeetingTimes().size() <= meeting) {
					throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Meeting not found");
					return;
				} else {
					d.removeMeeting(meeting);
					respond(exchange, "Deleted");
				}
		
	}

}
