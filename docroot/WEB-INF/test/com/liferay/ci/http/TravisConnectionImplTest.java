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

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.util.TestPropsUtil;

import java.io.IOException;

import org.json.JSONArray;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 
 * @author Manuel de la Peña
 */
@PowerMockIgnore({"javax.net.ssl.*", "javax.security.auth.*", "sun.net.www.*"})
@PrepareForTest({PortletPropsUtil.class})
@RunWith(PowerMockRunner.class)
public class TravisConnectionImplTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		TravisConnectImpl connectionImpl = new TravisConnectImpl();

		connectionParams = connectionImpl._connectionParams;
	}

	@Test
	public void testGetBuilds() throws Exception {
		String account = "liferay";
		String jobName = "alloy-editor";

		JSONArray testReports = JSONBuildUtil.getBuilds(
			connectionParams, account, jobName, 0);

		assertThat(testReports).isNotNull();
		assertThat(testReports.length()).isGreaterThan(0);
	}

	@Test(expected = IOException.class)
	public void testGetBuildsIOException() throws Exception {
		String baseApiUrl = connectionParams.getBaseApiUrl();

		connectionParams.setBaseApiUrl("https://fooblablablxxh");

		String account = "liferay";
		String jobName = "alloy-editor";

		try {
			JSONBuildUtil.getBuilds(connectionParams, account, jobName, 0);

			fail();
		}
		finally {
			connectionParams.setBaseApiUrl(baseApiUrl);
		}
	}

	@Test
	public void testGetBuildsMaxNumber() throws Exception {
		String account = "liferay";
		String jobName = "alloy-editor";

		JSONArray testReports = JSONBuildUtil.getBuilds(
			connectionParams, account, jobName, 100);

		assertThat(testReports).isNotNull();
		assertThat(testReports.length()).isGreaterThan(0);
	}

	@Test
	public void testGetBuildsMaxNumberNegative() throws Exception {
		String account = "liferay";
		String jobName = "alloy-editor";

		JSONArray testReports = JSONBuildUtil.getBuilds(
			connectionParams, account, jobName, -1);

		assertThat(testReports).isNotNull();
		assertThat(testReports.length()).isGreaterThan(0);
	}

	@Test
	public void testGetBuildsMaxNumberLessThan() throws Exception {
		String account = "liferay";
		String jobName = "alloy-editor";

		JSONArray testReports = JSONBuildUtil.getBuilds(
			connectionParams, account, jobName, 2);

		assertThat(testReports).isNotNull();
		assertThat(testReports.length()).isGreaterThan(0);
		assertThat(testReports.length()).isEqualTo(2);
	}

	private static AuthConnectionParams connectionParams;

}