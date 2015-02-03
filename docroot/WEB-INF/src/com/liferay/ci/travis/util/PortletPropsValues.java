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

package com.liferay.ci.travis.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

/**
 * @author Manuel de la Peña
 */
public class PortletPropsValues {

	public static final String TRAVIS_BASE_API_URL =
		GetterUtil.getString(PortletProps.get(
			PortletPropsKeys.TRAVIS_BASE_API_URL));

	public static final String JOB_NAME_PROCESSOR_CLASSNAME =
		GetterUtil.getString(
			PortletProps.get(PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME));

}