package org.springframework.social.jira.api.impl;

import org.springframework.social.jira.api.JiraUser;
import org.springframework.social.jira.api.MyselfOperations;
import org.springframework.web.client.RestTemplate;

public class MyselfTemplate extends AbstractJiraOperations implements MyselfOperations {

	private final RestTemplate restTemplate;

	public MyselfTemplate(String baseUrl, RestTemplate restTemplate) {
		super(baseUrl);
		this.restTemplate = restTemplate;
	}

	@Override
	public JiraUser getUser() {
		return restTemplate.getForObject(buildUri("/rest/api/2/myself"), JiraUser.class);
	}
}
