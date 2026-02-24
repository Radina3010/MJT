package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListDashboardHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private ListDashboardHandler underTest;

    private static final String IDENTIFIER = "user1";

    @Test
    void handleShouldReturnRestMessageWhenDashboardIsEmpty() {
        when(storage.getDashBoard(IDENTIFIER))
                .thenReturn(Collections.emptySet());

        String result = underTest.handle(Collections.emptyMap(), IDENTIFIER);

        assertEquals("No tasks were found for today. You can rest!", result,
                "Handler should return specific message when dashboard is empty.");
    }

    @Test
    void handleShouldReturnFormattedTasksWhenFound() {
        Task taskMock = mock(Task.class);

        Set<Task> dashboard = new HashSet<>();
        dashboard.add(taskMock);

        when(storage.getDashBoard(IDENTIFIER)).thenReturn(dashboard);

        String result = underTest.handle(Collections.emptyMap(), IDENTIFIER);

        assertTrue(result.contains("Found a total of 1 tasks"),
                "Result should contain the correct count of tasks.");

    }

}