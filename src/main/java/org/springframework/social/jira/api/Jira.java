package org.springframework.social.jira.api;

import org.springframework.social.ApiBinding;

public interface Jira extends ApiBinding {

	MyselfOperations myselfOperations();

	SearchOperations searchOperations();

	ProjectOperations projectOperations();
}
