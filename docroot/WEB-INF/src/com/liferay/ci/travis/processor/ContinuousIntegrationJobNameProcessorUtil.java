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

import java.util.HashMap;
import java.util.Map;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.util.PortletPropsUtil;

/**
 * @author Manuel de la Peña
 */
public class ContinuousIntegrationJobNameProcessorUtil {

	public static ContinuousIntegrationJobNameProcessor getProcessor()
		throws Exception {

		if (_processor == null) {
			_initialize();
		}

		return _processor;
	}

	public static String process(String jobName) throws Exception {
		if (!_initialized) {
			_initialize();
		}

		if (!_processedJobNames.containsKey(jobName)) {
			String processedJobName = getProcessor().process(jobName);

			_processedJobNames.put(jobName, processedJobName);
		}

		return _processedJobNames.get(jobName);
	}

	public static void setProcessor(
		ContinuousIntegrationJobNameProcessor processor) {

		_processor = processor;
	}

	private ContinuousIntegrationJobNameProcessorUtil() {
	}

	private static void _initialize() throws Exception {
		_processedJobNames = new HashMap<String, String>();

		String processorClassName =
			PortletPropsUtil.get(
				PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		ClassLoader classLoader =
			ContinuousIntegrationJobNameProcessorUtil.class.getClassLoader();

		Class<?> clazz = classLoader.loadClass(processorClassName);

		_processor =
			(AbstractContinuousIntegrationJobNameProcessor)clazz.newInstance();

		_initialized = true;
	}

	private static Map<String, String> _processedJobNames;

	private static ContinuousIntegrationJobNameProcessor _processor;

	private static boolean _initialized;

}