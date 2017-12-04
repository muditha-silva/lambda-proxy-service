package io.lambda.proxy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * @author muditha
 * 
 * Encapsulates the request utilized by the Lambda function.
 *  
 */
public class Request {

	private static final Gson gson =  new GsonBuilder().create();
	private final JsonObject jsonObject;

	public Request(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public <T> T getBody(Class<T> clazz) {
		JsonObject inputJson = getJsonObject();
		JsonElement body = inputJson.get("body");
		if(body == null) throw new IllegalStateException("{body} is missing");
		return  gson.fromJson(body.getAsString(), clazz);
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}
	
}
