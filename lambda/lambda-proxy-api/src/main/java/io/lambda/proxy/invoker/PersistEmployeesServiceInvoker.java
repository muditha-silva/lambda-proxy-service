package io.lambda.proxy.invoker;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.util.Constants;
import io.lambda.proxy.util.LambdaLogger;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 *
 * Persistent service for Create and Update Employees 
 */
public class PersistEmployeesServiceInvoker extends AbstractServiceInvoker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3882664216665716159L;

	private static final Logger LOG = LambdaLogger.getLogger(PersistEmployeesServiceInvoker.class);

	private static final Gson GSON=new GsonBuilder().create();

	@Override
	public Response invoke(final Request request,final EmployeeMapper employeeMapper) throws ServiceException, ResourceNotFoundException {

		try {

			final List<Employee> employees = GSON.fromJson(request.getBody(JsonArray.class),
					new TypeToken<List<Employee>>() {
			}.getType());

			/**
			 * Partition the Employee list into batches by 'BATCH_SIZE'
			 * Persist each batch of Employee list using separate threads in non-blocking fashion
			 */
			List<CompletableFuture<List<Employee>>> futures = ListUtils
					.partition(employees, Constants.BATCH_SIZE)
					.stream()
					.map(employeeList -> CompletableFuture.supplyAsync(() -> employeeMapper.save(employeeList)))
					.collect(Collectors.toList());

			List<Employee> employeeList = futures
					.stream()
					.flatMap(future -> {
						return future.join().stream();
					})
					.collect(Collectors.toList());

			return new Response()
					.withBody(employeeList);

		} catch (Exception ex) {
			LOG.error(String.format("An Exception has occoured while processing the request %s ", ex.getMessage()));
			throw new ServiceException(ex.getMessage());
		}

	}

}
