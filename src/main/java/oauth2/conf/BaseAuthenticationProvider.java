package oauth2.conf;

import java.io.Serializable;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import oauth2.security.Roles;

import static java.util.Collections.singletonList;

@Component
public class BaseAuthenticationProvider implements AuthenticationProvider, Serializable
{

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), singletonList(new GrantedAuthority()
			{
				@Override
				public String getAuthority()
				{
					return Roles.USER.toString();
				}
			}));
		return authenticationToken;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication)
	{
		return ( UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication) );
	}
}
