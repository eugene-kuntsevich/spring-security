package oauth2.github.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oauth2.service.UserService;
import oauth2.service.UserServiceImpl;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
@Configuration
public class ServiceLayerConfig {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }
}
