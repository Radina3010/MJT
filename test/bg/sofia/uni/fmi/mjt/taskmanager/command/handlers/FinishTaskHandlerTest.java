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
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinishTaskHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private FinishTaskHandler underTest;

    private static final String IDENTIFIER = "user1";

    private static final String NAME_KEY = "name";
    private static final String DATE_KEY = "date";
    private static final String COLLAB_KEY = "collaboration";

    private static final String NAME_VAL = "task1";
    private static final String DATE_VAL = "2026-01-01";

    @Test
    void handleValidTest() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, NAME_VAL,
                DATE_KEY, DATE_VAL
        );

        Task finishedTask = Task.of(NAME_VAL, DATE_VAL, null, null);

        when(storage.finishTask(eq(IDENTIFIER), any(TaskKey.class), any()))
                .thenReturn(finishedTask);

        String result = underTest.handle(arguments, IDENTIFIER);
        assertTrue(result.contains(NAME_VAL),
                "Should contain the task name that was finished.");

    }

    @Test
    void handleWithCollaborationArgumentValidTest() throws Exception {
        Map<String, String> arguments = Map.of(
                NAME_KEY, "task1",
                DATE_KEY, "2026-01-01",
                COLLAB_KEY, "collaboration"
        );

        Task finishedTask = mock(Task.class);
        when(finishedTask.mapTask()).thenReturn("Task: task1 ");

        when(storage.finishTask(eq(IDENTIFIER), any(TaskKey.class), eq("collaboration")))
                .thenReturn(finishedTask);

        String result = underTest.handle(arguments, IDENTIFIER);
        assertTrue(result.contains("task1"),
                "Should contain the task name that was finished.");

    }

    @Test
    void handleShouldReturnErrorWhenTaskDoesNotExist() throws Exception {
        Map<String, String> arguments = Map.of(NAME_KEY, "task does not exist");

        when(storage.finishTask(anyString(), any(TaskKey.class), nullable(String.class)))
                .thenThrow(new TaskDoesNotExistException("Task not found"));

        String result = underTest.handle(arguments, IDENTIFIER);

        assertTrue(result.contains("Task not found"),
                "Handler should return the TaskDoesNotExistException message.");
    }

}