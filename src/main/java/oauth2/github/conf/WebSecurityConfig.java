package oauth2.github.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import oauth2.github.security.OAuth2UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	private final OAuth2UserDetailsService userService;

	@Autowired
	public WebSecurityConfig(OAuth2UserDetailsService userService)
	{
		this.userService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated()).
			oauth2Login(oauth2Login -> oauth2Login.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(userService)));
	}

	@Bean
	@Autowired
	public ClientRegistrationRepository clientRegistrationRepository(List<ClientRegistration> registrations)
	{
		return new InMemoryClientRegistrationRepository(registrations);
	}
}
