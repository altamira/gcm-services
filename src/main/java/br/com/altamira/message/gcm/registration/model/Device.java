package br.com.altamira.message.gcm.registration.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 399075383711244323L;
	
	private boolean available; // true
	private String platform; // Android
	private String version; // "5.0.1"
	private String uuid; // "cf3d46c000659b53"
	private String cordova; // "4.0.2"
	private String model; // "GT-I9295"
	private String manufacturer; // "samsung"

	public Device() {
		// TODO Auto-generated constructor stub
	}

	public Device(boolean available, String platform, String version,
			String uuid, String cordova, String model, String manufacturer) {
		super();
		this.available = available;
		this.platform = platform;
		this.version = version;
		this.uuid = uuid;
		this.cordova = cordova;
		this.model = model;
		this.manufacturer = manufacturer;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCordova() {
		return cordova;
	}

	public void setCordova(String cordova) {
		this.cordova = cordova;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

}
