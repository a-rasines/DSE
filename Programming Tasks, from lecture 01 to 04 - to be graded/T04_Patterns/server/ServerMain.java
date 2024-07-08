package server;

import java.io.IOException;

public class ServerMain{
		
		public static void main(String[] args) {
			try {
				new RequestHandler(6600);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
