package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTasksHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private ListTasksHandler underTest;

    private static final String IDENTIFIER = "user1";
    private static final String DATE_KEY = "date";
    private static final String COLLAB_KEY = "collaboration";

    @Test
    void handleShouldReturnNoTasksFoundMessageWhenSetIsEmpty() throws Exception {
        Map<String, String> arguments = Collections.emptyMap();

        when(storage.getTasks(eq(IDENTIFIER), any(), any(), any()))
                .thenReturn(Collections.emptySet());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertEquals("No tasks found.", result,
                "Handler should return specific message when no tasks are found.");
    }

    @Test
    void handleShouldReturnFormattedTasksWhenFound() throws Exception {
        Map<String, String> arguments = Collections.emptyMap();

        Task task1 = new Task("task1", LocalDate.now(), null, "desc", false, null);
        Set<Task> tasks = Set.of(task1);

        when(storage.getTasks(eq(IDENTIFIER), isNull(), isNull(), isNull()))
                .thenReturn(tasks);

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Found a total of 1 tasks"),
                "Result should contain the count of tasks.");

        assertTrue(result.contains("task1"),
                "Result should contain information about the tasks.");
    }

    @Test
    void handleShouldReturnErrorOnInvalidDateFormat() {
        Map<String, String> arguments = Map.of(DATE_KEY, "not a date");

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.startsWith("The last command couldn't be completed"));
    }

    @Test
    void handleShouldReturnErrorWhenCollaborationDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(COLLAB_KEY, "not a real collaboration name");

        when(storage.getTasks(any(), any(), any(), any()))
                .thenThrow(new CollaborationDoesNotExistException("Collaboration not found"));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Collaboration not found"),
                "Handler should return error message when collaboration does not exist.");
    }

    @Test
    void handleShouldReturnErrorWhenUnauthorized() throws Exception {
        Map<String, String> args = Map.of(COLLAB_KEY, "user not a part of this collaboration");

        when(storage.getTasks(any(), any(), any(), any()))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        String result = underTest.handle(args, IDENTIFIER);

        assertTrue(result.contains("Access denied"),
                "Handler should return error message on unauthorized access.");
    }

}