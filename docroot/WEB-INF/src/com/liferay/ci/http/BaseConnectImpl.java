/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ci.http;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author Manuel de la Peña
 */
public abstract class BaseConnectImpl {

	public BaseConnectImpl() throws IOException {
		super();

		String baseApiURL = PortletPropsUtil.get(
			PortletPropsKeys.TRAVIS_BASE_API_URL);

		_connectionParams = new AuthConnectionParams(baseApiURL);
	}

	protected InputStream connect(
			AuthConnectionParams authParams, String urlSuffix,
			boolean appendUrlPrefix)
		throws IOException {

		if (authParams != null) {
			_connectionParams = authParams;
		}

		String connectionURL = "";

		if (appendUrlPrefix) {
			connectionURL += authParams.getBaseApiUrl();
		}

		connectionURL += urlSuffix;

		_skipSSLCertificationValidation();

		URL url = new URL(connectionURL);

		HttpsURLConnection uc = (HttpsURLConnection)url.openConnection();

		uc.setRequestMethod("GET");
		uc.setRequestProperty("Content-length", "0");
		uc.setAllowUserInteraction(false);
		uc.setRequestProperty("Accept", "application/json");

		return uc.getInputStream();
	}

	/*
	 * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
	 * sun.security.validator.ValidatorException:
	 * PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
	 * unable to find valid certification path to requested target
	 */
	private void _skipSSLCertificationValidation() {
		TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

				public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");

			sc.init(null, trustAllCerts, new java.security.SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	protected AuthConnectionParams _connectionParams;

}