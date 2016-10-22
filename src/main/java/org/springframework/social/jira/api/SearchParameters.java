package org.springframework.social.jira.api;

public class SearchParameters {

	private String jql;

	private int maxResults;

	public SearchParameters(String jql) {
		this.jql = jql;
	}

	public SearchParameters maxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public String getJql() {
		return jql;
	}

	public int getMaxResults() {
		return maxResults;
	}
}
