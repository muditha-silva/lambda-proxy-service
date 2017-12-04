package io.lambda.proxy.lambda;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 
 * @author muditha
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class LambdaFunctionHandlerTest {

	private static final Gson gson = new GsonBuilder().create();
	private static String ID="0001";

	private LambdaFunctionHandler handler ;	
	@Mock
	private Context context;

	@Before
	public void setup() {
		given(context.getAwsRequestId()).willReturn(UUID.randomUUID().toString());
		handler=new LambdaFunctionHandler();

	}

	@Test
	public void test01_PostEmployee() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("postRequest.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();		
		JsonArray employees = gson.fromJson(bodyString,JsonArray.class);   

		//VERIFICATION

		assertThat(gson.fromJson(outputStream.toString(), JsonObject.class).get("statusCode").getAsInt(), 
				equalTo(HttpStatus.SC_OK));
		assertThat(getFieldValue(employees,"id"), equalTo(ID));
		assertThat(getFieldValue(employees,"type"), equalTo("SWE"));
		assertThat(getFieldValue(employees,"firstName"), equalTo("XYZ"));
		assertThat(getFieldValue(employees,"lastName"), equalTo("PQR"));
		assertThat(getFieldValue(employees,"addressLine1"), equalTo("Address1"));
		assertThat(getFieldValue(employees,"addressLine2"), equalTo("Address2"));
		assertThat(getFieldValue(employees,"postalCode"), equalTo("900"));
		assertThat(getFieldValue(employees,"city"), equalTo("Colombo"));
		assertThat(getFieldValue(employees,"countryCode"), equalTo("SL"));
		assertThat(getFieldValue(employees,"country"), equalTo("Sri Lanka"));

	}	

	@Test
	public void test02_PutEmployee() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("putRequest.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();		
		JsonArray employees = gson.fromJson(bodyString,JsonArray.class);   

		//VERIFICATION

		assertThat(gson.fromJson(outputStream.toString(), JsonObject.class).get("statusCode").getAsInt(), 
				equalTo(HttpStatus.SC_OK));
		assertThat(getFieldValue(employees,"id"), equalTo(ID));
		assertThat(getFieldValue(employees,"type"), equalTo("SWA"));
		assertThat(getFieldValue(employees,"firstName"), equalTo("XYZ"));
		assertThat(getFieldValue(employees,"lastName"), equalTo("PQR"));
		assertThat(getFieldValue(employees,"addressLine1"), equalTo("Address1"));
		assertThat(getFieldValue(employees,"addressLine2"), equalTo("Address2"));
		assertThat(getFieldValue(employees,"postalCode"), equalTo("900"));
		assertThat(getFieldValue(employees,"city"), equalTo("Colombo"));
		assertThat(getFieldValue(employees,"countryCode"), equalTo("SL"));
		assertThat(getFieldValue(employees,"country"), equalTo("Sri Lanka"));

	}	

	@Test
	public void test03_getEmployee() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("getRequest.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();		
		JsonArray employees = gson.fromJson(bodyString,JsonArray.class);   

		//VERIFICATION

		assertThat(gson.fromJson(outputStream.toString(), JsonObject.class).get("statusCode").getAsInt(), 
				equalTo(HttpStatus.SC_OK));
		assertThat(getFieldValue(employees,"id"), equalTo(ID));
		assertThat(getFieldValue(employees,"type"), equalTo("SWA"));
		assertThat(getFieldValue(employees,"firstName"), equalTo("XYZ"));
		assertThat(getFieldValue(employees,"lastName"), equalTo("PQR"));
		assertThat(getFieldValue(employees,"addressLine1"), equalTo("Address1"));
		assertThat(getFieldValue(employees,"addressLine2"), equalTo("Address2"));
		assertThat(getFieldValue(employees,"postalCode"), equalTo("900"));
		assertThat(getFieldValue(employees,"city"), equalTo("Colombo"));
		assertThat(getFieldValue(employees,"countryCode"), equalTo("SL"));
		assertThat(getFieldValue(employees,"country"), equalTo("Sri Lanka"));

	}	

	@Test
	public void test04_getEmployeeById() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("getById_OK.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();		
		JsonObject employee = gson.fromJson(bodyString,JsonObject.class);   

		//VERIFICATION

		assertThat(gson.fromJson(outputStream.toString(), JsonObject.class).get("statusCode").getAsInt(), 
				equalTo(HttpStatus.SC_OK));
		assertThat(employee.get("id")!=null?employee.get("id").getAsString():null, equalTo(ID));
		assertThat(employee.get("type")!=null?employee.get("type").getAsString():null, equalTo("SWA"));
		assertThat(employee.get("firstName")!=null?employee.get("firstName").getAsString():null, equalTo("XYZ"));
		assertThat(employee.get("lastName")!=null?employee.get("lastName").getAsString():null, equalTo("PQR"));
		assertThat(employee.get("addressLine1")!=null?employee.get("addressLine1").getAsString():null, equalTo("Address1"));
		assertThat(employee.get("addressLine2")!=null?employee.get("addressLine2").getAsString():null, equalTo("Address2"));
		assertThat(employee.get("postalCode")!=null?employee.get("postalCode").getAsString():null, equalTo("900"));
		assertThat(employee.get("city")!=null?employee.get("city").getAsString():null, equalTo("Colombo"));
		assertThat(employee.get("countryCode")!=null?employee.get("countryCode").getAsString():null, equalTo("SL"));
		assertThat(employee.get("country")!=null?employee.get("country").getAsString():null, equalTo("Sri Lanka"));

	}	

	@Test
	public void test04_employeeNotFound() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("getById_NOT_FOUND.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();
		JsonObject bodyJson = gson.fromJson(bodyString,JsonObject.class);

		assertThat(bodyJson.get("type")!=null?bodyJson.get("type").getAsString():null,equalTo("[NotFound]"));
		assertThat(bodyJson.get("code")!=null?bodyJson.get("code").getAsInt():null,equalTo(HttpStatus.SC_NOT_FOUND));

	}	

	@Test
	public void test05_deleteEmployee() {

		InputStream inputStream = LambdaFunctionHandlerTest.class
				.getClassLoader()
				.getResourceAsStream("deleteRequest.json");

		OutputStream outputStream= new ByteArrayOutputStream();
		handler.handleRequest(inputStream,outputStream,context);

		String bodyString = gson.fromJson(outputStream.toString(), JsonObject.class).get("body").getAsString();		
		JsonArray ids = gson.fromJson(bodyString,JsonArray.class);   

		//VERIFICATION
		assertThat(gson.fromJson(outputStream.toString(),JsonObject.class).get("statusCode").getAsInt(), 
				equalTo(HttpStatus.SC_OK));
		assertThat(ids.get(0)!=null?ids.get(0).getAsString():null,equalTo(ID));

	}	

	private String getFieldValue(JsonArray array,String field){

		return (array.isJsonArray()?(array.get(0).isJsonObject()?
				(array.get(0).getAsJsonObject().get(field)!=null?
						array.get(0).getAsJsonObject().get(field).getAsString():null):null):null);

	}

}
