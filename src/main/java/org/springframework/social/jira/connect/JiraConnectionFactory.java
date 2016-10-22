package org.springframework.social.jira.connect;

import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.jira.api.Jira;

public class JiraConnectionFactory extends OAuth1ConnectionFactory<Jira> {

	private JiraServiceProvider jiraServiceProvider;

	public JiraConnectionFactory(String consumerKey, String consumerSecret, String baseUrl, String callback) {
		super("jira", new JiraServiceProvider(consumerKey, consumerSecret, baseUrl, callback), new JiraAdapter());
		this.jiraServiceProvider = (JiraServiceProvider) getServiceProvider();
	}

	public JiraServiceProvider getJiraServiceProvider() {
		return jiraServiceProvider;
	}
}
