package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignTaskHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private AssignTaskHandler underTest;

    private static final String IDENTIFIER = "creator";

    private static final String COLLAB_VAL = "project";
    private static final String TASK_VAL = "task name";
    private static final String USER_VAL = "user1";
    private static final String DATE_VAL = "2026-05-20";

    private static final String COLLAB_KEY = "collaboration";
    private static final String TASK_KEY = "task";
    private static final String USER_KEY = "user";
    private static final String DATE_KEY = "date";

    @Test
    void handleValidTest() throws Exception {
        Map<String, String> arguments = Map.of(
                COLLAB_KEY, COLLAB_VAL,
                TASK_KEY, TASK_VAL,
                USER_KEY, USER_VAL,
                DATE_KEY, DATE_VAL
        );
        Task assignedTask = Task.of(TASK_VAL, DATE_VAL, null, null);
        when(storage.assignTask(anyString(), anyString(), any(TaskKey.class), anyString()))
                .thenReturn(assignedTask);

        String result = underTest.handle(arguments, IDENTIFIER);
        assertTrue(result.contains(TASK_VAL) && result.contains(USER_VAL),
                "Handler should return success message with task details.");

        verify(storage).assignTask(eq(IDENTIFIER), eq(COLLAB_VAL), any(TaskKey.class), eq(USER_VAL));
    }

    @Test
    void handleShouldReturnErrorWhenTaskDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(
                COLLAB_KEY, COLLAB_VAL,
                TASK_KEY, "task does not exist",
                USER_KEY, USER_VAL,
                DATE_KEY, DATE_VAL
        );

        doThrow(new TaskDoesNotExistException("Task not found."))
                .when(storage).assignTask(anyString(), anyString(), any(TaskKey.class), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Task not found"),
                "Handler should return the TaskDoesNotExistException message.");
    }

    @Test
    void handleShouldReturnErrorWhenCollaborationDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(
                COLLAB_KEY, "collaboration does not exist",
                TASK_KEY, TASK_VAL,
                USER_KEY, USER_VAL,
                DATE_KEY, DATE_VAL
        );

        doThrow(new CollaborationDoesNotExistException("Collaboration not found."))
                .when(storage).assignTask(anyString(), anyString(), any(TaskKey.class), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Collaboration not found"),
                "Handler should return the CollaborationDoesNotExistException message.");
    }

    @Test
    void handleShouldReturnErrorWhenUnauthorized() throws Exception {
        Map<String, String> arguments = Map.of(
                COLLAB_KEY, COLLAB_VAL,
                TASK_KEY, TASK_VAL,
                USER_KEY, USER_VAL,
                DATE_KEY, DATE_VAL
        );

        doThrow(new UnauthorizedActionException("Access denied."))
                .when(storage).assignTask(anyString(), anyString(), any(TaskKey.class), anyString());

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Access denied"),
                "Handler should return the UnauthorizedActionException message.");
    }

}