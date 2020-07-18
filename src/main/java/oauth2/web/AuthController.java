package oauth2.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import oauth2.github.security.OAuth2UserDetails;

import static oauth2.github.security.OAuth2UserDetailsService.getInstanceUserDetailsManager;

@RestController
@RequestMapping("/auth")
public class AuthController
{
	@GetMapping("/github")
	public String authGithub()
	{
		UserDetails currentUser = getCurrentUser();
		return String.format("Welcome, %s! Github auth agent", currentUser.getUsername());
	}

	@GetMapping("/google")
	public String authGoogle()
	{
		UserDetails currentUser = getCurrentUser();
		return String.format("Welcome, %s! Google auth agent", currentUser.getUsername());
	}

	public UserDetails getCurrentUser()
	{
		return getInstanceUserDetailsManager().loadUserByUsername(getAuthenticatedUserName());
	}

	private String getAuthenticatedUserName()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2UserDetails principal = (OAuth2UserDetails) authentication.getPrincipal();
		return principal.getName();
	}
}
