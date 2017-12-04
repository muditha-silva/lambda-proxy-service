package io.lambda.proxy.dynamodb.mapper;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DeleteRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.util.CollectionUtils;

import io.lambda.proxy.dynamodb.entity.Employee;
import io.lambda.proxy.request.model.EmployeeSearchDTO;

/**
 * 
 * @author muditha
 *
 */
public class EmployeeMapper extends BaseRuleMapper{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1515099281396067214L;

	public EmployeeMapper(AmazonDynamoDB dynamoDB) {
		super(dynamoDB);		
	}

	/**
	 * 
	 * Retrieve Employees by different search criteria E.g type,firstName
	 */
	public List<Employee> findEmployee(final EmployeeSearchDTO searchDTO) {

		final Employee hashKObject =new Employee();		
		hashKObject.setType(searchDTO.getType());

		DynamoDBQueryExpression<Employee> dynamoDBQueryExpression = 
				new DynamoDBQueryExpression<Employee>()
				.withHashKeyValues(hashKObject)
				.withIndexName(Employee.DESIGNATION_INDEX);

		if(StringUtils.isNotEmpty(searchDTO.getFirstName())){
			dynamoDBQueryExpression.withQueryFilterEntry("firstName",
					new Condition().withComparisonOperator(ComparisonOperator.EQ)
					.withAttributeValueList(new AttributeValue().withS(searchDTO.getFirstName())));
		}

		dynamoDBQueryExpression.withConsistentRead(false);

		List<Employee> rules = dynamoDBMapper.query(Employee.class, dynamoDBQueryExpression);

		return (CollectionUtils.isNullOrEmpty(rules) ? Collections.emptyList() : rules);

	}	

	/**
	 * 
	 * Use the BatchWriteItem operation to deletes multiple items
	 * 
	 */
	public BatchWriteItemResult deleteEmployees(final List<String> ids){

		final List<WriteRequest> writeRequests=ids
				.parallelStream()
				.map(id->{							
					return new WriteRequest()
							.withDeleteRequest(new DeleteRequest()
									.withKey(Collections.unmodifiableMap(Stream.of(
											new SimpleEntry<>("id",new AttributeValue().withS(id) )						
											).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())))));
				})
				.collect(Collectors.toList());

		final BatchWriteItemRequest request=new BatchWriteItemRequest()
				.withRequestItems(Collections.unmodifiableMap(Stream.of(
						new SimpleEntry<>(Employee.TABLE_NAME,writeRequests)						
						).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue()))));

		BatchWriteItemResult result=amazonDynamoDB.batchWriteItem(request);		
				
		return result;

	}

}
