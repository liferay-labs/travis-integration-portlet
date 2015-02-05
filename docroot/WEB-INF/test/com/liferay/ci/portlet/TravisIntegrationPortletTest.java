package com.liferay.ci.portlet;

import static org.fest.assertions.Assertions.assertThat;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.travis.vo.ContinuousIntegrationJob;

import com.liferay.ci.util.TestPropsUtil;
import com.liferay.portal.kernel.util.StringPool;

import javax.portlet.PortletPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.springframework.mock.web.portlet.MockRenderRequest;

/**
 * @author Manuel de la Peña
 */
@PowerMockIgnore({"javax.net.ssl.*", "javax.security.auth.*", "sun.net.www.*"})
@PrepareForTest({PortletPropsUtil.class})
@RunWith(PowerMockRunner.class)
public class TravisIntegrationPortletTest {

	@Before
	public void setUp() {
		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);

		_request = new MockRenderRequest();

		_prefs = Mockito.mock(PortletPreferences.class);

		_request.setPreferences(_prefs);
	}

	@Test
	public void testBuildLights() throws Exception {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		mockPreference(_prefs, "account", "liferay", StringPool.BLANK);
		mockPreference(_prefs, "jobname", "alloy-editor", StringPool.BLANK);

		portlet.buildLights(_request);

		Object o = _request.getAttribute(
			TravisIntegrationConstants.LAST_BUILD_STATUS);

		assertThat(o).isNotNull();
	}

	@Test
	public void testBuildProjectsStackOneJob() throws Exception {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		mockPreference(
			_prefs, "jobnames", "liferay|alloy-editor|Alloy Editor",
			StringPool.BLANK);

		portlet.buildProjectsStack(_request);

		Object o = _request.getAttribute(
			TravisIntegrationConstants.TRAVIS_JOBS);

		assertThat(o).isNotNull();
		assertThat(o).isInstanceOf(ContinuousIntegrationJob[].class);

		ContinuousIntegrationJob[] jobs = (ContinuousIntegrationJob[])o;

		assertThat(jobs).hasSize(1);

		ContinuousIntegrationJob job = jobs[0];

		assertThat(job.getAccount()).isEqualTo("liferay");
		assertThat(job.getJobName()).isEqualTo("alloy-editor");
		assertThat(job.getRealJobName()).isEqualTo("alloy-editor");
		assertThat(job.getJobAlias()).isEqualTo("Alloy Editor");
	}

	@Test
	public void testBuildProjectsStackTwoJobs() throws Exception {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames =
			"liferay|alloy-editor|Alloy Editor 1" + StringPool.NEW_LINE +
			"liferay|alloy-editor|Alloy Editor 2";

		mockPreference(_prefs, "jobnames", jobNames, StringPool.BLANK);

		portlet.buildProjectsStack(_request);

		Object o = _request.getAttribute(
			TravisIntegrationConstants.TRAVIS_JOBS);

		assertThat(o).isNotNull();
		assertThat(o).isInstanceOf(ContinuousIntegrationJob[].class);

		ContinuousIntegrationJob[] jobs = (ContinuousIntegrationJob[])o;

		assertThat(jobs).hasSize(2);

		ContinuousIntegrationJob job1 = jobs[0];

		assertThat(job1.getAccount()).isEqualTo("liferay");
		assertThat(job1.getJobName()).isEqualTo("alloy-editor");
		assertThat(job1.getRealJobName()).isEqualTo("alloy-editor");
		assertThat(job1.getJobAlias()).isEqualTo("Alloy Editor 1");

		ContinuousIntegrationJob job2 = jobs[1];

		assertThat(job2.getAccount()).isEqualTo("liferay");
		assertThat(job2.getJobName()).isEqualTo("alloy-editor");
		assertThat(job2.getRealJobName()).isEqualTo("alloy-editor");
		assertThat(job2.getJobAlias()).isEqualTo("Alloy Editor 2");
	}

	@Test
	public void testParseJobNamesFromComplexJobName() {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames = "my-account|wonderful-job-name|wonderful-alias";

		ContinuousIntegrationJob[] jobs = portlet.parseJobNames(jobNames);

		assertThat(jobs).hasSize(1);

		ContinuousIntegrationJob job = jobs[0];

		assertThat(job.getAccount()).isEqualTo("my-account");
		assertThat(job.getJobAlias()).isEqualTo("wonderful-alias");
		assertThat(job.getJobName()).isEqualTo("wonderful-job-name");
	}

	@Test
	public void testParseJobNamesFromComplexJobNames() {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames =
			"my-account-1|wonderful-job-name-1|wonderful-alias-1" +
			StringPool.NEW_LINE +
			"my-account-2|wonderful-job-name-2|wonderful-alias-2";

		ContinuousIntegrationJob[] jobs = portlet.parseJobNames(jobNames);

		assertThat(jobs).hasSize(2);

		ContinuousIntegrationJob job1 = jobs[0];

		assertThat(job1.getAccount()).isEqualTo("my-account-1");
		assertThat(job1.getJobAlias()).isEqualTo("wonderful-alias-1");
		assertThat(job1.getJobName()).isEqualTo("wonderful-job-name-1");

		ContinuousIntegrationJob job2 = jobs[1];

		assertThat(job2.getAccount()).isEqualTo("my-account-2");
		assertThat(job2.getJobAlias()).isEqualTo("wonderful-alias-2");
		assertThat(job2.getJobName()).isEqualTo("wonderful-job-name-2");
	}

	@Test
	public void testParseJobNamesFromEmptyJobNamesIsEmpty() {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames = "";

		ContinuousIntegrationJob[] jobs = portlet.parseJobNames(jobNames);

		assertThat(jobs).isEmpty();
	}

	@Test
	public void testParseJobNamesFromSimpleJobName() {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames = "wonderful-job-name";

		ContinuousIntegrationJob[] jobs = portlet.parseJobNames(jobNames);

		assertThat(jobs).hasSize(1);

		ContinuousIntegrationJob job = jobs[0];

		assertThat(job.getAccount()).isEqualTo("wonderful-job-name");
		assertThat(job.getJobAlias()).isEqualTo("wonderful-job-name");
		assertThat(job.getJobName()).isEqualTo("wonderful-job-name");
	}

	@Test
	public void testParseJobNamesFromSimpleJobNames() {
		TravisIntegrationPortlet portlet = new TravisIntegrationPortlet();

		String jobNames =
			"wonderful-job-name-1" + StringPool.NEW_LINE +
			"wonderful-job-name-2";

		ContinuousIntegrationJob[] jobs = portlet.parseJobNames(jobNames);

		assertThat(jobs).hasSize(2);

		ContinuousIntegrationJob job1 = jobs[0];

		assertThat(job1.getAccount()).isEqualTo("wonderful-job-name-1");
		assertThat(job1.getJobAlias()).isEqualTo("wonderful-job-name-1");
		assertThat(job1.getJobName()).isEqualTo("wonderful-job-name-1");

		ContinuousIntegrationJob job2 = jobs[1];

		assertThat(job2.getAccount()).isEqualTo("wonderful-job-name-2");
		assertThat(job2.getJobAlias()).isEqualTo("wonderful-job-name-2");
		assertThat(job2.getJobName()).isEqualTo("wonderful-job-name-2");
	}

	protected void mockPreference(
		PortletPreferences prefs, String preference, String value,
		String defaultValue) {

		Mockito.when(
			prefs.getValue(preference, defaultValue)
		).thenReturn(
			value
		);
	}

	private PortletPreferences _prefs;
	private MockRenderRequest _request;

}