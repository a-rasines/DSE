package domain;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

public class Request<T> {
	
	public static <T> Request<T> fromJSON(Class<T> clazz, String json) {
		try {
			return fromJSON(clazz, new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Request<T> fromJSON(Class<T> clazz, JSONObject json) {
		try {
			if(clazz.isInstance("") || Number.class.isAssignableFrom(clazz))
				return new Request<T>(json.getString("id"), (T) json.get("body"));
			else
				return new Request<T>(json.getString("id"), (T) clazz.getMethod("fromJSON", JSONObject.class).invoke(null, json.getJSONObject("body")));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public final String id;
	public final T body;
	public Request(String id, T body) {
		this.id = id;
		this.body = body;
	}
}
