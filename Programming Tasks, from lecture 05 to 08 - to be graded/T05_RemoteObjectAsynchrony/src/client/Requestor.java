package client;

import java.io.IOException;

import client.domain.Request;
import imported.KnownMethods;
import imported.RequestMessage;

public class Requestor {

	public static <T, Y> Request<T, Y> generateRequest(KnownMethods method, T data, Class<Y> responseType) throws IOException{
		return new Request<>(new RequestMessage<T>(method, data));
	}
}
