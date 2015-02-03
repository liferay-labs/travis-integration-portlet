package com.liferay.ci.http;

public class AuthConnectionParams {

	public AuthConnectionParams(String baseApiUrl) {
		super();

		this.baseApiUrl = baseApiUrl;
	}

	public String getBaseApiUrl() {
		return baseApiUrl;
	}

	public void setBaseApiUrl(String url) {
		this.baseApiUrl = url;
	}

	private String baseApiUrl;

}