package org.springframework.social.jira.connect;

import net.oauth.*;
import net.oauth.signature.RSA_SHA1;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.support.HttpRequestDecorator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

public class JiraOAuth1RequestInterceptor implements ClientHttpRequestInterceptor {

	private OAuthAccessor accessor;

	public JiraOAuth1RequestInterceptor(String consumerKey, String privateKey, String accessToken, String accessTokenSecret) {
		OAuthServiceProvider serviceProvider = new OAuthServiceProvider(null, null, null);
		OAuthConsumer consumer = new OAuthConsumer(null, consumerKey, null, serviceProvider);
		consumer.setProperty(RSA_SHA1.PRIVATE_KEY, privateKey);
		consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
		accessor = new OAuthAccessor(consumer);
		accessor.accessToken = accessToken;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpRequestDecorator protectedResourceRequest = new HttpRequestDecorator(request);
		OAuthMessage message;
		try {
			message = accessor.newRequestMessage(request.getMethod().name(), request.getURI().toString(), Collections.<Map.Entry<?, ?>>emptySet());
			message.getParameters().forEach((entry) -> protectedResourceRequest.addParameter(entry.getKey(), entry.getValue()));
		} catch (OAuthException e) {
			throw new IOException(e);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		return execution.execute(protectedResourceRequest, body);
	}
}
