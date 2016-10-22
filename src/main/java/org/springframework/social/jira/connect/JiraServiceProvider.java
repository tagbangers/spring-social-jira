package org.springframework.social.jira.connect;

import org.springframework.social.jira.api.Jira;
import org.springframework.social.jira.api.impl.JiraTemplate;
import org.springframework.social.oauth1.AbstractOAuth1ServiceProvider;

public class JiraServiceProvider extends AbstractOAuth1ServiceProvider<Jira> {

	private String baseUrl;

	public JiraServiceProvider(String consumerKey, String privateKey, String baseUrl, String callback) {
		super(consumerKey, privateKey, new JiraOAuth1Template(
				consumerKey,
				privateKey,
				baseUrl,
				callback));
		this.baseUrl = baseUrl;
	}

	@Override
	public Jira getApi(String accessToken, String secret) {
		return new JiraTemplate(baseUrl, getConsumerKey(), getConsumerSecret(), accessToken, secret);
	}
}
