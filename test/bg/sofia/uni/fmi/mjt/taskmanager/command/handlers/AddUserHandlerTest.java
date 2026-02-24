package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistInCollaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AddUserHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private AddUserHandler underTest;

    private static final String IDENTIFIER = "creator";
    private static final String COLLAB_NAME = "project";
    private static final String USER_TO_ADD = "user1";

    private static final String COLLAB_KEY = "collaboration";
    private static final String USER_KEY = "user";

    @Test
    void handleValidTest() {
        Map<String, String> arguments = Map.of(
                COLLAB_KEY, COLLAB_NAME,
                USER_KEY, USER_TO_ADD
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains(USER_TO_ADD) && result.contains(COLLAB_NAME),
                "Handler should return success message indicating the added user and the collaboration.");

    }

    @Test
    void handleShouldReturnErrorWhenUserDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(COLLAB_KEY, COLLAB_NAME, USER_KEY, "user does not exist");

        doThrow(new UserDoesNotExistException("User does not exist."))
                .when(storage).addUserToCollaboration(anyString(), anyString(), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("User does not exist"),
                "Handler should return the UserDoesNotExistException message.");
    }

    @Test
    void handleShouldReturnErrorWhenCollaborationDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(COLLAB_KEY, "collaboration does not exist", USER_KEY, USER_TO_ADD);

        doThrow(new CollaborationDoesNotExistException("Collaboration not found."))
                .when(storage).addUserToCollaboration(anyString(), anyString(), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Collaboration not found"),
                "Handler should return the CollaborationDoesNotExistException message.");
    }

    @Test
    void handleShouldReturnErrorWhenUserAlreadyInCollaboration() throws Exception {
        Map<String, String> arguments = Map.of(COLLAB_KEY, COLLAB_NAME, USER_KEY, USER_TO_ADD);

        doThrow(new UserAlreadyExistInCollaboration("User is already a member."))
                .when(storage).addUserToCollaboration(anyString(), anyString(), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("User is already a member"),
                "Handler should return the UserAlreadyExistInCollaboration message.");
    }

    @Test
    void handleShouldReturnErrorWhenUnauthorized() throws Exception {
        Map<String, String> arguments = Map.of(COLLAB_KEY, COLLAB_NAME, USER_KEY, USER_TO_ADD);

        doThrow(new UnauthorizedActionException("You do not have permission to add new members."))
                .when(storage).addUserToCollaboration(anyString(), anyString(), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("You do not have permission to add new members"),
                "Handler should return the UnauthorizedActionException message.");
    }

}