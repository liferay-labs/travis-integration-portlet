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

package com.liferay.ci.travis.processor;

import static org.fest.assertions.Assertions.assertThat;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.util.TestPropsUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Manuel de la Peña
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PortletPropsUtil.class})
public class ContinuousIntegrationJobNameProcessorUtilTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		ContinuousIntegrationJobNameProcessorUtil.setProcessor(
			new DefaultJobNameProcessorImpl());
	}

	@Test
	public void testProcess() throws Exception {
		String actualJobName =
			ContinuousIntegrationJobNameProcessorUtil.process(
				"jobnamewitnodashes");

		assertThat(actualJobName).isEqualTo("jobnamewitnodashes");
	}

	@Test
	public void testProcessWithDashes() throws Exception {
		String actualJobName =
			ContinuousIntegrationJobNameProcessorUtil.process(
				"job-name-with-dashes");

		assertThat(actualJobName).isEqualTo("job name with dashes");
	}

	@Test
	public void testProcessWithDashesAndLiferay() throws Exception {
		String actualJobName =
			ContinuousIntegrationJobNameProcessorUtil.process(
				"liferay-job-name-with-dashes");

		assertThat(actualJobName).isEqualTo("job name with dashes");
	}

	@Test
	public void testProcessWithLiferay() throws Exception {
		String actualJobName =
			ContinuousIntegrationJobNameProcessorUtil.process(
				"liferayjobnamewithnodashes");

		assertThat(actualJobName).isEqualTo("jobnamewithnodashes");
	}

}