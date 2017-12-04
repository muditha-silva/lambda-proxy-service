package io.lambda.proxy.util;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * 
 * @author muditha
 *
 * Encapsulates HTTP standard errors 
 * E.g 'BAD_REQUEST','FORBIDDEN','NOT_FOUND','INTERNAL_SERVER_ERROR'
 */
public class Error {

	private String type;
	private int code;
	private String message;
	private String requestId;


	public Error(String message, Reason reason, Context context) {
		this.type = reason.getText();
		this.code = reason.getCode();
		this.message = message;
		this.requestId = context.getAwsRequestId();
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public enum Reason {
		BAD_REQUEST("[BadRequest]", 400),
		FORBIDDEN("[Forbidden]", 403),
		NOT_FOUND("[NotFound]", 404),
		INTERNAL_SERVER_ERROR("[InternalServerError]", 500);

		private String value;
		private int code;

		Reason(final String value, int code) {
			this.value = value;
			this.code = code;
		}

		public String getText() {
			return value;
		}
		public int getCode() {
			return code;
		}

		@Override
		public String toString() {
			return this.getText()+"," + this.getCode();
		}
	}
}
