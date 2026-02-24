package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationAlreadyExistsException;
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
class AddCollaborationHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private AddCollaborationHandler underTest;

    private static final String IDENTIFIER = "user1";
    private static final String COLLAB_NAME = "project1";
    private static final String NAME_KEY = "name";

    @Test
    void handleValidTest() throws Exception {
        Map<String, String> arguments = Map.of(NAME_KEY, COLLAB_NAME);

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains(COLLAB_NAME),
                "Handler should return success message with the collaboration name.");

    }

    @Test
    void handleShouldReturnErrorWhenCollaborationAlreadyExists() throws Exception {
        Map<String, String> arguments = Map.of(NAME_KEY, COLLAB_NAME);

        doThrow(new CollaborationAlreadyExistsException("Collaboration already exists."))
                .when(storage).addCollaboration(anyString(), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Collaboration already exists"),
                "Handler should return the exception message when collaboration exists.");
    }

}