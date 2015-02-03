package com.liferay.ci.travis.processor;

public interface JenkinsJobNameProcessor {

	public String process(String jobName);

}