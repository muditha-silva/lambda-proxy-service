package io.lambda.proxy.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.invoker.AbstractServiceInvoker;
import io.lambda.proxy.util.Error;
import io.lambda.proxy.util.InvokerUtil;
import io.lambda.proxy.util.LambdaLogger;
import io.lambda.proxy.util.LambdaUtil;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;


/**
 * 
 * @author muditha
 *
 */
public class LambdaFunctionHandler implements RequestStreamHandler{ 

	private static final Logger LOG = LambdaLogger.getLogger(LambdaFunctionHandler.class);

	private static final AmazonDynamoDB DYNAMO_DB= AmazonDynamoDBClientBuilder.defaultClient();

	private static final EmployeeMapper EMPLOYEE_MAPPER=new EmployeeMapper(DYNAMO_DB);	

	private static final InvokerUtil INVOKER_UTIL = new InvokerUtil();

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {

		final Request request = LambdaUtil.getRequest(inputStream);	

		LOG.info("Request: " + request.getJsonObject());

		try {
			final AbstractServiceInvoker invoker= INVOKER_UTIL.getInvoker(request);

			final Response response = invoker.invoke(request,EMPLOYEE_MAPPER);

			response.send(outputStream, context);

		}
		catch (ServiceException | IOException  ex) {			
			Error error = new Error(ex.getMessage(), Error.Reason.INTERNAL_SERVER_ERROR, context);
			try {
				new Response().withError(error).send(outputStream, context);
			}
			catch(IOException e){
				LOG.error("IOException occurs at marshalling the error response "+e.getMessage());
			}
		}
		catch(IllegalArgumentException ex){			
			try {
				
				Error error = new Error(ex.getMessage(), Error.Reason.BAD_REQUEST, context);
				new Response().withError(error).send(outputStream, context);
			}
			catch(IOException e){
				LOG.error("IOException occurs at marshalling the error response "+e.getMessage());
			}

		} catch (ResourceNotFoundException ex) {		
			try {
				Error error = new Error(ex.getMessage(), Error.Reason.NOT_FOUND, context);
				new Response().withError(error).send(outputStream, context);
			}
			catch(IOException e){
				LOG.error("IOException occurs at marshalling the error response "+e.getMessage());
			}
		}
	}

}
