package io.lambda.proxy.request.model;

import java.io.Serializable;

/**
 * 
 * @author muditha
 *
 */
public class EmployeeSearchDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4714186118263034104L;
	
	private String firstName;
	private String lastName;
	private String type;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
