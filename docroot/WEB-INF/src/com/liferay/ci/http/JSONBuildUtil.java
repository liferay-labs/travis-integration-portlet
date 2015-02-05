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
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.liferay.ci.travis.vo.ContinuousIntegrationBuild;
import com.liferay.ci.travis.vo.ContinuousIntegrationJob;
import com.liferay.ci.travis.vo.ContinuousIntegrationUnstableJob;
import com.liferay.ci.portlet.TravisIntegrationConstants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 *
 * @author Manuel de la Peña
 */
public class JSONBuildUtil {

	public static JSONArray getBuilds(
			AuthConnectionParams connectionParams, String account,
			String jobName, int maxNumber)
		throws IOException, JSONException {

		JSONArray builds = _getJob(connectionParams, account, jobName);

		JSONArray result = new JSONArray();

		int end = builds.length();

		if ((maxNumber > 0) && (maxNumber < end)) {
			end = maxNumber;
		}

		for (int i = 0; i < end; i++) {
			JSONObject build = (JSONObject)builds.get(i);

			result.put(build);
		}

		return result;
	}

	public static ContinuousIntegrationBuild getLastBuild(
			AuthConnectionParams connectionParams, String account,
			String jobName)
		throws IOException, JSONException {

		JSONArray builds = _getJob(connectionParams, account, jobName);

		// last build

		JSONObject lastBuild = (JSONObject)builds.get(0);

		ContinuousIntegrationBuild result = new ContinuousIntegrationBuild(
			lastBuild.getInt("id"));

		result.setStatus(lastBuild.getInt("result"));

		return result;
	}

	public static ContinuousIntegrationJob[] getLastBuilds(
			AuthConnectionParams connectionParams,
			ContinuousIntegrationJob... jobs)
		throws IOException, JSONException {

		ContinuousIntegrationJob[] result =
			new ContinuousIntegrationJob[jobs.length];

		System.arraycopy(jobs, 0, result, 0, jobs.length);

		for (int i = 0; i < result.length; i++) {
			String jobAccount = result[i].getAccount();
			String jobName = result[i].getRealJobName();
			String jobAlias = result[i].getJobAlias();

			ContinuousIntegrationBuild lastBuild =
				getLastBuild(connectionParams, jobAccount, jobName);

			if (lastBuild.getStatus() ==
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED) {

				result[i] = new ContinuousIntegrationUnstableJob(
					jobAccount, jobName, jobAlias, lastBuild.getStatus());
			}
			else {
				result[i] = new ContinuousIntegrationJob(
					jobAccount, jobName, jobAlias, lastBuild.getStatus());
			}
		}

		// sort jobs by status

		Arrays.sort(result);

		return result;
	}

	private JSONBuildUtil() {
	}

	private static JSONArray _getJob(
		AuthConnectionParams connectionParams, String account, String jobName)
		throws IOException, JSONException {

		return _getService(connectionParams).getJob(account, jobName);
	}

	private static TravisConnectImpl _getService(
		AuthConnectionParams connectionParams)
		throws IOException {

		if (_service == null) {
			_service = new TravisConnectImpl();
		}

		_service.setAuthConnectionParams(connectionParams);

		return _service;
	}

	private static TravisConnectImpl _service;
	private static Log _log = LogFactoryUtil.getLog(JSONBuildUtil.class);

}