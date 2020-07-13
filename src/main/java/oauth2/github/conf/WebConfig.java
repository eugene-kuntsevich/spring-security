package oauth2.github.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import oauth2.web.AuthController;
import oauth2.exception.AccessDeniedExceptionMapper;
import oauth2.exception.UncaughtExceptionMapper;
import oauth2.exception.WebApplicationExceptionMapper;

@Configuration
public class WebConfig extends ResourceConfig
{

    @PostConstruct
    public void init() {
        registerEndpoints();
        registerExceptionMappers();
    }

/*    @Bean
    public AuthController welcomeEndpoint() {
        return new AuthController();
    }*/

    private void registerEndpoints() {
        register(AuthController.class);
    }

    private void registerExceptionMappers() {
        register(UncaughtExceptionMapper.class);
        register(AccessDeniedExceptionMapper.class);
        register(WebApplicationExceptionMapper.class);
    }
}
