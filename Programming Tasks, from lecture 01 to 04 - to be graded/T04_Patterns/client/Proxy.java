package client;

import java.io.IOException;
import java.util.List;

import common.utils.RemoteException;

public class Proxy {
	
	public static String hello(String name) {
		try {
			System.out.println("[Proxy] Sending command");
			return Requestor.generateMessage("hello", List.of(name)).get(0);
		} catch (IOException | RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String goodbye(String name) {
		try {
			return Requestor.generateMessage("goodbye", List.of(name)).get(0);
		} catch (IOException | RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String teapot(String name) {
		try {
			return Requestor.generateMessage("teapot", List.of(name)).get(0);
		} catch (IOException | RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
