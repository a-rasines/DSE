package imported;

public class ResponseMessage<T> implements IMarshall {

	private static final long serialVersionUID = -692687925008724699L;
	private final T responseData;

	public ResponseMessage(T responseData) {
		this.responseData = responseData;
	}

	public T getResponseData() {
		return responseData;
	}
}