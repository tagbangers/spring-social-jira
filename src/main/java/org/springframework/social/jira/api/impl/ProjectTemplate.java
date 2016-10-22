package org.springframework.social.jira.api.impl;

import org.springframework.social.jira.api.Project;
import org.springframework.social.jira.api.ProjectOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ProjectTemplate extends AbstractJiraOperations implements ProjectOperations {

	private final RestTemplate restTemplate;

	public ProjectTemplate(String baseUrl, RestTemplate restTemplate) {
		super(baseUrl);
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Project> getAllProjects() {
		Project[] projects = restTemplate.getForObject(buildUri("/rest/api/2/project"), Project[].class);
		return Arrays.asList(projects);
	}
}
