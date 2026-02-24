package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsersHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private ListUsersHandler underTest;

    private static final String IDENTIFIER = "user1";
    private static final String COLLAB_NAME = "collab";

    private static final String COLLABORATION_KEY = "collaboration";

    private static final Map<String, String> arguments = Map.of(COLLABORATION_KEY, COLLAB_NAME);

    @Test
    void handleValidTest() throws Exception {
        Set<String> users = new HashSet<>();
        users.add("user1");
        users.add("user2");

        when(storage.listUsersFromCollaboration(IDENTIFIER, COLLAB_NAME))
                .thenReturn(users);

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains(COLLAB_NAME),
                "Result should contain the collaboration name.");

        assertTrue(result.contains("user1") && result.contains("user2"),
                "Result should contain the usernames returned from storage.");
    }

    @Test
    void handleShouldReturnErrorWhenCollaborationDoesNotExist() throws Exception {
        when(storage.listUsersFromCollaboration(IDENTIFIER, COLLAB_NAME))
                .thenThrow(new CollaborationDoesNotExistException("Collaboration not found"));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Collaboration not found"),
                "Handler should return error message when collaboration does not exist.");
    }

    @Test
    void handleShouldReturnErrorWhenUnauthorized() throws Exception {
        when(storage.listUsersFromCollaboration(IDENTIFIER, COLLAB_NAME))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Access denied"),
                "Handler should return error message when action is unauthorized.");
    }

}