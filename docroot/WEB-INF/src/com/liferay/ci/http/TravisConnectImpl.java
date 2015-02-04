/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

import com.liferay.ci.travis.vo.ContinuousIntegrationBuild;
import com.liferay.ci.portlet.TravisIntegrationConstants;

import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.liferay.ci.json.JSONReaderImpl;

/**
 *
 * @author Manuel de la Peña
 */
public class TravisConnectImpl extends BaseConnectImpl {

	public TravisConnectImpl() throws IOException {
		super();
	}

	public JSONArray getBuildTestReport(JSONObject build)
		throws IOException, JSONException {

		String buildURL = (String)build.get("url");

		return getBuildTestReport(buildURL);
	}

	public JSONArray getBuildTestReport(String url)
		throws IOException, JSONException {

		return _get(url + "testReport/", false);
	}

	public ContinuousIntegrationBuild getLastBuild(JSONObject build)
		throws IOException, JSONException {

		String buildURL = (String)build.get("url");

		int buildNumber = build.getInt("number");

		ContinuousIntegrationBuild continuousIntegrationBuild =
			new ContinuousIntegrationBuild(buildNumber, new URL(buildURL));

		JSONArray buildResult = _get(buildURL, false);

		Object result = buildResult.get(0);

		String resultString = String.valueOf(result);

		if (resultString.equals(
				TravisIntegrationConstants.JENKINS_BUILD_STATUS_UNSTABLE)) {

			JSONArray buildTestReport = getBuildTestReport(buildURL);

			// retrieve number of broken tests for last build

			int failedTests = buildTestReport.getInt(0);

			continuousIntegrationBuild.setFailedTests(failedTests);
		}

		continuousIntegrationBuild.setNumber(buildNumber);
		continuousIntegrationBuild.setStatus(Integer.valueOf(resultString));
		continuousIntegrationBuild.setUrl(new URL(buildURL));

		return continuousIntegrationBuild;
	}

	public JSONArray getJob(String account, String jobName)
		throws IOException, JSONException {

		return _get("/repositories/" + account + "/" + jobName + "/builds");
	}

	public void setAuthConnectionParams(AuthConnectionParams connectionParams) {
		_connectionParams = connectionParams;
	}

	private JSONArray _get(String apiURL) throws IOException, JSONException {
		return _get(apiURL, true);
	}

	private JSONArray _get(String apiURL, boolean appendBaseURL)
		throws IOException, JSONException {

		return JSONReaderImpl.readJSONFromURL(
			connect(_connectionParams, apiURL, appendBaseURL));
	}

}