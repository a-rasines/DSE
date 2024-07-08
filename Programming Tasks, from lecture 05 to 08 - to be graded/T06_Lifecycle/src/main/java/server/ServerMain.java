package server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerMain {
	public static void main(String[] args) {
		Server server = ServerBuilder.forPort(8080)
                .addService(new CipherServiceImpl())
                .build();

        try {
			server.start();
			System.out.println("Server started in port 8080");
			server.awaitTermination();
        } catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
