package io.lambda.proxy.invoker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;

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
 * Delete Employees by ids (partition keys)
 *
 */
public class DeleteEmployeesServiceInvoker extends AbstractServiceInvoker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1533553569676695390L;

	private static final Logger LOG = LambdaLogger.getLogger(DeleteEmployeesServiceInvoker.class);	

	@Override
	public Response invoke(final Request request,final EmployeeMapper employeeMapper) throws ServiceException,ResourceNotFoundException {	

		try{
			/**
			 * Employee ids pass as query string parameters E.g. 'ids=id1,id2'
			 */
			final String ids=(request.getJsonObject().get("queryStringParameters").isJsonObject()?request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("ids")!=null?
					request.getJsonObject().get("queryStringParameters").getAsJsonObject().get("ids").getAsString():null:null);

			final List<String> idList=(StringUtils.split(ids,",")!=null?Arrays.asList(StringUtils.split(ids,",")):Collections.emptyList());

			/**
			 * Partition the ids into batches by 'BATCH_SIZE'
			 * Delete Employees in batches, by batch of ids (partition keys) 
			 * This is done in separate threads in non-blocking fashion using the 'CompletableFuture'
			 */
			final List<CompletableFuture<BatchWriteItemResult>> futures = ListUtils
					.partition(idList, Constants.BATCH_SIZE)
					.stream()
					.map(list -> CompletableFuture.supplyAsync(() -> employeeMapper.deleteEmployees(list)))
					.collect(Collectors.toList());

			final List<BatchWriteItemResult> results=futures
					.stream()
					.map(future -> {
						return future.join();
					})
					.collect(Collectors.toList());	

			final List<String> failedIds=results
					.stream()
					.filter(result->MapUtils.isNotEmpty(result.getUnprocessedItems()))
					.flatMap(result->{
						return result.getUnprocessedItems().entrySet().stream();
					})			
					.flatMap( entry->{
						return entry
								.getValue()
								.stream()
								.flatMap( writeRequest->{
									return writeRequest
											.getDeleteRequest()
											.getKey()
											.entrySet()
											.stream()
											.map(entry1->{
												return entry1.getValue().getS();
											});
								});
					})		
					.collect(Collectors.toList());

			if(CollectionUtils.isNotEmpty(failedIds)){

				return new Response()
						.withBody(failedIds)
						.withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}

			return new Response()
					.withBody(idList);

		}
		catch(Exception ex){
			LOG.error(String.format("An Exception has occoured while processing the request %s ",ex.getMessage()));
			throw new ServiceException(ex.getMessage());
		}

	}

}
