package server.marshalling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JSONArray extends ArrayList<Object> implements IJSON{
	private static final long serialVersionUID = -6916370619767520934L;
	
	public JSONArray() {
		super();
	}
	public JSONArray(Collection<? extends Object> objs) {
		super(objs);
	}
	
	@Override
	public String toString() {
		List<String> values = new LinkedList<>();
		forEach(v->values.add(objectToString(v)));
		return "[" + String.join(", ", values) + "]";
	}
}
