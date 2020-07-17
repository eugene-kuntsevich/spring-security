package oauth2.github.security;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import oauth2.model.User;
import oauth2.service.UserService;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

@Service
public class MyOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
{
	private final UserService userService;

	private final RestOperations restOperations;

	private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
	
	public MyOAuth2UserService(UserService userService)
	{
		this.userService = userService;
		this.restOperations = createRestTemplate();
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
//		OAuth2UserAuthority ипользует дл передачи юзера в Spring Security, можно ипользовать другой контруктор, в который
//		можно добавить роль
		authorities.add(new OAuth2UserAuthority(userAttributes));

		User user = findOrCreate(userAttributes);
		userAttributes.put("id", user.getId());
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		return new MyOAuth2User(userNameAttributeName, userAttributes, authorities);
	}

	private User findOrCreate(java.util.Map<String, Object> userAttributes)
	{
		String login = (String) userAttributes.get("login");
		String username = (String) userAttributes.get("name");

		if (login == null)
		{
			login = username;
		}

		Optional<User> userOpt = userService.findByLogin(login);
		if (userOpt.isEmpty())
		{
			User user = new User();
			user.setLogin(login);
			user.setName(username);
			return userService.create(user);
		}
		return userOpt.get();
	}

	private RestTemplate createRestTemplate()
	{
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		return restTemplate;
	}
}
