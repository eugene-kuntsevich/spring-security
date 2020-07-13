package oauth2.service;

import java.util.Optional;

import oauth2.model.User;

public interface UserService {

    User create(User user);

    Optional<User> findByLogin(String login);
}
