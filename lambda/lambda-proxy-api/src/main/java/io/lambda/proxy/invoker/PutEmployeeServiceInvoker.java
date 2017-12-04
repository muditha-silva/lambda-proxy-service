package io.lambda.proxy.invoker;

import java.util.Collections;

import org.apache.logging.log4j.Logger;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.util.LambdaLogger;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 *
 * Persistent service for Update Employee by id
 */
public class PutEmployeeServiceInvoker extends AbstractServiceInvoker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6402595353201621574L;

	private static final Logger LOG = LambdaLogger.getLogger(PutEmployeeServiceInvoker.class);


	@Override
	public Response invoke(Request request,EmployeeMapper ruleMapper) throws ServiceException {

		try{

			String id=(request.getJsonObject().get("pathParameters").isJsonObject()?request.getJsonObject().get("pathParameters").getAsJsonObject().get("id")!=null?
					request.getJsonObject().get("pathParameters").getAsJsonObject().get("id").getAsString():null:null);

			final Employee employee=  request.getBody(Employee.class);

			employee.setId(id);

			ruleMapper.save(Collections.singletonList(employee));		

			return new Response()
					.withBody(employee);

		}
		catch(Exception ex){
			LOG.error(String.format("An Exception has occoured while processing the request %s ", ex.getMessage()));
			throw new ServiceException(ex.getMessage());
		}

	}

}
