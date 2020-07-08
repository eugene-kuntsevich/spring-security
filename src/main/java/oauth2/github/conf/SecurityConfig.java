package oauth2.github.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import oauth2.github.security.MyOAuth2UserService;
import oauth2.github.service.UserService;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.BASIC;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
@Configuration
public class SecurityConfig {

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Bean
    public ClientRegistration clientRegistration() {
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
    public MyOAuth2UserService oAuth2userService(UserService userService) {
        return new MyOAuth2UserService(userService);
    }

    @Bean
    @Autowired
    public ClientRegistrationRepository clientRegistrationRepository(java.util.List<ClientRegistration> registrations) {
        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Configuration
    @EnableWebSecurity
    public static class AuthConfig extends WebSecurityConfigurerAdapter {

        private final MyOAuth2UserService userService;

        @Autowired
        public AuthConfig(MyOAuth2UserService userService) {
            this.userService = userService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http
                    .authorizeRequests(authorizeRequests -> authorizeRequests
                            .anyRequest().authenticated()
                    )
                    .oauth2Login(oauth2Login -> oauth2Login
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(userService))
                    );
        }
    }
}
