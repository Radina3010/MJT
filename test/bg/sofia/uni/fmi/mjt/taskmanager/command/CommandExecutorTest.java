package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    TaskManagerStorage storage;

    CommandExecutor underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommandExecutor(storage);
    }

    @Test
    void executeShouldReturnErrorMessageForUnknownCommandTest() {
        Command command = new Command("unknown command", Collections.emptyMap());
        String response = underTest.execute(command, "session1");

        assertTrue(response.contains("Unknown command"),
                "CommandExecutor's execute should return error message for unknown commands.");
    }

    @Test
    void executeShouldReturnErrorWhenValidationFailsTest() {
        Command command = new Command("register", Collections.emptyMap());
        String response = underTest.execute(command, "session1");

        assertTrue(response.contains("Missing required arguments"),
                "CommandExecutor's execute should catch validation exception and return it as a string message.");
    }

    @Test
    void executeShouldBlockPrivateCommandWhenNotLoggedInTest() throws CollaborationDoesNotExistException, TaskAlreadyExistsException, UserDoesNotExistException, UnauthorizedActionException {
        Command command = new Command("add-task", Map.of(
                "name", "task1",
                "date", "2026-01-01"
        ));

        String expected = "The last command couldn't be completed. Please login first.";
        String actual = underTest.execute(command, "session1");

        assertEquals(expected, actual,
                "CommandExecutor's execute should block execution of private commands if user is not logged in.");

        verify(storage, never()).addTask(any(), any(), any());
    }

    @Test
    void executeShouldAllowPublicCommandWhenNotLoggedInTest() throws Exception {
        Command command = new Command("register", Map.of(
                "username", "user1",
                "password", "1234"
        ));

        String response = underTest.execute(command, "session1");

        assertFalse(response.contains("Please login first"),
                "Public commands should work without login.");

        verify(storage).registerUser("user1", "1234");
    }

    @Test
    void executeShouldBlockPublicCommandWhenAlreadyLoggedInTest() throws Exception {

        String username = "user1";
        String password = "1234";

        when(storage.logInUser(username, password)).thenReturn(new User(username, password));

        Command loginCommand = new Command("login",
                Map.of("username", username, "password", password));

        underTest.execute(loginCommand, "session1");

        Command registerCommand = new Command("register", Map.of(
                "username", "user2",
                "password", "1111"
        ));

        String response = underTest.execute(registerCommand, "session1");

        assertTrue(response.contains("You are already logged in"),
                "CommandExecutor should block public commands when already logged in.");
    }

    @Test
    void executeShouldAllowPrivateCommandWhenLoggedInTest() throws Exception {
        String username = "user1";
        String password = "1234";

        when(storage.logInUser(username, password)).thenReturn(new User(username, password));

        Command loginCommand = new Command("login", Map.of("username", username, "password", password));
        underTest.execute(loginCommand, "session1");

        Command addTaskCommand = new Command("add-task", Map.of(
                "name", "task1",
                "date", "2026-01-01"
        ));

        String response = underTest.execute(addTaskCommand, "session1");

        assertFalse(response.contains("Please login first"));
    }

    @Test
    void removeSessionShouldLogOutUserTest() throws Exception {
        String username = "user1";
        String password = "pass";
        when(storage.logInUser(username, password)).thenReturn(new User(username, password));

        Command loginCommand = new Command("login", Map.of("username", username, "password", password));

        underTest.execute(loginCommand, "session1");
        underTest.removeSession("session1");

        Command publicCommand = new Command("list-tasks", Collections.emptyMap());
        String response = underTest.execute(publicCommand, "session1");

        assertTrue(response.contains("Please login first"),
                "CommandExecutor's removeSession should log out users.");
    }

}