package io.lambda.proxy.invoker;

import java.util.Collections;

import org.apache.logging.log4j.Logger;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.util.LambdaLogger;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 * 
 * Delete Employee by id (partition key)
 *
 */
public class DeleteEmployeeServiceInvoker extends AbstractServiceInvoker {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7821214822309293272L;	

	private static final Logger LOG = LambdaLogger.getLogger(DeleteEmployeeServiceInvoker.class);

	@Override
	public Response invoke(final Request request,final EmployeeMapper employeeMapper) throws ResourceNotFoundException,ServiceException {	

		final String id=(request.getJsonObject().get("pathParameters").isJsonObject()?request.getJsonObject().get("pathParameters").getAsJsonObject().get("id")!=null?
				request.getJsonObject().get("pathParameters").getAsJsonObject().get("id").getAsString():null:null);

		final Employee employee=employeeMapper.getEntity(Employee.class, id);

		if(employee == null){
			LOG.error("Unable to find employee for 'id' "+id);
			throw new ResourceNotFoundException("Unable to find employee for 'id' "+id);
		}

		employeeMapper.delete(Collections.singletonList(employee));

		return new Response()
				.withBody(employee);				

	}

}
