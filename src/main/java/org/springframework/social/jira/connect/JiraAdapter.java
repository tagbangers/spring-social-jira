package org.springframework.social.jira.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.jira.api.Jira;
import org.springframework.social.jira.api.JiraUser;

public class JiraAdapter implements ApiAdapter<Jira> {

	@Override
	public boolean test(Jira jira) {
		try {
			jira.myselfOperations().getUser();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	@Override
	public void setConnectionValues(Jira jira, ConnectionValues values) {
		JiraUser jiraUser = jira.myselfOperations().getUser();
		values.setProviderUserId(jiraUser.getName());
		values.setDisplayName("@" + jiraUser.getDisplayName());
//		values.setProfileUrl(profile.getProfileUrl());
//		values.setImageUrl(profile.getProfileImageUrl());
	}

	@Override
	public UserProfile fetchUserProfile(Jira jira) {
		JiraUser user = jira.myselfOperations().getUser();
		return new UserProfileBuilder()
				.setName(user.getName())
				.setUsername(user.getName())
				.setEmail(user.getEmailAddress())
				.build();
	}

	@Override
	public void updateStatus(Jira jira, String message) {

	}
}