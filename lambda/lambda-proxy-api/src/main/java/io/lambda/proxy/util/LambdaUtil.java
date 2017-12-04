package io.lambda.proxy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * 
 * @author muditha
 *
 */
public final class LambdaUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8763271363773252545L;

	private static final Log LOG = LogFactory.getLog(LambdaUtil.class);
	private static final Gson gson = new GsonBuilder().create();

	public static JsonObject getJsonObject(final InputStream input) {
		
		JsonObject jsonObject = null;
		try (
				final InputStreamReader isr = new InputStreamReader(input, Charset.forName("UTF-8"));
				final BufferedReader reader = new BufferedReader(isr)
				) {
			jsonObject = gson.fromJson(reader, JsonObject.class);
		}catch(IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}
		return jsonObject;
	}

	public static Request getRequest(InputStream input) {
		return new Request(getJsonObject(input));
	}

}
