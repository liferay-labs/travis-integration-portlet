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

public class TravisIntegrationConstants {

	public static final long DEFAULT_TIMEOUT = 60000;

	public static final String TRAVIS_BUILD_STATUS_FAILURE = "FAILURE";

	public static final String BUILD_STATUS_SUCCESS = "SUCCESS";

	public static final String TRAVIS_JOBS = "TRAVIS_JOBS";

	public static final int TRAVIS_BUILD_STATUS_PENDING = -1;

	public static final int TRAVIS_BUILD_STATUS_FAILED = 1;

	public static final int TRAVIS_BUILD_STATUS_SUCCESS = 0;

	public static final int VIEW_MODE_JOBS_STACK = 3;

	public static final int VIEW_MODE_TRAFFIC_LIGHTS = 2;

}