package server.marshalling;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class JSONObject extends HashMap<String, Object> implements IJSON{
	private static final long serialVersionUID = -5985646023590972459L;

	public static JSONObject from(Object o) {
		JSONObject obj = new JSONObject();
		for (Field f : o.getClass().getDeclaredFields())
			if(Modifier.isTransient(f.getModifiers()))
				continue;
			else
				try {
					f.setAccessible(true);
					obj.put(f.getName(), f.get(o));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
		return obj;
	}
	
	
	@Override
	public String toString() {
		List<String> values = new LinkedList<>();
		entrySet().forEach(v -> values.add('"' + v.getKey() + "\" : " + objectToString(v.getValue())));
		return "{" + String.join(", ", values) + "}";
	}
}
