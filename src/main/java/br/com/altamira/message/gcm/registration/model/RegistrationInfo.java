package br.com.altamira.message.gcm.registration.model;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "GCM_REGISTRATION")
public class RegistrationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8653131924082534504L;

	@DynamoDBIndexHashKey
	@DynamoDBAutoGeneratedKey
	protected String id;

	protected Date timestamp;

	@DynamoDBHashKey
	private String email;

	@DynamoDBRangeKey
	private String token;

	private Device device; // Device

	public RegistrationInfo() {
		this.id = java.util.UUID.randomUUID().toString();
		this.timestamp = new Date();
	}

	@JsonCreator
	public RegistrationInfo(
			@JsonProperty("email") String email,
			@JsonProperty("token") String token,
			@JsonProperty("device") Device device) {
		super();
		this.id = java.util.UUID.randomUUID().toString();
		this.timestamp = new Date();
		this.email = email;
		this.token = token;
		this.device = device;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
