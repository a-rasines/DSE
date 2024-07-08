package server.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import domain.Doodle;
import domain.MeetingTime;
import domain.Request;
import server.BetterHttpHandler;
import server.ServerState;
import server.marshalling.JSONArray;
import server.marshalling.JSONObject;

public class ManageDoodleHandler implements BetterHttpHandler {

	@Override
	public void handleGET(HttpExchange exchange) {
		System.out.println("--- GET DOODLE ---");
		String[] params = exchange.getRequestURI().getPath().split("/");
		if(params.length == 2) {
			try {
				String userId = exchange.getRequestHeaders().get("user_id").get(0);
				if(!ServerState.userExists(userId)) {
					throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid user");
				}
				else {
					respond(exchange, new JSONArray(ServerState.userList.get(userId)));
				}
				return;
			} catch(IndexOutOfBoundsException | NullPointerException e) {
				throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "User needed");
				return;
			}
		}
		String id = params[2];
		Doodle d = ServerState.doodleList.get(id);
		if(d == null) {
			throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "No doodle with that id");
			return;
		}
		
		respond(exchange, JSONObject.from(d));
		
	}

	@Override
	public void handlePOST(HttpExchange exchange) {
		System.out.println("--- CREATE DOODLE ---");
		try {
			Request<Doodle> r = Request.fromJSON(Doodle.class, new String(exchange.getRequestBody().readAllBytes()));
			if(!ServerState.userExists(r.id)) {
				throwError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid user");
				return;
			}
			
			Doodle doodle = r.body;
			doodle.id = Integer.toHexString(doodle.hashCode());
			List<MeetingTime> mts = new ArrayList<>(doodle.getMeetingTimes());
			doodle.clearMeetingTimes();
			for(MeetingTime mt : mts) {
				if(mt.verify()) //Remove all absurd times (start after finish and out-of-bounds numbers)
					doodle.addMeeting(mt);//Fix meeting ids in the proccess
			}
			ServerState.registerDoodle(r.id, doodle);
			respond(exchange, JSONObject.from(doodle)); //Returns the doodle for update on client
		} catch (IOException e) {
			e.printStackTrace();
			throwError(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Something went wrong");
		}
		
	}

	@Override
	public void handleDELETE(HttpExchange exchange) {
		System.out.println("--- DELETE DOODLE ---");
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
		String doodleId = exchange.getRequestHeaders().get("doodle_id").get(0);
		List<Doodle> dl = ServerState.userList.get(userId);
		List<Doodle> dlCopy = new ArrayList<>(dl);
		for (Doodle d : dlCopy)
			if(d.id.equals(doodleId)) {
				dl.remove(d);
				ServerState.doodleList.remove(doodleId);
				respond(exchange, "Deleted");
				return;
			}
		throwError(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Doodle not found or not owned");
		
	}

}
