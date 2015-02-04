package com.liferay.ci.travis.processor;

public interface ContinuousIntegrationJobNameProcessor {

	public String process(String jobName);

}