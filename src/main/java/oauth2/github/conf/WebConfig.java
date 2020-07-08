package oauth2.github.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import oauth2.github.endpoint.WelcomeEndpoint;
import oauth2.github.endpoint.exception.AccessDeniedExceptionMapper;
import oauth2.github.endpoint.exception.UncaughtExceptionMapper;
import oauth2.github.endpoint.exception.WebApplicationExceptionMapper;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-04
 */
@Configuration
public class WebConfig extends ResourceConfig
{

    @PostConstruct
    public void init() {
        registerEndpoints();
        registerExceptionMappers();
    }

    @Bean
    public WelcomeEndpoint welcomeEndpoint() {
        return new WelcomeEndpoint();
    }

    private void registerEndpoints() {
        register(WelcomeEndpoint.class);
    }

    private void registerExceptionMappers() {
        register(UncaughtExceptionMapper.class);
        register(AccessDeniedExceptionMapper.class);
        register(WebApplicationExceptionMapper.class);
    }
}
