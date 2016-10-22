package org.springframework.social.jira.api.impl;

import org.springframework.social.jira.api.SearchOperations;
import org.springframework.social.jira.api.SearchParameters;
import org.springframework.social.jira.api.SearchResults;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SearchTemplate extends AbstractJiraOperations implements SearchOperations {

	private final RestTemplate restTemplate;

	public SearchTemplate(String baseUrl, RestTemplate restTemplate) {
		super(baseUrl);
		this.restTemplate = restTemplate;
	}

	@Override
	public SearchResults search(SearchParameters searchParameters) {
		MultiValueMap<String, String> parameters = SearchParametersUtil.buildQueryParametersFromSearchParameters(searchParameters);
		return restTemplate.getForObject(buildUri("/rest/api/2/search", parameters), SearchResults.class);
	}
}
