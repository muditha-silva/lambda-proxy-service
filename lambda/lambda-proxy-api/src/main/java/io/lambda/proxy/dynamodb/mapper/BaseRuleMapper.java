package io.lambda.proxy.dynamodb.mapper;

import java.io.Serializable;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


/**
 * 
 * @author muditha
 *
 */
public  class BaseRuleMapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8826818544651414168L;

	protected transient DynamoDBMapper dynamoDBMapper;
	protected transient AmazonDynamoDB amazonDynamoDB;

	public BaseRuleMapper(AmazonDynamoDB dynamoDB){		
		this.amazonDynamoDB=dynamoDB;
		this.dynamoDBMapper = new DynamoDBMapper(dynamoDB);
	}	
	
	/**
	 * 
	 * @param entities
	 * @return
	 */
	public <Entity> List<Entity> save(List<Entity> entities){
		dynamoDBMapper.batchSave(entities);
		return entities;
	}

	/**
	 * 
	 * @param entities
	 */
	public <Entity> void delete(List<Entity> entities){
		dynamoDBMapper.batchDelete(entities);
	}

	/**
	 * 
	 * @param clazz
	 * @param hashKey
	 * @return
	 */
	public <Entity> Entity getEntity(Class<Entity> clazz,String hashKey ){
		return dynamoDBMapper.load(clazz,hashKey);
	}

}
