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

import java.io.IOException;
import java.net.URL;

import com.liferay.ci.travis.vo.ContinuousIntegrationBuild;
import com.liferay.ci.portlet.TravisIntegrationConstants;
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

	public JSONObject getBuildTestReport(JSONObject build)
		throws IOException, JSONException {

		String buildURL = (String)build.get("url");

		return getBuildTestReport(buildURL);
	}

	public JSONObject getBuildTestReport(String url)
		throws IOException, JSONException {

		return _get(url + "testReport/", false);
	}

	public ContinuousIntegrationBuild getLastBuild(JSONObject build)
		throws IOException, JSONException {

		String buildURL = (String)build.get("url");

		int buildNumber = build.getInt("number");

		ContinuousIntegrationBuild continuousIntegrationBuild =
			new ContinuousIntegrationBuild(buildNumber, new URL(buildURL));

		JSONObject buildResult = _get(buildURL, false);

		Object result = buildResult.get("result");

		String resultString = String.valueOf(result);

		if (resultString.equals(
				TravisIntegrationConstants.JENKINS_BUILD_STATUS_UNSTABLE)) {

			JSONObject buildTestReport = getBuildTestReport(buildURL);

			// retrieve number of broken tests for last build

			int failedTests = buildTestReport.getInt("failCount");

			continuousIntegrationBuild.setFailedTests(failedTests);
		}

		continuousIntegrationBuild.setNumber(buildNumber);
		continuousIntegrationBuild.setStatus(resultString);
		continuousIntegrationBuild.setUrl(new URL(buildURL));

		return continuousIntegrationBuild;
	}

	public JSONObject getJob(String jobName) throws IOException, JSONException {
		return _get(getJobAPIURLSuffix(jobName));
	}

	protected String getJobAPIURLSuffix(String jobName) {
		return "/job/" + jobName + "/";
	}

	public void setAuthConnectionParams(AuthConnectionParams connectionParams) {
		_connectionParams = connectionParams;
	}

	private JSONObject _get(String apiURL) throws IOException, JSONException {
		return _get(apiURL, true);
	}

	private JSONObject _get(String apiURL, boolean appendBaseURL)
		throws IOException, JSONException {

		return JSONReaderImpl.readJSONFromURL(
			connect(_connectionParams, apiURL, appendBaseURL));
	}

}