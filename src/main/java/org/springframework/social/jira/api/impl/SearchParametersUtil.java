package org.springframework.social.jira.api.impl;

import org.springframework.social.jira.api.SearchParameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class SearchParametersUtil {

	public static final int DEFAULT_RESULTS_PER_PAGE = 50;

	public static MultiValueMap<String, String> buildQueryParametersFromSearchParameters(SearchParameters searchParameters) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.set("jql", searchParameters.getJql());
		if (searchParameters.getMaxResults() > 0) {
			parameters.set("maxResults", Integer.toString(searchParameters.getMaxResults()));
		}
//		if (searchParameters.getLang() != null) {
//			parameters.set("lang", searchParameters.getLang());
//		}
//		if (searchParameters.getLocale() != null) {
//			parameters.set("locale", searchParameters.getLocale());
//		}
//		if (searchParameters.getResultType() != null) {
//			parameters.set("result_type", searchParameters.getResultType().toString());
//		}
//		parameters.set("count", searchParameters.getCount() != null ? String.valueOf(searchParameters.getCount()) : String.valueOf(DEFAULT_RESULTS_PER_PAGE));
//		if (searchParameters.getUntil() != null) {
//			parameters.set("until", new SimpleDateFormat("yyyy-MM-dd").format(searchParameters.getUntil()));
//		}
//		if (searchParameters.getSinceId() != null) {
//			parameters.set("since_id", String.valueOf(searchParameters.getSinceId()));
//		}
//		if (searchParameters.getMaxId() != null) {
//			parameters.set("max_id", String.valueOf(searchParameters.getMaxId()));
//		}
//		if (!searchParameters.isIncludeEntities()) {
//			parameters.set("include_entities", "false");
//		}
		return parameters;
	}
}