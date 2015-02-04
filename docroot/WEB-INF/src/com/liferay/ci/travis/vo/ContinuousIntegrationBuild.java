/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

/**
 * @author Cristina González
 */
public class ContinuousIntegrationBuild {

	public ContinuousIntegrationBuild(int number) {
		_number = number;
	}

	public int getNumber() {
		return _number;
	}

	public int getStatus() {
		return _status;
	}

	public void setNumber(int number) {
		_number = number;
	}

	public void setStatus(int status) {
		_status = status;
	}

	private int _number;
	private int _status;

}