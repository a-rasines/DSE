package server.marshalling;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface IJSON {
	
	default String objectToString(Object o) {
		if(o instanceof Number)
			return o.toString();
		else if (o instanceof String)
			return "\"" + o + "\"";
		else if (o instanceof Collection) {
			List<String> values = new LinkedList<>();
			for(Object v : (Collection<?>) o)
				values.add(objectToString(v));
			return "[" + String.join(",", values) + "]";
			
		} else if (o instanceof Map) {
			List<String> values = new LinkedList<>();
			for(Entry<?,?> v : ((Map<?, ?>) o).entrySet())
				values.add(objectToString(v.getKey()) + ":" + objectToString(v.getValue()));
			return "{" + String.join(",", values) + "}";
		} else 
			return JSONObject.from(o).toString();
	}

}
