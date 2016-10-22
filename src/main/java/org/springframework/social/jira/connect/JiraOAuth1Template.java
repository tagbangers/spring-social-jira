package org.springframework.social.jira.connect;

import net.oauth.*;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.signature.RSA_SHA1;
import org.springframework.social.oauth1.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JiraOAuth1Template implements OAuth1Operations {

	protected static final String SERVLET_BASE_URL = "/plugins/servlet";

	private final String consumerKey;
	private final String privateKey;
	private final String baseUrl;
	private final String callback;
	private OAuthAccessor accessor;

	public JiraOAuth1Template(String consumerKey, String privateKey, String baseUrl, String callback) {
		this.consumerKey = consumerKey;
		this.privateKey = privateKey;
		this.baseUrl = baseUrl;
		this.callback = callback;
	}

	@Override
	public OAuth1Version getVersion() {
		return OAuth1Version.CORE_10_REVISION_A;
	}

	@Override
	public OAuthToken fetchRequestToken(String callbackUrl, MultiValueMap<String, String> additionalParameters) {
		try {
			OAuthAccessor accessor = getAccessor();
			OAuthClient oAuthClient = new OAuthClient(new HttpClient4());
			List<OAuth.Parameter> callBack;
			if (callback == null || "".equals(callback)) {
				callBack = Collections.<OAuth.Parameter>emptyList();
			} else {
				callBack = Stream.of(new OAuth.Parameter(OAuth.OAUTH_CALLBACK, callback)).collect(Collectors.toList());
			}

			OAuthMessage message = oAuthClient.getRequestTokenResponse(accessor, "POST", callBack);
			return new OAuthToken(accessor.requestToken, accessor.tokenSecret);
		} catch (Exception e) {
			throw new RestClientException("Failed to obtain request token", e);
		}
	}

	@Override
	public String buildAuthorizeUrl(String requestToken, OAuth1Parameters parameters) {
		return buildAuthUrl(getAuthorizeUrl(), requestToken, parameters);
	}

	@Override
	public String buildAuthenticateUrl(String requestToken, OAuth1Parameters parameters) {
		return buildAuthorizeUrl(requestToken, parameters);
	}

	@Override
	public OAuthToken exchangeForAccessToken(AuthorizedRequestToken requestToken, MultiValueMap<String, String> additionalParameters) {
		try {
			OAuthAccessor accessor = getAccessor();
			OAuthClient client = new OAuthClient(new HttpClient4());
			accessor.requestToken = requestToken.getValue();
			accessor.tokenSecret = requestToken.getSecret();
			OAuthMessage message = client.getAccessToken(accessor, "POST",
					Stream.of(new OAuth.Parameter(OAuth.OAUTH_VERIFIER, requestToken.getVerifier())).collect(Collectors.toList()));

			return new OAuthToken(message.getToken(), requestToken.getSecret());
		} catch (Exception e) {
			throw new RestClientException("Failed to swap request token with access token", e);
		}
	}

	private final OAuthAccessor getAccessor() {
		if (accessor == null) {
			OAuthServiceProvider serviceProvider = new OAuthServiceProvider(getRequestTokenUrl(), getAuthorizeUrl(), getAccessTokenUrl());
			OAuthConsumer consumer = new OAuthConsumer(callback, consumerKey, null, serviceProvider);
			consumer.setProperty(RSA_SHA1.PRIVATE_KEY, privateKey);
			consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
			accessor = new OAuthAccessor(consumer);
		}
		return accessor;
	}

	private String getAccessTokenUrl() {
		return baseUrl + SERVLET_BASE_URL + "/oauth/access-token";
	}

	private String getRequestTokenUrl() {
		return baseUrl + SERVLET_BASE_URL + "/oauth/request-token";
	}

	private String getAuthorizeUrl() {
		return baseUrl + SERVLET_BASE_URL + "/oauth/authorize";
	}

	private String buildAuthUrl(String baseAuthUrl, String requestToken, OAuth1Parameters parameters) {
		StringBuilder authUrl = new StringBuilder(baseAuthUrl).append('?').append("oauth_token").append('=').append(formEncode(requestToken));
		if (parameters != null) {
			for (Iterator<Map.Entry<String, List<String>>> additionalParams = parameters.entrySet().iterator(); additionalParams.hasNext();) {
				Map.Entry<String, List<String>> param = additionalParams.next();
				String name = formEncode(param.getKey());
				for (Iterator<String> values = param.getValue().iterator(); values.hasNext();) {
					authUrl.append('&').append(name).append('=').append(formEncode(values.next()));
				}
			}
		}
		return authUrl.toString();
	}

	private String formEncode(String data) {
		try {
			return URLEncoder.encode(data, "UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			// should not happen, UTF-8 is always supported
			throw new IllegalStateException(ex);
		}
	}
}
