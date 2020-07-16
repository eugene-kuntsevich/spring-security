package oauth2.github.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MyOAuth2User implements OAuth2User, UserDetails
{
	private final String nameAttributeKey;

	private final Map<String, Object> attributes;

	private final Set<GrantedAuthority> authorities;

	public MyOAuth2User(String nameAttributeKey, Map<String, Object> attributes, Collection<GrantedAuthority> authorities)
	{
		this.nameAttributeKey = nameAttributeKey;
		this.attributes = attributes;
		this.authorities = new HashSet<>(authorities);
	}

	@Override
	public String getName()
	{
		return getAttribute(nameAttributeKey);
	}

	@Override
	public String getUsername()
	{
		return getName();
	}

	@Override
	public String getPassword()
	{
		return null;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public Map<String, Object> getAttributes()
	{
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}
}
