package oauth2.github.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.BASIC;

@Configuration
public class AuthProvidersConfig
{
	@Value("${client_id}")
	private String clientId;

	@Value("${client_secret}")
	private String clientSecret;

	@Value("${client_id_google}")
	private String clientIdGoogle;

	@Value("${client_secret_google}")
	private String clientSecretGoogle;
////переделать конфиги, добавить провперти для авторизации
	@Bean
	public ClientRegistration clientRegistrationGithub()
	{
		return ClientRegistration
			.withRegistrationId("github")
			.clientId(clientId)
			.clientSecret(clientSecret)
			.userNameAttributeName("login")
			.clientAuthenticationMethod(BASIC)
			.authorizationGrantType(AUTHORIZATION_CODE)
			.userInfoUri("https://api.github.com/user")
			.tokenUri("https://github.com/login/oauth/access_token")
			.authorizationUri("https://github.com/login/oauth/authorize")
			.redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
			.build();
	}

	@Bean
	public ClientRegistration clientRegistrationGoogle()
	{
		return ClientRegistration
			.withRegistrationId("google")
			.clientId(clientIdGoogle)
			.clientSecret(clientSecretGoogle)
			.userNameAttributeName("name")
			.clientAuthenticationMethod(BASIC)
			.authorizationGrantType(AUTHORIZATION_CODE)
			.userInfoUri("https://www.googleapis.com/userinfo/v2/me")
			.tokenUri("https://www.googleapis.com/oauth2/v3/token")
			.authorizationUri("https://accounts.google.com/o/oauth2/auth")
			.scope("https://www.googleapis.com/auth/plus.login")
			.redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
			.build();
	}
}
