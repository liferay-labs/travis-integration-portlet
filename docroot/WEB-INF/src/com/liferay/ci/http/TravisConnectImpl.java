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

package com.liferay.ci.http;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import com.liferay.ci.json.JSONReaderImpl;

/**
 *
 * @author Manuel de la Peña
 */
public class TravisConnectImpl extends BaseConnectImpl {

	public TravisConnectImpl() throws IOException {
		super();
	}

	public JSONArray getJob(String account, String jobName)
		throws IOException, JSONException {

		return _get("/repositories/" + account + "/" + jobName + "/builds");
	}

	public void setAuthConnectionParams(AuthConnectionParams connectionParams) {
		_connectionParams = connectionParams;
	}

	private JSONArray _get(String apiURL) throws IOException, JSONException {
		return _get(apiURL, true);
	}

	private JSONArray _get(String apiURL, boolean appendBaseURL)
		throws IOException, JSONException {

		return JSONReaderImpl.readJSONFromURL(
			connect(_connectionParams, apiURL, appendBaseURL));
	}

}