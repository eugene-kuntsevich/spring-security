package oauth2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import oauth2.github.security.MyOAuth2User;
import oauth2.model.User;
import oauth2.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController
{
	private UserService userService;

	@GetMapping("/github")
	public String authGithub()
	{
		User currentUser = getCurrentUser();
        return String.format("Welcome, %s! (user id: %s, user login: %s)",
			currentUser.getName(), currentUser.getId(), currentUser.getLogin());
	}

	@GetMapping("/google")
	public String authGoogle()
	{
		User currentUser = getCurrentUser();
		return String.format("Welcome, %s! (user id: %s, user login: %s)",
			currentUser.getName(), currentUser.getId(), currentUser.getLogin());
	}

	public User getCurrentUser()
	{
		return userService.findByLogin(getAuthenticatedUserName()).orElseThrow(() -> new RuntimeException("No user logged in."));
	}

	private String getAuthenticatedUserName()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyOAuth2User principal = (MyOAuth2User) authentication.getPrincipal();
		return principal.getName();
	}

	@Autowired
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
}
