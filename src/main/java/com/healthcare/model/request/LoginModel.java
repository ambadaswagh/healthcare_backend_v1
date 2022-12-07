package com.healthcare.model.request;

public class LoginModel {

	private String username;
	private String password;
	private String deviceId;
	private String objectId;
	private String deviceToken;
	private String mobileOS;
	private String appLanguage;

	public String getAppLanguage() {
		return appLanguage;
	}

	public void setAppLanguage(String appLanguage) {
		this.appLanguage = appLanguage;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getMobileOS() {
		return mobileOS;
	}

	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "LoginModel{" + "username='" + username + '\'' + ", deviceId='" + deviceId + '\'' + ", objectId='"
				+ objectId + '\'' + ", deviceToken='" + deviceToken + '\'' + ", mobileOS='" + mobileOS + '\''
				+ ", appLanguage='" + appLanguage + '\'' + '}';
	}
}
