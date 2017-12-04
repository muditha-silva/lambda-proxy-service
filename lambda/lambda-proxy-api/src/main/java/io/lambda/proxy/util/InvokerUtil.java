package io.lambda.proxy.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.invoker.AbstractServiceInvoker;
import io.lambda.proxy.invoker.DeleteEmployeeServiceInvoker;
import io.lambda.proxy.invoker.DeleteEmployeesServiceInvoker;
import io.lambda.proxy.invoker.GetEmployeeServiceInvoker;
import io.lambda.proxy.invoker.GetEmployeesServiceInvoker;
import io.lambda.proxy.invoker.PersistEmployeesServiceInvoker;
import io.lambda.proxy.invoker.PutEmployeeServiceInvoker;

/**
 * 
 * @author muditha
 *
 * Utility class to resolve the appropriate implementation for the different operations
 */
public class InvokerUtil implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5981045686367086741L;

	private final AbstractServiceInvoker getEmployeesServiceInvoker;
	private final AbstractServiceInvoker persistEmployeesServiceInvoker;
	private final AbstractServiceInvoker deleteEmployeesServiceInvoker;
	private final AbstractServiceInvoker getEmployeeServiceInvoker;
	private final AbstractServiceInvoker deleteEmployeeServiceInvoker;
	private final AbstractServiceInvoker putEmployeeServiceInvoker;

	public InvokerUtil(){

		getEmployeesServiceInvoker=new GetEmployeesServiceInvoker();
		persistEmployeesServiceInvoker=new PersistEmployeesServiceInvoker();		
		deleteEmployeesServiceInvoker=new DeleteEmployeesServiceInvoker();	
		getEmployeeServiceInvoker=new GetEmployeeServiceInvoker();
		deleteEmployeeServiceInvoker=new DeleteEmployeeServiceInvoker();
		putEmployeeServiceInvoker=new PutEmployeeServiceInvoker();

	}

	public  AbstractServiceInvoker getInvoker(Request request) throws ServiceException{	

		final String resource=(request.getJsonObject()!=null?
				request.getJsonObject().get("resource")!=null?request.getJsonObject().get("resource").getAsString():null:null);

		final String httpMethod=(request.getJsonObject()!=null?
				request.getJsonObject().get("httpMethod")!=null?request.getJsonObject().get("httpMethod").getAsString():null:null);

		if(StringUtils.isEmpty(resource) || StringUtils.isEmpty(httpMethod)){
			throw new  IllegalArgumentException(String.format("Missing mandatory attributes 'resources' %s 'operation' %s",resource,httpMethod));
		}

		if(resource.equals("/v1/employee")){

			if(httpMethod.equals("GET")){
				return getEmployeesServiceInvoker;
			}
			else if(httpMethod.equals("POST")){
				return persistEmployeesServiceInvoker;
			}
			else if(httpMethod.equals("PUT")){
				return persistEmployeesServiceInvoker;
			}
			else if(httpMethod.equals("DELETE")){
				return deleteEmployeesServiceInvoker;
			}			
		}
		else if(resource.equals("/v1/employee/{id}")){
			if(httpMethod.equals("GET")){
				return getEmployeeServiceInvoker;
			}	
			else if(httpMethod.equals("DELETE")){
				return deleteEmployeeServiceInvoker;
			}
			else if(httpMethod.equals("PUT")){
				return putEmployeeServiceInvoker;
			}			
		}
		throw new ServiceException(String.format("No invoker implemented for the 'resources' %s 'operation' %s",resource,httpMethod));
	}

}
