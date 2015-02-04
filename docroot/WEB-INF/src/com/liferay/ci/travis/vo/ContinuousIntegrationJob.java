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
		String account, String jobName, String alias, int lastBuildStatus) {

		this.account = account;
		this.jobAlias = alias;
		this.jobName = jobName;
		this.lastBuildStatus = lastBuildStatus;
	}

	@Override
	public int compareTo(ContinuousIntegrationJob that) {
		if (this.lastBuildStatus < that.lastBuildStatus) {
			return 1;
		}
		else if (this.lastBuildStatus == that.lastBuildStatus) {
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

	public int getLastBuildStatus() {
		return lastBuildStatus;
	}

	private String account;
	private String jobAlias;
	private String jobName;
	private int lastBuildStatus;

}