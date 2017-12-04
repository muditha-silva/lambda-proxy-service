package io.lambda.proxy.invoker;

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
 * Retrieve Employee entity by id , id pass as a path Parameter
 *
 */
public class GetEmployeeServiceInvoker extends AbstractServiceInvoker {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7821214822309293272L;	

	private static final Logger LOG = LambdaLogger.getLogger(GetEmployeeServiceInvoker.class);

	@Override
	public Response invoke(final Request request,EmployeeMapper employeeMapper) throws ServiceException,ResourceNotFoundException {	

		String id=(request.getJsonObject().get("pathParameters").isJsonObject()?request.getJsonObject().get("pathParameters").getAsJsonObject().get("id")!=null?
				request.getJsonObject().get("pathParameters").getAsJsonObject().get("id").getAsString():null:null);

		Employee employee= employeeMapper.getEntity(Employee.class, id);

		if(employee == null){
			LOG.error("Unable to find employee for 'id' "+id);
			throw new ResourceNotFoundException("Unable to find employee for 'id' "+id);
		}

		return new Response()
				.withBody(employee);		

	}

}
