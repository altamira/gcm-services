package br.com.altamira.message.gcm.registration.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class DeviceInfo {

	private boolean isWebView; // true
	private boolean isIPad; // false
	private boolean isIOS; // false,
	private boolean isAndroid; // true,
	private boolean isWindowsPhone; // false,
	private String currentPlatform; // "android",
	private String currentPlatformVersion; // 5,
	private String deviceModel; // "GT-I9295",
	private String deviceUUID; // "cf3d46c000659b53"
	private Device device;

	public DeviceInfo() {
		// TODO Auto-generated constructor stub
	}

	public DeviceInfo(boolean isWebView, boolean isIPad, boolean isIOS,
			boolean isAndroid, boolean isWindowsPhone, String currentPlatform,
			String currentPlatformVersion, String deviceModel,
			String deviceUUID, Device device) {
		super();
		this.isWebView = isWebView;
		this.isIPad = isIPad;
		this.isIOS = isIOS;
		this.isAndroid = isAndroid;
		this.isWindowsPhone = isWindowsPhone;
		this.currentPlatform = currentPlatform;
		this.currentPlatformVersion = currentPlatformVersion;
		this.deviceModel = deviceModel;
		this.deviceUUID = deviceUUID;
		this.device = device;
	}

	public boolean isWebView() {
		return isWebView;
	}

	public void setWebView(boolean isWebView) {
		this.isWebView = isWebView;
	}

	public boolean isIPad() {
		return isIPad;
	}

	public void setIPad(boolean isIPad) {
		this.isIPad = isIPad;
	}

	public boolean isIOS() {
		return isIOS;
	}

	public void setIOS(boolean isIOS) {
		this.isIOS = isIOS;
	}

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	public boolean isWindowsPhone() {
		return isWindowsPhone;
	}

	public void setWindowsPhone(boolean isWindowsPhone) {
		this.isWindowsPhone = isWindowsPhone;
	}

	public String getCurrentPlatform() {
		return currentPlatform;
	}

	public void setCurrentPlatform(String currentPlatform) {
		this.currentPlatform = currentPlatform;
	}

	public String getCurrentPlatformVersion() {
		return currentPlatformVersion;
	}

	public void setCurrentPlatformVersion(String currentPlatformVersion) {
		this.currentPlatformVersion = currentPlatformVersion;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceUUID() {
		return deviceUUID;
	}

	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
