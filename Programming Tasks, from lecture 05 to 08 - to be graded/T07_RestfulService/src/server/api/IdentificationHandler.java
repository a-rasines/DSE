package server.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import domain.Doodle;
import server.ServerState;

public class IdentificationHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("Creating token");
		List<Doodle> v = new ArrayList<>();
		String id = Integer.toHexString(v.hashCode());
		ServerState.userList.put(id, v);
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, id.length());
		
		OutputStream os = exchange.getResponseBody();
 		os.write(id.getBytes());
 		os.close();
		
	}

}
