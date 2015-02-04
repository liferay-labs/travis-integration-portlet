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

package com.liferay.ci.util;

import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Cristina González
 */
public class TestPropsUtil {

	static {
		_loadProperties();
	}

	public static String getValue(String value) {
		return _properties.getProperty(value);
	}

	public static void mockPortletKey(String key) {
		PowerMockito.when(
			PortletPropsUtil.get(key)
		).thenReturn(
			TestPropsUtil.getValue(key)
		);
	}

	private static void _loadProperties() {
		_properties = new Properties();

		try {
			ClassLoader classLoader = TestPropsUtil.class.getClassLoader();

			_properties.load(
				classLoader.getResourceAsStream(_NAME_PROPERTIES_FILE));

			_properties.load(
				classLoader.getResourceAsStream(_NAME_EXT_PROPERTIES_FILE));
		}
		catch (IOException ex) {
			throw new RuntimeException("Can't load properties");
		}
	}

	private static final String _NAME_EXT_PROPERTIES_FILE =
		"portlet-ext.properties";

	private static final String _NAME_PROPERTIES_FILE = "portlet.properties";

	private static Properties _properties;

}