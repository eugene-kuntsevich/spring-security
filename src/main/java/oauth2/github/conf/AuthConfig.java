package oauth2.github.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import oauth2.github.security.MyOAuth2UserService;

@Configuration
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter
{
	private final MyOAuth2UserService userService;

	@Autowired
	public AuthConfig(MyOAuth2UserService userService)
	{
		this.userService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated()).
			oauth2Login(oauth2Login -> oauth2Login.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(userService)));
	}
}
