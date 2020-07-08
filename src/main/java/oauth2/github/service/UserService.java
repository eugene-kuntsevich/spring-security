package oauth2.github.service;

import java.util.Optional;

import oauth2.github.model.User;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
public interface UserService {

    User create(User user);

    Optional<User> findByLogin(String login);
}
