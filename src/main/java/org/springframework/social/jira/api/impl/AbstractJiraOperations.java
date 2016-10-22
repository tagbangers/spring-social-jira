package org.springframework.social.jira.api.impl;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class AbstractJiraOperations {

	private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();

	private String baseUrl;

	public AbstractJiraOperations(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected URI buildUri(String path) {
		return buildUri(path, EMPTY_PARAMETERS);
	}

	protected URI buildUri(String path, String parameterName, String parameterValue) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.set(parameterName, parameterValue);
		return buildUri(path, parameters);
	}

	protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
		return UriComponentsBuilder
				.fromUriString(baseUrl)
				.path(path)
				.queryParams(parameters)
				.build().toUri();
	}
}
