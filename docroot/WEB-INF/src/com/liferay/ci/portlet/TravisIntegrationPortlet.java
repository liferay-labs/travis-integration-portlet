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

package com.liferay.ci.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.travis.vo.ContinuousIntegrationBuild;
import org.json.JSONException;

import com.liferay.ci.http.AuthConnectionParams;
import com.liferay.ci.http.JSONBuildUtil;
import com.liferay.ci.travis.action.ConfigurationValidator;
import com.liferay.ci.travis.cache.LiferayJenkinsBuildCache;
import com.liferay.ci.travis.vo.ContinuousIntegrationJob;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * 
 * @author Manuel de la Peña
 */
public class TravisIntegrationPortlet extends MVCPortlet {

	@Override
	public void init() throws PortletException {
		super.init();

		_cache = new LiferayJenkinsBuildCache();
	}

	public static LiferayJenkinsBuildCache getCache() {
		return _cache;
	}

	@Override
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {

		PortletPreferences portletPreferences = request.getPreferences();

		boolean configured = ConfigurationValidator.isConfigured(
			portletPreferences);

		if (configured) {
			int viewMode = GetterUtil.getInteger(
				portletPreferences.getValue("viewmode", null),
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS);

			if (viewMode ==
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS) {

				buildLights(request);
			}
			else if (viewMode ==
				TravisIntegrationConstants.VIEW_MODE_JOBS_STACK) {

				buildProjectsStack(request);
			}
		}

		super.render(request, response);
	}

	protected void buildLights(RenderRequest request) {
		PortletPreferences portletPreferences = request.getPreferences();

		String account = portletPreferences.getValue(
			"account", StringPool.BLANK);

		String jobName = portletPreferences.getValue(
			"jobname", StringPool.BLANK);

		_log.debug("Getting builds for " + jobName);

		AuthConnectionParams connectionParams = getConnectionParams();

		try {
			ContinuousIntegrationBuild lastBuild = JSONBuildUtil.getLastBuild(
				connectionParams, account, jobName);

			request.setAttribute(
				"LAST_BUILD_STATUS", lastBuild.getStatus());

			if (lastBuild.getStatus() ==
					TravisIntegrationConstants.TRAVIS_BUILD_STATUS_FAILED) {

				// retrieve number of broken tests for last build

				request.setAttribute("TEST_RESULTS", lastBuild);
			}
		}
		catch (IOException ioe) {
			SessionErrors.add(request, ioe.getClass());

			_log.error("The job was not available", ioe);
		}
		catch (JSONException e) {
			_log.error("The job is not well-formed", e);
		}
	}

	protected void buildProjectsStack(RenderRequest request) {
		PortletPreferences portletPreferences = request.getPreferences();

		String jobNamesParam = portletPreferences.getValue(
			"jobnames", StringPool.BLANK);

		AuthConnectionParams connectionParams = getConnectionParams();

		try {
			ContinuousIntegrationJob[] jobs = parseJobNames(jobNamesParam);

			ContinuousIntegrationJob[] lastBuilds =
				JSONBuildUtil.getLastBuilds(connectionParams, jobs);

			request.setAttribute(
				TravisIntegrationConstants.TRAVIS_JOBS, lastBuilds);
		}
		catch (IOException ioe) {
			SessionErrors.add(request, ioe.getClass());

			_log.error("The jobs were not available", ioe);
		}
		catch (JSONException e) {
			_log.error("The jobs are not well-formed", e);
		}
	}

	protected AuthConnectionParams getConnectionParams() {
		String url = PortletPropsUtil.get(PortletPropsKeys.TRAVIS_BASE_API_URL);

		return new AuthConnectionParams(url);
	}

	protected ContinuousIntegrationJob[] parseJobNames(String jobNamesParam) {
		String[] jobNames = StringUtil.split(jobNamesParam, StringPool.NEW_LINE);

		ContinuousIntegrationJob[] jobs =
			new ContinuousIntegrationJob[jobNames.length];

		for (int i = 0; i < jobNames.length; i++) {
			String fullJobName = jobNames[i];

			String[] jobNameArray = fullJobName.split("\\|");

			String jobAccount;
			String jobName;
			String jobAlias;

			if (jobNameArray.length > 3) {
				_log.warn("Job name uses invalid format: " + fullJobName);

				continue;
			} else if (jobNameArray.length == 3) {
				jobAccount = jobNameArray[0];
				jobName = jobNameArray[1];
				jobAlias = jobNameArray[2];
			} else {
				jobAccount = fullJobName;
				jobName = fullJobName;
				jobAlias = fullJobName;
			}

			jobs[i] = new ContinuousIntegrationJob(
				jobAccount, jobName, jobAlias,
				TravisIntegrationConstants.TRAVIS_BUILD_STATUS_PENDING);
		}

		return jobs;
	}

	private static LiferayJenkinsBuildCache _cache;

	private static Log _log = LogFactoryUtil.getLog(
		TravisIntegrationPortlet.class);

}