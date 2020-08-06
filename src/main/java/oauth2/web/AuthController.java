package oauth2.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import oauth2.model.UserDetailsImpl;

import static oauth2.security.OAuth2UserDetailsService.getInstanceUserDetailsManager;

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
	public StringResponse authGoogle()
	{
		UserDetails currentUser = getCurrentUser();
		return new StringResponse("Hello Google");
//		return String.format("Welcome, %s! Google auth agent", currentUser.getUsername());
	}

	public UserDetails getCurrentUser()
	{
		return getInstanceUserDetailsManager().loadUserByUsername(getAuthenticatedUserName());
	}

	private String getAuthenticatedUserName()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
		return principal.getName();
	}

	private class StringResponse
	{
		private String response;

		public StringResponse(String s)
		{
			this.response = s;
		}

		public String getResponse()
		{
			return response;
		}

		public void setResponse(String response)
		{
			this.response = response;
		}
	}
}
