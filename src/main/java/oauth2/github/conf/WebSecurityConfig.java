package oauth2.github.conf;

import java.util.Arrays;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import oauth2.github.security.HandlerInterceptorImpl;
import oauth2.github.security.MyAuthenticationProvider;
import oauth2.github.security.MySimpleUrlAuthenticationSuccessHandler;
import oauth2.github.security.OAuth2UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer
{
	private final OAuth2UserDetailsService userService;
	private final MyAuthenticationProvider myAuthenticationProvider;
	private final HandlerInterceptorAdapter handlerInterceptorAdapter;

	@Autowired
	public WebSecurityConfig(OAuth2UserDetailsService userService, MyAuthenticationProvider myAuthenticationProvider,
		HandlerInterceptorAdapter handlerInterceptorAdapter)
	{
		this.userService = userService;
		this.myAuthenticationProvider = myAuthenticationProvider;
		this.handlerInterceptorAdapter = handlerInterceptorAdapter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().anyRequest().authenticated().and().authenticationProvider(myAuthenticationProvider).formLogin()
			.successHandler(myAuthenticationSuccessHandler()).and().cors().and().
			oauth2Login(oauth2Login -> oauth2Login.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(userService))
				.successHandler(myAuthenticationSuccessHandler()));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(new HandlerInterceptorImpl());
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler()
	{
		return new MySimpleUrlAuthenticationSuccessHandler();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4300"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@Autowired
	public ClientRegistrationRepository clientRegistrationRepository(List<ClientRegistration> registrations)
	{
		return new InMemoryClientRegistrationRepository(registrations);
	}
}
