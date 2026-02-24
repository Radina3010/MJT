package bg.sofia.uni.fmi.mjt.taskmanager.model.repository;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.IncorrectPasswordException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final Map<String, User> registeredUsers = new HashMap<>();

    private static final String USER_ALREADY_EXISTS_MESSAGE =
            "User with the username: %s already exists.";
    private static final String USER_DOES_NOT_EXISTS_MESSAGE =
            "User with the username: %s does not exist.";
    private static final String INCORRECT_PASSWORD_MESSAGE =
            "Password does not match with the username. Please try again!";

    public void addUser(String username, String password) throws UserAlreadyExistsException {

        if (registeredUsers.containsKey(username)) {
            String message = String.format(USER_ALREADY_EXISTS_MESSAGE, username);
            throw new UserAlreadyExistsException(message);
        }

        registeredUsers.put(username, new User(username, password));
    }

    public User loginUser(String username, String password)
            throws UserDoesNotExistException, IncorrectPasswordException {

        validateNotNullOrBlank(username);
        validateNotNullOrBlank(password);

        User user = registeredUsers.get(username);
        if (user == null) {
            String message = String.format(USER_DOES_NOT_EXISTS_MESSAGE, username);
            throw new UserDoesNotExistException(message);
        }

        String correctPassword = user.password();
        if (!correctPassword.equals(password)) {
            throw new IncorrectPasswordException(INCORRECT_PASSWORD_MESSAGE);
        }

        return user;

    }

    public boolean isUserRegistered(String username) {
        validateNotNullOrBlank(username);

        return registeredUsers.containsKey(username);
    }

    private void validateNotNullOrBlank(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("String arguments can not be null or blank");
        }
    }

}
