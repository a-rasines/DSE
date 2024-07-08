package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import common.utils.MessageMarshaller;
import common.utils.RemoteException;

public class Invoker {
	@SuppressWarnings("unchecked")
	public static String invoke(String input) {
		List<String> params = MessageMarshaller.demarshall(input);
		String methodName = params.get(0);
		System.out.println("[Invoker] Received request for '"+methodName+"', searching");
		params.remove(0);
		for(Method m :Invoker.class.getDeclaredMethods())
			if(m.getName().equals(methodName)) {
				System.out.println("[Invoker] Method found, invoking");
				List<String> c;
				try {
					c = new ArrayList<>((List<String>) m.invoke(null, params.toArray()));
					System.out.println("[Invoker] Method successfully invoked, returning");
					c.add(0, "200");
					return MessageMarshaller.marshall(c);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					System.out.println("[Invoker] Method invocation failure, generating feedback");
					return MessageMarshaller.marshall(List.of(""+RemoteException.ERROR_CODE.SERVER_ERROR.code, e.getMessage()));
				}
				
			}
		System.out.println("[Invoker] Method not found, generating feedback");
		return MessageMarshaller.marshall(List.of(""+RemoteException.ERROR_CODE.NOT_FOUND.code, "Method not found"));
	}
	
	@SuppressWarnings("unused")
	private static List<String> hello(String s) {
		System.out.println("[Invoker] Method invoked");
		return List.of("Hello " + s+"!");
	}
	
	@SuppressWarnings("unused")
	private static List<String> goodbye(String s) {
		System.out.println("[Invoker] Method invoked");
		return List.of(s+", I'm afraid this is a farewell.");
	}
}
