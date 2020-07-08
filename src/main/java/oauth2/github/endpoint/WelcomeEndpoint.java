package oauth2.github.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import oauth2.github.model.User;
import oauth2.github.security.MyOAuth2User;
import oauth2.github.service.UserService;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
@Path("/")
public class WelcomeEndpoint {

    @Autowired
    private UserService userService;

    @GET
    public String welcome() {
        User currentUser = getCurrentUser();
        return String.format("Welcome, %s! (user id: %s, user login: %s)",
                currentUser.getName(), currentUser.getId(), currentUser.getLogin());
    }

    public User getCurrentUser() {
        return userService.findByLogin(getAuthenticatedUser().getName()).orElseThrow(() -> new RuntimeException("No user logged in."));
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private MyOAuth2User getAuthenticatedUser() {
        return (MyOAuth2User) getAuthentication().getPrincipal();
    }
}
