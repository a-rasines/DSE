package server;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import domain.Decriptor;
import domain.EncriptionPair;
import domain.EncriptionPair.KeyPair;
import domain.Encriptor;
import io.grpc.stub.StreamObserver;
import server.CipherServiceProto.ClientData;
import server.CipherServiceProto.EmptyMessage;
import server.CipherServiceProto.StringRequest;
import server.CipherServiceProto.StringResponse;

public class CipherServiceImpl extends CipherGrpc.CipherImplBase{
	private static Map<String, EncriptionPair> encriptorPool = new HashMap<>();
	private static Map<String, KeyPair> uninitializedEncriptors = new HashMap<>();
    public void sendMessagePool(StringRequest request, StreamObserver<StringResponse> responseObserver) {
    	System.out.println("SendMessagePool received");
    	String inputMessage = request.getMessage();
    	String id = request.getId();
    	EncriptionPair pair = encriptorPool.get(id);
    	
        System.out.println("Received: " + pair.decriptor.decrypt(inputMessage));

        StringResponse response = StringResponse.newBuilder()
                .setResponse(pair.encriptor.encrypt("200 OK"))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void sendMessageReq(StringRequest request, StreamObserver<StringResponse> responseObserver) {
    	System.out.println("SendMessageReq received");
    	String inputMessage = request.getMessage();
    	String id = request.getId();
    	EncriptionPair pair = EncriptionPair.fromKeyPair(uninitializedEncriptors.get(id));
    	
    	System.out.println("Received: " + pair.decriptor.decrypt(inputMessage));

        StringResponse response = StringResponse.newBuilder()
                .setResponse(pair.encriptor.encrypt("200 OK"))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void startConnection(EmptyMessage request, StreamObserver<ClientData> responseObserver) {
    	System.out.println("StartConnection received");
    	Encriptor.Builder e = new Encriptor.Builder();
    	Decriptor.Builder d = new Decriptor.Builder();
    	String id = Integer.toHexString(e.hashCode());
    	encriptorPool.put(id, new EncriptionPair(e.encriptor, d.dec));
    	uninitializedEncriptors.put(id, new KeyPair(e.encriptor.getKey(), d.dec.getKey()));
    	ClientData response = ClientData.newBuilder()
                 .setId(id)
                 .setPrivateKey(new String(Base64.getEncoder().encode(e.privateKey.getEncoded())))
                 .setPublicKey(new String(Base64.getEncoder().encode(d.key.getEncoded())))
                 .build();

         responseObserver.onNext(response);
         responseObserver.onCompleted();
    }
    @Override
    public String toString() {
    	return super.toString();
    }

}
