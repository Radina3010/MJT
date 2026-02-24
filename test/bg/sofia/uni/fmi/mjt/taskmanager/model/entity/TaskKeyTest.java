package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskKeyTest {

    @Test
    void ofValidTest() {
        TaskKey expected = new TaskKey("name", LocalDate.of(2026, 1, 1));
        TaskKey actual = TaskKey.of("name", LocalDate.of(2026, 1, 1));
        assertEquals(expected, actual,
                "TaskKey's of should return a valid object with the correct name and date.");
    }

    @Test
    void ofNullDateTest() {
        TaskKey expected = new TaskKey("name", null);
        TaskKey actual = TaskKey.of("name", null);
        assertEquals(expected, actual,
                "TaskKey's of should return a valid object with null date.");
    }

    @Test
    void ofNullNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> TaskKey.of(null, LocalDate.of(2026, 1, 1)),
                "TaskKey's of should throw IllegalArgumentException when called with null name.");
    }

    @Test
    void ofBlankNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> TaskKey.of("   ", LocalDate.of(2026, 1, 1)),
                "TaskKey's of should throw IllegalArgumentException when called with empty name.");
    }

    @Test
    void equalsWithValidDateAndNullDateTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));
        TaskKey taskWithNullDate = new TaskKey("Task1", null);

        assertNotEquals(taskWithNullDate, taskWithValidDate,
                "TaskKey's equals() should return false when called with [name=task1, date=2026-01-01] and [name=task1,  date=null]");
    }

    @Test
    void formatExceptionMessageNullDateTest() {
        TaskKey taskWithNullDate = new TaskKey("Task1", null);
        String expected = "Name: Task1 Date: NO DATE";
        String actual = taskWithNullDate.formatExceptionMessage("Name: %s Date: %s");
        assertEquals(expected, actual,
                "TaskKey's formatExceptionMessage expected 'Name: Task1 Date: NO DATE' but instead received " + actual);
    }

    @Test
    void formatExceptionMessageValidDateTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));
        String expected = "Name: Task1 Date: 2026-01-01";
        String actual = taskWithValidDate.formatExceptionMessage("Name: %s Date: %s");
        assertEquals(expected, actual,
                "TaskKey's formatExceptionMessage expected 'Name: Task1 Date: 2026-01-01' but instead received " + actual);
    }

    @Test
    void formatExceptionMessageValidDateNullTemplateTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));

        assertThrows(IllegalArgumentException.class,
                () -> taskWithValidDate.formatExceptionMessage(null),
                "TaskKey's formatExceptionMessage should throw IllegalArgumentException when called with null template.");
    }

    @Test
    void formatExceptionMessageNullDateAndExtraParameterTest() {
        TaskKey taskWithNullDate = new TaskKey("Task1", null);
        String expected = "Name: Task1 Date: NO DATE Value: extra value";
        String actual = taskWithNullDate.formatExceptionMessage("Name: %s Date: %s Value: %s", "extra value");
        assertEquals(expected, actual,
                "TaskKey's formatExceptionMessage expected 'Name: Task1 Date: NO DATE Value: extra value' but instead received " + actual);
    }

    @Test
    void formatExceptionMessageValidDateAndExtraParameterTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));
        String expected = "Name: Task1 Date: 2026-01-01 Value: extra value";
        String actual = taskWithValidDate.formatExceptionMessage("Name: %s Date: %s Value: %s", "extra value");
        assertEquals(expected, actual,
                "TaskKey's formatExceptionMessage expected 'Name: Task1 Date: 2026-01-01 Value: extra value' but instead received " + actual);
    }

    @Test
    void formatExceptionMessageValidDateAndExtraParameterNullTemplateTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));

        assertThrows(IllegalArgumentException.class,
                () -> taskWithValidDate.formatExceptionMessage(null, "extra value"),
                "TaskKey's formatExceptionMessage should throw IllegalArgumentException when called with null template and extra value.");
    }

    @Test
    void formatExceptionMessageValidDateAndExtraParameterNullExtraValueTest() {
        TaskKey taskWithValidDate = new TaskKey("Task1", LocalDate.of(2026, 1, 1));
        String expected = "Name: Task1 Date: 2026-01-01 Value: null";
        String actual = taskWithValidDate.formatExceptionMessage("Name: %s Date: %s Value: %s", null);
        assertEquals(expected, actual,
                "TaskKey's formatExceptionMessage expected 'Name: Task1 Date: 2026-01-01 Value: null' but instead received " + actual);
    }

}