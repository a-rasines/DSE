package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import domain.EncriptionPair;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import server.CipherGrpc;
import server.CipherGrpc.CipherBlockingStub;
import server.CipherServiceProto;
import server.CipherServiceProto.ClientData;
import server.CipherServiceProto.StringRequest;

public class ClientMain {
	
	 public static void main(String[] args) throws IOException {
	        // Replace "localhost" and 8080 with your gRPC server's address and port
	        String serverAddress = "localhost";
	        int serverPort = 8080;
	        // Create a channel to connect to the gRPC server
	        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
	                .usePlaintext() // For simplicity, use plaintext communication. In production, consider using TLS.
	                .build();

	        try {
	            // Create a gRPC client stub
	            CipherBlockingStub blockingStub = CipherGrpc.newBlockingStub(channel);
	            ClientData response = blockingStub.startConnection(CipherServiceProto.EmptyMessage.newBuilder().build());
	            EncriptionPair pair = EncriptionPair.fromKeyStrings(Base64.getDecoder().decode(response.getPublicKey()), Base64.getDecoder().decode(response.getPrivateKey()));
	            String id = response.getId();
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	            while (true) {
	            	System.out.println("Select an option:\n"
	            					 + "1. Send message (Pooling)\n"
	            					 + "2. Send message (Per-Request)\n"
	            					 + "3. Exit\n");
	            	String name = reader.readLine();
	            	System.out.println("\n".repeat(10));
	            	if(name.equals("3"))
	            		break;
	            	switch(name) {
	            		case "1":
	            			System.out.println("Write the message to send: ");
	            			System.out.println(pair.decriptor.decrypt(
	            				blockingStub.sendMessagePool(
	            					StringRequest.newBuilder()
	            								 .setId(id)
	            								 .setMessage(pair.encriptor.encrypt(reader.readLine()))
	            								 .build()
	            				).getResponse()
	            			));
	            			break;
	            		case "2":
	            			System.out.println("Write the message to send: ");
	            			System.out.println(pair.decriptor.decrypt(
	            				blockingStub.sendMessageReq(
	            					StringRequest.newBuilder()
	            								 .setId(id)
	            								 .setMessage(pair.encriptor.encrypt(reader.readLine()))
	            								 .build()
	            				).getResponse()
	            			));
	            			break;
	            		default:
	            			System.out.println("No option '"+name+"'");
	            	}
	            }
	        } finally {
	            channel.shutdown();
	        }
	    }
}
