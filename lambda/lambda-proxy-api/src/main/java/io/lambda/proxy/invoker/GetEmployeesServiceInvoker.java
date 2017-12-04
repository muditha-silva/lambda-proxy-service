package io.lambda.proxy.invoker;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.request.model.EmployeeSearchDTO;
import io.lambda.proxy.util.LambdaLogger;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 *
 * Retrieve Employees by different criteria, filter criteria pass as query string parameters
 * E.g. 'type','firstName'. 
 * 
 */
public class GetEmployeesServiceInvoker extends AbstractServiceInvoker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7821214822309293272L;

	private static final Logger LOG = LambdaLogger.getLogger(GetEmployeesServiceInvoker.class);

	@Override
	public Response invoke(final Request request,final EmployeeMapper employeeMapper) throws ServiceException, ResourceNotFoundException {

		try{

			final String type=(request.getJsonObject().get("queryStringParameters").isJsonObject()?request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("type")!=null?
					request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("type").getAsString():null:null);

			final String firstName=(request.getJsonObject().get("queryStringParameters").isJsonObject()?request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("firstName")!=null?
					request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("firstName").getAsString():null:null);

			EmployeeSearchDTO searchDTO=new EmployeeSearchDTO();

			if(StringUtils.isNotEmpty(type)){
				searchDTO.setType(type);
			}

			if(StringUtils.isNotEmpty(firstName)){
				searchDTO.setFirstName(firstName);
			}

			final List<Employee> employees=employeeMapper.findEmployee(searchDTO);

			return new Response()
					.withBody(employees);
		}
		catch(Exception ex){
			LOG.error(String.format("An Exception has occoured while processing the request %s ",ex.getMessage()));
			throw new ServiceException(ex.getMessage());
		}

	}
}
