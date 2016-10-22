package org.springframework.social.jira.api.impl;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.social.jira.api.Jira;
import org.springframework.social.jira.api.MyselfOperations;
import org.springframework.social.jira.api.ProjectOperations;
import org.springframework.social.jira.api.SearchOperations;
import org.springframework.social.jira.connect.JiraOAuth1RequestInterceptor;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

public class JiraTemplate extends AbstractOAuth1ApiBinding implements Jira {

	private final String baseUrl;

	private final RestTemplate restTemplate;

	private MyselfOperations myselfOperations;

	private SearchOperations searchOperations;

	private ProjectOperations projectOperations;

	public JiraTemplate(String baseUrl, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		this.baseUrl = baseUrl;
		this.restTemplate = createRestTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		configureRestTemplate(this.restTemplate);
		initSubApis();
	}

	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	@Override
	public MyselfOperations myselfOperations() {
		return myselfOperations;
	}

	@Override
	public SearchOperations searchOperations() {
		return searchOperations;
	}

	@Override
	public ProjectOperations projectOperations() {
		return projectOperations;
	}

	@Override
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Override
	public boolean isAuthorized() {
		return false;
	}

	private RestTemplate createRestTemplate(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		RestTemplate client = createRestTemplateWithCulledMessageConverters();
		JiraOAuth1RequestInterceptor interceptor = new JiraOAuth1RequestInterceptor(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		List<ClientHttpRequestInterceptor> interceptors = new LinkedList<ClientHttpRequestInterceptor>();
		interceptors.add(interceptor);
		client.setInterceptors(interceptors);
		return client;
	}

	// Temporary: The RestTemplate that accepts a list of message converters wasn't added until Spring 3.2.7.
	//            Remove this method and use that constructor exclusively when 3.1.x support is no longer necessary (Spring Social 2.0).
	private RestTemplate createRestTemplateWithCulledMessageConverters() {
		RestTemplate client;
		List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
		try {
			client = new RestTemplate(messageConverters);
		} catch (NoSuchMethodError e) {
			client = new RestTemplate();
			client.setMessageConverters(messageConverters);
		}
		client.setRequestFactory(ClientHttpRequestFactorySelector.getRequestFactory());
		return client;
	}

	private void initSubApis() {
		this.myselfOperations = new MyselfTemplate(baseUrl, getRestTemplate());
		this.searchOperations = new SearchTemplate(baseUrl, getRestTemplate());
		this.projectOperations = new ProjectTemplate(baseUrl, getRestTemplate());
	}
}
