package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCollaborationsHandlerTest {

    @Mock
    private TaskManagerStorage storage;

    @InjectMocks
    private ListCollaborationsHandler underTest;

    private static final String IDENTIFIER = "user1";

    @Test
    void handleShouldReturnMessageWhenNoCollaborationsFound() {
        when(storage.listCollaborations(IDENTIFIER))
                .thenReturn(Collections.emptySet());

        String result = underTest.handle(Collections.emptyMap(), IDENTIFIER);

        assertEquals("No collaborations found", result,
                "Handler should return specific message when the user has no collaborations.");
    }

    @Test
    void handleShouldReturnFormattedListWhenCollaborationsExist() {
        Set<String> collaborations = new HashSet<>();
        collaborations.add("project1");
        collaborations.add("project2");

        when(storage.listCollaborations(IDENTIFIER))
                .thenReturn(collaborations);

        String result = underTest.handle(Collections.emptyMap(), IDENTIFIER);


        assertTrue(result.contains("project1"),
                "Result should contain the name of the first collaboration.");

        assertTrue(result.contains("project2"),
                "Result should contain the name of the second collaboration.");

    }

}