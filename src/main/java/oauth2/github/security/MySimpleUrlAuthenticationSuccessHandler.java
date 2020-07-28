package oauth2.github.security;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response, Authentication authentication)
		throws IOException
	{
		handle(request, response);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response)
		throws IOException
	{
		String targetUrl = "http://localhost:4300/cabinet";
		if (response.isCommitted())
		{
			return;
		}
		Cookie cookie = new Cookie("my_super_cookie", "cook_value");
		cookie.setPath("/");
		response.addCookie(cookie);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		if (session == null)
		{
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy)
	{
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy()
	{
		return redirectStrategy;
	}
}
