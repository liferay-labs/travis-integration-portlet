<%--
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
--%>

<%@ include file="/html/init.jsp" %>

<%
String buildsNumber = GetterUtil.getString(portletPreferences.getValue("buildsnumber", null));
String travisAccount = GetterUtil.getString(portletPreferences.getValue("account", null));
String jobName = GetterUtil.getString(portletPreferences.getValue("jobname", null));
String baseApiURL = GetterUtil.getString(portletPreferences.getValue("baseapiurl", null));

int viewMode = GetterUtil.getInteger(portletPreferences.getValue("viewmode", String.valueOf(TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS)));

long timeout = GetterUtil.getLong(portletPreferences.getValue("timeout", String.valueOf(TravisIntegrationConstants.DEFAULT_TIMEOUT)));

String jobNames = GetterUtil.getString(portletPreferences.getValue("jobnames", null));
%>

<liferay-portlet:actionURL portletConfiguration="true" var="actionURL" />

<aui:form action="<%= actionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<aui:fieldset label="builds-view-configuration">
		<div class="alert alert-info">
			Travis-CI API URL is set to <%= baseApiURL %>
		</div>

		<aui:input cssClass="lfr-input-text-container" label="job-name" name="preferences--account--" type="text" value="<%= travisAccount %>" />

		<aui:input cssClass="lfr-input-text-container" label="job-name" name="preferences--jobname--" type="text" value="<%= jobName %>" />

		<aui:input cssClass="lfr-input-text-container" label="reload-timeout" name="preferences--timeout--" type="text" value="<%= timeout %>" />

		<aui:select id="preferences--viewmode--" label="view-mode" name="preferences--viewmode--">
			<aui:option selected='<%=(viewMode == TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS)%>' label="traffic-lights-view-mode" value="<%=TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS%>" />
			<aui:option selected='<%=(viewMode == TravisIntegrationConstants.VIEW_MODE_JOBS_STACK)%>' label="jobs-stack-view-mode" value="<%=TravisIntegrationConstants.VIEW_MODE_JOBS_STACK%>" />
		</aui:select>

        <div class="alert alert-info">
			<span class="displaying-help-message-holder">
				<liferay-ui:message key="you-can-define-here-job-names-using-an-alias" />
			</span>
		</div>

		<aui:panel label="jobs-stack-view-mode">
			<aui:input id="preferences--jobnames--" label="job-names" name="preferences--jobnames--" resizable="true" type="textarea" value="<%= jobNames %>" />
		</aui:panel>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>