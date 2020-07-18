package oauth2.github.security;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

@Service
public class OAuth2UserDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
	private static final InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

	private final RestOperations restOperations;

	private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

	public OAuth2UserDetailsService()
	{
		this.restOperations = createRestTemplate();
	}

	public static InMemoryUserDetailsManager getInstanceUserDetailsManager()
	{
		return userDetailsManager;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
	{
		ResponseEntity<Map<String, Object>> response =
			restOperations.exchange(Objects.requireNonNull(requestEntityConverter.convert(userRequest)), new ParameterizedTypeReference<java.util.Map<String, Object>>()
			{
			});

		Map<String, Object> userAttributes = emptyIfNull(response.getBody());

		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		authorities.add(new OAuth2UserAuthority("USER", userAttributes));
		findOrCreate(userAttributes);
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		return new OAuth2UserDetails(userNameAttributeName, userAttributes, authorities);
	}

	private void findOrCreate(Map<String, Object> userAttributes)
	{
		String login = (String) userAttributes.get("login");
		String username = (String) userAttributes.get("name");

		if (login == null)
		{
			login = username;
		}

		if (!userDetailsManager.userExists(login))
		{
			userDetailsManager.createUser(User.withDefaultPasswordEncoder().username(login).password("").roles("USER").build());
		}
	}

	private RestTemplate createRestTemplate()
	{
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		return restTemplate;
	}
}
