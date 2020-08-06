package oauth2.security;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import oauth2.model.UserDetailsImpl;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class HandlerInterceptorImpl extends HandlerInterceptorAdapter
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		UserDetailsImpl userDetails = new UserDetailsImpl("", Collections.emptyMap(), emptyList());
		UsernamePasswordAuthenticationToken rememberMeAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails
			, new Object());
		SecurityContextHolder.getContext().setAuthentication(rememberMeAuthenticationToken);
		SecurityContextHolder.getContext().setAuthentication(null);
		return true;
	}
}
//разобраться где хранить куки и как часто обновлять токен
