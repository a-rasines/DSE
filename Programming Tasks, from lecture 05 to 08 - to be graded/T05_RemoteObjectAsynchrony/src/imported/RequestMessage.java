package imported;

public class RequestMessage<T> implements IMarshall {
	private static final long serialVersionUID = 6353322385623094300L;
	private final KnownMethods method;
	private final T requestData;

	public RequestMessage(KnownMethods method, T requestData) {
		super();
		this.method = method;
		this.requestData = requestData;
	}

	public T getRequestData() {
		return requestData;
	}
	
	public KnownMethods getMethod() {
		return method;
	}
	
	public String toString() {
		return method.name() + " " + requestData.toString();
	}
}

