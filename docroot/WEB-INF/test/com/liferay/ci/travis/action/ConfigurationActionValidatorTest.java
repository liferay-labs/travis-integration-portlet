package com.liferay.ci.travis.action;

import static org.fest.assertions.Assertions.assertThat;

import javax.portlet.PortletPreferences;

import com.liferay.ci.portlet.TravisIntegrationConstants;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Manuel de la Peña
 */
public class ConfigurationActionValidatorTest {

	@Test
	public void testIsConfiguredWithJobsStackViewModeSet() throws Exception {
		PortletPreferences prefs = Mockito.mock(PortletPreferences.class);

		mockPreference(
			prefs, "viewmode",
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_JOBS_STACK),
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS));

		mockPreference(prefs, "jobnames", "myjobname", null);

		assertThat(ConfigurationValidator.isConfigured(prefs)).isTrue();
	}

	@Test
	public void testIsConfiguredWithJobsStackViewModeSetWithoutJobnames()
		throws Exception {

		PortletPreferences prefs = Mockito.mock(PortletPreferences.class);

		mockPreference(
			prefs, "viewmode",
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_JOBS_STACK),
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS));

		assertThat(ConfigurationValidator.isConfigured(prefs)).isFalse();
	}

	@Test
	public void testIsConfiguredWithTrafficLightsViewModeSet() throws Exception {
		PortletPreferences prefs = Mockito.mock(PortletPreferences.class);

		mockPreference(
			prefs, "viewmode",
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS),
			String.valueOf(
				TravisIntegrationConstants.VIEW_MODE_TRAFFIC_LIGHTS));

		mockPreference(prefs, "jobnames", "myjobname", null);

		assertThat(ConfigurationValidator.isConfigured(prefs)).isFalse();

		mockPreference(prefs, "jobname", "myjobname", null);

		assertThat(ConfigurationValidator.isConfigured(prefs)).isTrue();
	}

	@Test
	public void testIsConfiguredWithoutViewModeSet() throws Exception {
		PortletPreferences prefs = Mockito.mock(PortletPreferences.class);

		mockPreference(prefs, "jobname", "myjobname", null);

		assertThat(ConfigurationValidator.isConfigured(prefs)).isTrue();
	}

	@Test
	public void testIsConfiguredWithoutViewModeAndJobNameSet() {
		PortletPreferences prefs = Mockito.mock(PortletPreferences.class);

		assertThat(ConfigurationValidator.isConfigured(prefs)).isFalse();
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
}