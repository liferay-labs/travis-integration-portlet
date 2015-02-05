package com.liferay.ci.portlet;

import static org.fest.assertions.Assertions.assertThat;

import com.liferay.ci.travis.util.PortletPropsKeys;
import com.liferay.ci.travis.util.PortletPropsUtil;
import com.liferay.ci.travis.vo.ContinuousIntegrationJob;

import com.liferay.ci.util.TestPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Manuel de la Peña
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PortletPropsUtil.class})
public class TravisIntegrationPortletTest {

	@BeforeClass
	public static void setUpClass() {
		PowerMockito.mockStatic(PortletPropsUtil.class);

		TestPropsUtil.mockPortletKey(PortletPropsKeys.TRAVIS_BASE_API_URL);
		TestPropsUtil.mockPortletKey(
			PortletPropsKeys.JOB_NAME_PROCESSOR_CLASSNAME);
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
		assertThat(job.getJobName()).isEqualTo("wonderful job name");
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
		assertThat(job1.getJobName()).isEqualTo("wonderful job name 1");

		ContinuousIntegrationJob job2 = jobs[1];

		assertThat(job2.getAccount()).isEqualTo("my-account-2");
		assertThat(job2.getJobAlias()).isEqualTo("wonderful-alias-2");
		assertThat(job2.getJobName()).isEqualTo("wonderful job name 2");
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
		assertThat(job.getJobName()).isEqualTo("wonderful job name");
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
		assertThat(job1.getJobName()).isEqualTo("wonderful job name 1");

		ContinuousIntegrationJob job2 = jobs[1];

		assertThat(job2.getAccount()).isEqualTo("wonderful-job-name-2");
		assertThat(job2.getJobAlias()).isEqualTo("wonderful-job-name-2");
		assertThat(job2.getJobName()).isEqualTo("wonderful job name 2");
	}

}