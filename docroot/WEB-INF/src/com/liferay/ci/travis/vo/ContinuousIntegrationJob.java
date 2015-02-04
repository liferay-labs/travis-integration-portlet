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

import com.liferay.ci.travis.processor.ContinuousIntegrationJobNameProcessorUtil;
import com.liferay.ci.portlet.TravisIntegrationConstants;

/**
 * @author Manuel de la Peña
 */
public class ContinuousIntegrationJob
	implements Comparable<ContinuousIntegrationJob>{

	public ContinuousIntegrationJob(
		String account, String jobName, String alias, String lastBuildStatus) {

		this.account = account;
		this.jobAlias = alias;
		this.jobName = jobName;
		this.lastBuildStatus = lastBuildStatus;

		if (lastBuildStatus.equals(
			TravisIntegrationConstants.JENKINS_BUILD_STATUS_SUCCESS)) {

			internalLastBuildStatus = 0;
		}
		else if (lastBuildStatus.equals(
			TravisIntegrationConstants.JENKINS_BUILD_STATUS_UNSTABLE)) {

			internalLastBuildStatus = -2;
		}
		else if (lastBuildStatus.equals(
			TravisIntegrationConstants.JENKINS_BUILD_STATUS_ABORTED)) {

			internalLastBuildStatus = -3;
		}
		else {
			internalLastBuildStatus = -4;
		}
	}

	@Override
	public int compareTo(ContinuousIntegrationJob that) {
		if (this.internalLastBuildStatus > that.internalLastBuildStatus) {
			return 1;
		}
		else if (this.internalLastBuildStatus == that.internalLastBuildStatus) {
			if (this.jobAlias == null || that.jobAlias == null) {
				return (this.jobName.compareTo(that.jobName));
			}
			else {
				return (this.jobAlias.compareTo(that.jobAlias));
			}
		}
		else {
			return -1;
		}
	}

	public String getAccount() {
		return account;
	}

	public String getJobAlias() {
		return jobAlias;
	}

	public String getJobName() {
		try {
			return ContinuousIntegrationJobNameProcessorUtil.process(jobName);
		}
		catch (Exception e) {
			return jobName;
		}
	}

	public String getLastBuildStatus() {
		return lastBuildStatus;
	}

	private String account;
	private int internalLastBuildStatus;
	private String jobAlias;
	private String jobName;
	private String lastBuildStatus;

}