package org.springframework.social.jira.api;

import org.springframework.social.ApiBinding;
import org.springframework.web.client.RestTemplate;

public interface Jira extends ApiBinding {

	String getBaseUrl();

	RestTemplate getRestTemplate();

	MyselfOperations myselfOperations();

	SearchOperations searchOperations();

	ProjectOperations projectOperations();
}
