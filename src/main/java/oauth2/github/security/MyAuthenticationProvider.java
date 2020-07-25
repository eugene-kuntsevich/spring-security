package oauth2.github.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider, Serializable
{

	private static List<GrantedAuthority> AUTHORITIES = new ArrayList<>(1)
	{{
		add((GrantedAuthority) () -> "ROLE_USER");
	}};

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), AUTHORITIES);
	}

	@Override
	public boolean supports(Class<? extends Object> authentication)
	{
		return ( UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication) );
	}
}
