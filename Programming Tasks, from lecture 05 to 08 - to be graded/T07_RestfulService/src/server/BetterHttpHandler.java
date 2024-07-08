package server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import server.marshalling.JSONArray;
import server.marshalling.JSONObject;

public interface BetterHttpHandler extends HttpHandler {

	@Override
	public default void handle(HttpExchange exchange) throws IOException {
		try {
			this.getClass()
				.getMethod("handle"+exchange.getRequestMethod(), HttpExchange.class)
				.invoke(this, exchange);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			throwError(exchange, HttpURLConnection.HTTP_NOT_IMPLEMENTED, "This method is not supported");
		}
		// TODO Auto-generated method stub
		
	}
	
	public default void throwError(HttpExchange exchange, int code, String reason) {
		try {
			exchange.sendResponseHeaders(code, reason.length());
			OutputStream os = exchange.getResponseBody();
	 		os.write(reason.getBytes());
	 		os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public default void respond(HttpExchange exchange, String content) {
		throwError(exchange, HttpURLConnection.HTTP_OK, content);
	}
	
	public default void respond(HttpExchange exchange, JSONObject jsonObject) {
		exchange.getResponseHeaders().set("Content-Type", "application/json");
		throwError(exchange, HttpURLConnection.HTTP_OK, jsonObject.toString());
	}
	
	public default void respond(HttpExchange exchange, JSONArray jsonObject) {
		exchange.getResponseHeaders().set("Content-Type", "application/json");
		throwError(exchange, HttpURLConnection.HTTP_OK, jsonObject.toString());
	}
	
	public void handleGET(HttpExchange exchange);
	public void handlePOST(HttpExchange exchange);
	public void handleDELETE(HttpExchange exchange);

}
