package common.utils;

public class RemoteException extends Exception {
	public static enum ERROR_CODE {
		UNKNOWN(-1),
		BAD_REQUEST(400),
		UNAUTHENTICATED(401),
		FORBIDDEN(403),
		NOT_FOUND(404),
		TOO_MANY_REQUESTS(429),
		SERVER_ERROR(500);
		public final int code;
		ERROR_CODE (int code) {
			this.code = code;
		}
		public static ERROR_CODE of(int code) {
			for(ERROR_CODE ec: values())
				if(ec.code == code)
					return ec;
			return UNKNOWN;
		}
	}
	private static final long serialVersionUID = 3167169414590481918L;
	int code;
	public RemoteException(int code, String message) {
		super(message);
		this.code = code;
	}
	@Override
	public void printStackTrace() {
		System.err.println("Code "+code+": "+ERROR_CODE.of(code)+" caused by " + getMessage());
		super.printStackTrace();
	}
	
	/**
	 * Returns the type of error based on HTTP responses
	 * @return
	 */
	public int getErrorCode() {
		return code;
	}
}
