package client;

import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import domain.Doodle;
import domain.MeetingTime;
import domain.Request;
import server.marshalling.JSONObject;

public class ClientController {
	/**
	 * A simplified version of an http response and also way to assure you can get either your result or the error message if the server gives one
	 *
	 */
	public static class Response extends RuntimeException {
		private static final long serialVersionUID = 519375302527407530L;
		public final String message;
		public final int status;
		/**
		 * Represents a simplified version of what the server returns, can be thrown
		 * @param status Status of the response, some mapped ones are {@link Response#SUCCESS SUCCESS}, {@link Response#BAD_REQUEST BAD REQUEST}, {@link Response#UNAUTHORIZED UNAUTHORIZED}...
		 * @param message Body of the response or custom message
		 */
		public Response(int status, String message) {
			super(message);
			this.message = message;
			this.status = status;
		}
		/**
		 * Responded when a query gets a response from the server
		 */
		public static final int SUCCESS = 200;
		/**
		 * One or many parameters are missing or have invalid values
		 */
		public static final int BAD_REQUEST = 400;
		/**
		 * The query requires a token and/or the token it's invalid
		 */
		public static final int UNATHORIZED = 401;
		/**
		 * The API doesn't have the method trying to reach or has been moved
		 */
		public static final int METHOD_NOT_ALLOWED = 405;
		/**
		 * The server has an unexpected exception
		 */
		public static final int INTERNAL_SERVER_ERROR = 500;
	}
	/**
	 * Establishes the server connection data
	 * @param sv
	 */
	public static void setServerHandler(ServiceLocator sv) {
		handler = sv;
		try {
			Auth.identify();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	private static String token = null;
	private static ServiceLocator handler;
	
	public static class Auth {
		private static final String PATH = "auth";
		/**
		 * Register a new user
		 * @return A simplified response, giving in the status the result of the exchange and in the body the error in case of one
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
		public static Response identify() throws InterruptedException, ExecutionException{
			try {
				HttpResponse<String> response = handler.sendGET(PATH);
				if(response.statusCode() != Response.SUCCESS)
					return new Response(response.statusCode(), response.body());
				else {
					token = response.body();
					return new Response(Response.SUCCESS, "");
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	public static class Doodles {
		private static final String PATH = "doodle";
		public static List<Doodle> getDoodles() {
			try {
				HttpResponse<String> response = handler.sendGET(PATH,Map.of("user_id", token));
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return null;
				} else {
					List<Doodle> d = new LinkedList<>();
					JSONArray heHadToBeSpecialAndNotUseIterablesAsTheRest = new JSONArray(response.body());
					for (int i = 0; i < heHadToBeSpecialAndNotUseIterablesAsTheRest.length(); i++)
						d.add(Doodle.fromJSON(heHadToBeSpecialAndNotUseIterablesAsTheRest.getJSONObject(i)));
					return d;
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException | JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static Doodle getDoodle(String id) {
			try {
				HttpResponse<String> response = handler.sendGET(PATH+"/"+id);
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return null;
				} else {
					return Doodle.fromJSON(response.body());
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static Doodle createDoodle(Doodle orig) {
			try {
				HttpResponse<String> response = handler.sendPOST(PATH, Map.of("user_id", token),JSONObject.from(new Request<Doodle>(token, orig)));
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return null;
				} else {
					return Doodle.fromJSON(response.body());
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static Response deleteDoodle(String id) {
			try {
				HttpResponse<String> response = handler.sendDELETE(PATH, Map.of("user_id", token, "doodle_id", id));
				return new Response(response.statusCode(), response.body());
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class Meeting {
		private static final String PATH = "meeting/manage";
		
		public static MeetingTime createMeeting(MeetingTime mt) {
			try {
				HttpResponse<String> response = handler.sendPOST(PATH, JSONObject.from(new Request<>(token, mt)));
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return null;
				} else {
					return MeetingTime.fromJSON(response.body());
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static boolean deleteMeeting(String doodle, Integer meeting) {
			try {
			HttpResponse<String> response = handler.sendDELETE(PATH, Map.of("user_id", token, "doodle_id", doodle, "meeting_id", meeting.toString()));
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return false;
				} else {
					return true;
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	public static class MeetingPeople {
		private static final String PATH = "meeting/people";
		
		public static boolean register(String doodle, Integer meeting) {
			try {
				HttpResponse<String> response = handler.sendPOST(PATH, Map.of("user_id", token, "doodle_id", doodle, "meeting_id", meeting.toString()), "".getBytes());
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return false;
				} else {
					return true;
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public static boolean deregister(String doodle, Integer meeting) {
			try {
				HttpResponse<String> response = handler.sendDELETE(PATH, Map.of("user_id", token, "doodle_id", doodle, "meeting_id", meeting.toString()));
				if(response.statusCode() != Response.SUCCESS) {
					System.err.println(response.body());
					return false;
				} else {
					return true;
				}
			} catch (URISyntaxException | InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
