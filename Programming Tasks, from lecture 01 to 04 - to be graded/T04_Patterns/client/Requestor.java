package client;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import common.utils.MessageMarshaller;
import common.utils.RemoteException;

public class Requestor {

	public static List<String> generateMessage(String method, List<String> params) throws IOException, RemoteException{
		System.out.println("[Requestor] Received '"+method+"' method. Parsing and sending");
		List<String> msg = new LinkedList<String>(params);
		msg.add(0, method);
		List<String> l = MessageMarshaller.demarshall(RequestHandler.getInstace().sendMessage(MessageMarshaller.marshall(msg)));
		System.out.println("[Requestor] Received response, checking for errors");
		if(l.get(0).equals("200")) {
			l.remove(0);
			return l;
		}
		else throw new RemoteException(Integer.parseInt(l.get(0)), l.get(1));
	}
}
