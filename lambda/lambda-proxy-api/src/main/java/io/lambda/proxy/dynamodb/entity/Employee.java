package io.lambda.proxy.dynamodb.entity;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;


/**
 * 
 * @author muditha
 *
 */
@DynamoDBTable(tableName = "Employee")
public class Employee  implements Serializable  {	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1387751894831280656L;	

	public static final String TABLE_NAME="Employee";	
	public static final String DESIGNATION_INDEX  ="type-index";

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String id;
	@DynamoDBIndexHashKey(attributeName="type", globalSecondaryIndexName="type-index")
	private String type;
	private String firstName;
	private String lastName;
	private String addressLine1;
	private String addressLine2;
	private String postalCode;
	private String city;
	private String countryCode;
	private String country;
	private double salary;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}

}
