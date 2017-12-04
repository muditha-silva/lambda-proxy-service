package io.lambda.proxy.invoker;

import java.io.Serializable;

import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 *
 */

public abstract class AbstractServiceInvoker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1549741813093183142L;

	public abstract Response invoke(final Request request,final EmployeeMapper employeeMapper) throws ServiceException, ResourceNotFoundException;

}


