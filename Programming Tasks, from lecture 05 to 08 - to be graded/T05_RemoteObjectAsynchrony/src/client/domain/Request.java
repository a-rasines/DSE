package client.domain;

import java.util.Base64;
import java.util.function.Consumer;

import client.RequestHandler;
import imported.IMarshall;
import imported.RequestMessage;
import imported.ResponseMessage;

public class Request<Req, Res> {

	private RequestMessage<Req> message;
	private ResponseMessage<Res> response;
	private boolean responded = false;
	public Request(RequestMessage<Req> message) {
		this.message = message;
	}
	
	public Request<Req, Res> then(Consumer<ResponseMessage<Res>> cons) {
		this.after = cons;
		return this;
	}
	private Consumer<ResponseMessage<Res>> after = (v)->{}; 
	
	/**
	 * Sends a message and doesn't wait for response
	 */
	public void send() {
		try {
			System.out.println("[Request] Sending without waiting " + this.message);
			RequestHandler.getInstace().sendMessage(new String(Base64.getEncoder().encode(this.message.marshall())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message and acknowledges that a response will be returned but doesn't block to wait
	 */
	public void queue() {
		if(responded) //No need to ask twice for same request
			return;
		try {
			System.out.println("[Request] Sending with future response " + this.message);
			new Thread(this::_get).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns whether the server has responded the message
	 * @return
	 */
	public boolean isResponded() {
		return responded;
	}
	
	/**
	 * The response the server has given or null if none has been given yet
	 * @return
	 */
	public ResponseMessage<Res> getResponse() {
		return response;
	}
	
	/**
	 * Sends a message and waits for response
	 */
	public ResponseMessage<Res> get() {
		if(responded) //No need to ask twice for same request
			return response;
		try {
			System.out.println("[Request] Sending and waiting " +this.message);
			_get();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void _get() {
		try {
			RequestHandler safeHandle = RequestHandler.getInstace();
			byte[] res = Base64.getDecoder().decode(safeHandle.sendMessageAndWait(new String(Base64.getEncoder().encode(this.message.marshall()))).getBytes());
			responded = true;
			response = IMarshall.unmarshall(res);
			after.accept(response);
		} catch (Exception e) {
			e.printStackTrace();
			responded = false;
		}
		
	}
}
