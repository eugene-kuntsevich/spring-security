package oauth2.github.service;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import oauth2.github.model.User;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
public class UserServiceImpl implements UserService {

    private static final AtomicLong userIdCounter = new AtomicLong();

    private final java.util.Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        user.setId(userIdCounter.incrementAndGet());
        userStore.put(user.getLogin(), user);
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userStore.get(login));
    }
}
