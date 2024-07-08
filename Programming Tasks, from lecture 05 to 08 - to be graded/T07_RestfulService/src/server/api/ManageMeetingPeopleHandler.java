package server.api;

import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import domain.Doodle;
import server.BetterHttpHandler;
import server.ServerState;
import server.marshalling.JSONArray;

public class ManageMeetingPeopleHandler implements BetterHttpHandler{

	@Override
	public void handleGET(HttpExchange exchange) {
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
		for (Doodle d : ServerState.userList.get(userId)) {
			if(d.id.equals(doodle)) {
				if(d.getMeetingTimes().size() <= meeting) {
					throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Meeting not found");
					return;
				} else {
					respond(exchange, new JSONArray(d.getMeetingTimes().get(meeting).users));
				}
			}
		}
		throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Doodle not found or not owned");
	}

	@Override
	public void handlePOST(HttpExchange exchange) {
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
		Doodle d = ServerState.doodleList.get(doodle);
		if(d == null || d.getMeetingTimes().size() <= meeting) {
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Doodle or meeting not found");
		} else if(d.getMeetingTimes().get(meeting).addPerson(userId))
			respond(exchange, "Succesfully added");
		else
			throwError(exchange, HttpURLConnection.HTTP_FORBIDDEN, "Already in the meeting");
		
	}

	@Override
	public void handleDELETE(HttpExchange exchange) {
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
		Doodle d = ServerState.doodleList.get(doodle);
		if(d == null || d.getMeetingTimes().size() <= meeting) {
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Doodle or meeting not found");
		} else if(d.getMeetingTimes().get(meeting).removePerson(userId))
			respond(exchange, "Succesfully removed");
		else
			throwError(exchange, HttpURLConnection.HTTP_FORBIDDEN, "Not in the meeting");
		
	}

}
