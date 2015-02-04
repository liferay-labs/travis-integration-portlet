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

package com.liferay.ci.travis.vo;

public class ContinuousIntegrationUnstableJob extends ContinuousIntegrationJob {

	public ContinuousIntegrationUnstableJob(
		String account, String jobName, String alias, int lastBuildStatus) {

		this(account, jobName, alias, lastBuildStatus, 0);
	}

	public ContinuousIntegrationUnstableJob(
		String account, String jobName, String alias, int lastBuildStatus,
		int failedTestCount) {

		super(account, jobName, alias, lastBuildStatus);

		this.failedTestCount = failedTestCount;
	}

	public int getFailedTestCount() {
		return failedTestCount;
	}

	private int failedTestCount;

}