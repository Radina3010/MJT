package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTaskHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private GetTaskHandler underTest;

    private static final String IDENTIFIER = "user1";

    private static final String NAME_KEY = "name";
    private static final String DATE_KEY = "date";

    private static final String NAME_VAL= "task1";
    private static final String DATE_VAL = "2026-01-01";

    @Test
    void handleValidTest() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL
        );

        Task foundTask = Task.of(NAME_VAL, DATE_VAL, null, null);

        when(storage.getPersonalTask(eq(IDENTIFIER), any(TaskKey.class)))
                .thenReturn(foundTask);

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains(NAME_VAL) && result.contains(DATE_VAL),
                "Handler should return the name of the task and it's date when it's present.");

        verify(storage).getPersonalTask(eq(IDENTIFIER), any(TaskKey.class));
    }

    @Test
    void handleShouldReturnErrorWhenTaskDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(NAME_KEY, "missing-task");

        when(storage.getPersonalTask(anyString(), any(TaskKey.class)))
                .thenThrow(new TaskDoesNotExistException("Task not found with that name/date."));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Task not found"),
                "Handler should return error message when TaskDoesNotExistException is thrown.");

        assertTrue(result.startsWith("The last command couldn't be completed"));
    }

    @Test
    void handleShouldReturnErrorOnInvalidDateFormat() {
        Map<String, String> arguments = Map.of(
                NAME_KEY, "task1",
                DATE_KEY, "invalid-date-format"
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.startsWith("The last command couldn't be completed"));
    }

}