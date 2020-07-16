package oauth2.github.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.BASIC;

@Configuration
public class SecurityConfig
{
	@Value("${client_id}")
	private String clientId;

	@Value("${client_secret}")
	private String clientSecret;

	@Bean
	public ClientRegistration clientRegistration()
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
	@Autowired
	public ClientRegistrationRepository clientRegistrationRepository(List<ClientRegistration> registrations)
	{
		return new InMemoryClientRegistrationRepository(registrations);
	}
}
