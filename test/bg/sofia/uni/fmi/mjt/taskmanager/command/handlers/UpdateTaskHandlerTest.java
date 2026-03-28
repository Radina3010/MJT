package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UpdateTaskHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private UpdateTaskHandler underTest;

    private static final String IDENTIFIER = "user1";

    private static final String NAME_KEY = "name";
    private static final String DATE_KEY = "date";
    private static final String DESC_KEY = "description";
    private static final String DUE_DATE_KEY = "due-date";

    private static final String NAME_VAL = "task1";
    private static final String DATE_VAL = "2026-01-02";
    private static final String DESC_VAL = "description";
    private static final String DUE_DATE_VAL = "2026-01-01";

    @Test
    void handleValidTest() {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL,
                DESC_KEY, DESC_VAL
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("successfully"),
                "UpdateTaskHandler should return success message when task is updated.");
    }

    @Test
    void handleShouldReturnErrorWhenTaskDoesNotExistTest() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL
        );

        doThrow(new TaskDoesNotExistException("Task not found"))
                .when(storage).updatePersonalTask(eq(IDENTIFIER), any());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Task not found"),
                "Handler should catch TaskDoesNotExistException and return its message.");
    }

    @Test
    void handleShouldReturnErrorWhenDateFormatIsInvalid() {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, "invalid date format"
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.startsWith("The last command couldn't be completed"),
                "Handler should return error message on invalid date format.");

        verifyNoInteractions(storage);
    }

    @Test
    void handleShouldReturnErrorWhenDueDateIsBeforeDate() {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL,
                DUE_DATE_KEY, DUE_DATE_VAL
        );

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.startsWith("The last command couldn't be completed"),
                "Handler should return failure message when due date is before date.");

        verifyNoInteractions(storage);
    }

    @Test
    void handleShouldReturnFatalErrorOnUnexpectedException() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL
        );

        doThrow(new RuntimeException("Something went wrong"))
                .when(storage).updatePersonalTask(eq(IDENTIFIER), any());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertEquals("Unexpected error occurred. Please try again.", result,
                "Handler should return the FATAL_ERROR_MESSAGE defined in CommandHandler.");
    }

}