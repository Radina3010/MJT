package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTaskHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private DeleteTaskHandler underTest;

    private static final String IDENTIFIER = "user1";

    private static final String NAME_KEY = "name";
    private static final String DATE_KEY = "date";

    private static final String NAME_VAL = "task1";
    private static final String DATE_VAL = "2026-01-01";

    @Test
    void handleValidTest() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL
        );

        Task deletedTas = Task.of(NAME_VAL, DATE_VAL, null, null);

        when(storage.deleteTask(anyString(), any(TaskKey.class)))
                .thenReturn(deletedTas);

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains(" was deleted successfully"),
                "Handler should return success message containing the deleted task details.");

        verify(storage).deleteTask(anyString(), any(TaskKey.class));
    }

    @Test
    void handleShouldReturnErrorWhenTaskDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(NAME_KEY, "task that does not exist");

        when(storage.deleteTask(anyString(), any(TaskKey.class)))
                .thenThrow(new TaskDoesNotExistException("Task not found"));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Task not found"),
                "Handler should return the TaskDoesNotExistException error message when task does not exist.");
    }

    @Test
    void handleShouldReturnErrorOnInvalidDateFormat() {
        Map<String, String> arguments = Map.of(
                NAME_KEY, "task1",
                DATE_KEY, "invalid date"
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.startsWith("The last command couldn't be completed"));
    }

}