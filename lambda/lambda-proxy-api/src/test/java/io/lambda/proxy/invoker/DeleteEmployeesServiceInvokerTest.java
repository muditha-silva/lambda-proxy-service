package io.lambda.proxy.invoker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.dynamodb.mapper.EmployeeMapper;
import io.lambda.proxy.exception.ResourceNotFoundException;
import io.lambda.proxy.exception.ServiceException;
import io.lambda.proxy.util.InvokerUtil;
import io.lambda.proxy.util.LambdaUtil;
import io.lambda.proxy.util.Request;
import io.lambda.proxy.util.Response;

/**
 * 
 * @author muditha
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteEmployeesServiceInvokerTest {

	private static final Gson gson = new GsonBuilder().create();
	private static final InvokerUtil INVOKER_UTIL = new InvokerUtil();

	@Mock
	private EmployeeMapper mapper;
	@Mock
	private BatchWriteItemResult results;

	private AbstractServiceInvoker invoker;
	private static Request request;
	private List<String> requestIds;
	private List<String> failedIds;

	@Captor
	private ArgumentCaptor<List<String>> idCaptor;

	@Before
	public void setUp() throws Exception {

		request = LambdaUtil.getRequest(DeleteEmployeesServiceInvokerTest.class.getClassLoader()
				.getResourceAsStream("deleteRequest.json"));      

		invoker= INVOKER_UTIL.getInvoker(request);

		requestIds=Arrays.asList("0001");
		failedIds=Arrays.asList("0001");

		given(mapper.deleteEmployees(idCaptor.capture())).willReturn(results);
	}

	@Test
	public void testSuccessfulExecution() throws ServiceException, ResourceNotFoundException {

		Response response=invoker.invoke(request, mapper);

		assertThat(idCaptor.getValue(),equalTo(requestIds));	

		String bodyString = gson.fromJson(response.toJSON(), JsonObject.class).get("body").getAsString();		
		JsonArray ids = gson.fromJson(bodyString,JsonArray.class);   

		assertThat(ids.get(0)!=null?ids.get(0).getAsString():null,equalTo(requestIds.get(0)));		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_OK));

	}

	@Test
	public void testFailExecution() throws ServiceException, ResourceNotFoundException {

		given(results.getUnprocessedItems()).willReturn(getUnprocessedItems());

		Response response=invoker.invoke(request, mapper);

		assertThat(idCaptor.getValue(),equalTo(requestIds) );	

		String bodyString = gson.fromJson(response.toJSON(), JsonObject.class).get("body").getAsString();		
		JsonArray ids = gson.fromJson(bodyString,JsonArray.class);   

		assertThat(ids.get(0)!=null?ids.get(0).getAsString():null,equalTo(failedIds.get(0)));		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));

	}

	private Map<String,List<WriteRequest>> getUnprocessedItems(){

		final List<WriteRequest> writeRequests=failedIds
				.parallelStream()
				.map(id->{							
					return new WriteRequest()
							.withDeleteRequest(new DeleteRequest()
									.withKey(Collections.unmodifiableMap(Stream.of(
											new SimpleEntry<>("id",new AttributeValue().withS(id) )						
											).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())))));
				})
				.collect(Collectors.toList());

		return Collections.unmodifiableMap(Stream.of(
				new SimpleEntry<>(Employee.TABLE_NAME,writeRequests)						
				).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

	}

}
