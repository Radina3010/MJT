package bg.sofia.uni.fmi.mjt.taskmanager.model.repository;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.IncorrectPasswordException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    UserRepository underTest = new UserRepository();

    @BeforeEach
    void setUp() throws UserAlreadyExistsException {
        underTest.addUser("user1", "1234");
    }

    @Test
    void addUserNullNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser(null, "password"),
        "UserRepository's addUser should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void addUserBlankNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("    ", "password"),
                "UserRepository's addUser should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void addUserNullPasswordTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("username", null),
                "UserRepository's addUser should throw IllegalArgumentException when called with null password.");
    }

    @Test
    void addUserBlankPasswordTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("username", "    "),
                "UserRepository's addUser should throw IllegalArgumentException when called with blank password.");
    }

    @Test
    void addUserWithSameUsernameTest() {
        assertThrows(UserAlreadyExistsException.class,
                () -> underTest.addUser("user1", "password"),
                "UserRepository's addUser should throw UserAlreadyExistsException when a person tries to use already existing username.");
    }

    @Test
    void addUserValidTest() throws UserAlreadyExistsException {
        underTest.addUser("user3", "password");

        assertTrue(underTest.isUserRegistered("user3"),
                "UserRepository's isUserRegistered should return true for the users added by the method addUser.");
    }

    @Test
    void loginUserNullNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.loginUser(null, "password"),
                "UserRepository's loginUser should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void loginUserBlankNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.loginUser("    ", "password"),
                "UserRepository's loginUser should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void loginUserNullPasswordTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.loginUser("username", null),
                "UserRepository's loginUser should throw IllegalArgumentException when called with null password.");
    }

    @Test
    void loginUserBlankPasswordTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.loginUser("username", "    "),
                "UserRepository's loginUser should throw IllegalArgumentException when called with blank password.");
    }

    @Test
    void loginUserWithUserNotRegisteredTest() {
        assertThrows(UserDoesNotExistException.class,
                () -> underTest.loginUser("username", "password"),
                "UserRepository's loginUser should throw UserDoesNotExistException when there is no user registered with the given username.");
    }

    @Test
    void loginUserIncorrectPasswordTest() {
        assertThrows(IncorrectPasswordException.class,
                () -> underTest.loginUser("user1", "11"),
                "UserRepository's loginUser should throw IncorrectPasswordException when the password doesn't match with the registration password.");
    }

    @Test
    void loginUserValidTest() throws IncorrectPasswordException, UserDoesNotExistException {
        User actual = underTest.loginUser("user1", "1234");

        assertEquals("user1", actual.username(),
                "UserRepository's loginUser should return User object which username matches the username argument.");
        assertEquals("1234", actual.password(),
                "UserRepository's loginUser should return User object which password matches the password argument.");

    }

    @Test
    void isUserRegisteredNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.isUserRegistered(null),
                "UserRepository's isUserRegistered should throw IllegalArgumentException when username is null.");
    }

    @Test
    void isUserRegisteredBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.isUserRegistered("   "),
                "UserRepository's isUserRegistered should throw IllegalArgumentException when username is blank.");
    }


    @Test
    void isUserRegisteredNotExistingUserTest() {
        assertFalse(underTest.isUserRegistered("username"),
                "UserRepository's isUserRegistered should return false when the user does not exist.");
    }
}