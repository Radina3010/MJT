package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.DueDateBeforeDateException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidDateFormatException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    Task task = new Task("name",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 1),
            "desc",
            true,
            "user1");

    LocalDate sameDate = LocalDate.of(2026, 1, 1);
    LocalDate differentDate = LocalDate.of(2026, 1, 2);

    Task nullOptionalArgumentsTask = new Task("name",
            null,
            null,
            null,
            false,
            null);



    @Test
    void getKeyNullDateTest(){
        TaskKey expected = new TaskKey("name", null);
        TaskKey actual = nullOptionalArgumentsTask.getKey();

        assertEquals(expected, actual,
                "Task's getKey expected" + expected + "but instead received" + actual);
    }

    @Test
    void mapNullOptionalsParametersTest(){
        String expected = "Task name: name Completed status: false";
        String actual = nullOptionalArgumentsTask.mapTask();

        assertEquals(expected, actual,
                "Task's getKey expected " + expected + " but instead received" + actual);
    }

    @Test
    void mapValidParametersTest(){
        String expected = "Task name: name Date: 2026-01-01 Due date: 2026-01-01 Description: desc Completed status: true Assignee: user1";
        String actual = task.mapTask();

        assertEquals(expected, actual,
                "Task's getKey expected "+ expected + " but instead received" + actual);
    }

    @Test
    void matchesValidDateAndStatusTest() {
        assertTrue(task.matches(sameDate, true),
        "Task's matches should return true when called with date: 2026-01-01 and status: true for " + task.mapTask());
    }

    @Test
    void matchesValidDateAndNullStatusTest() {
        assertTrue(task.matches(sameDate, null),
                "Task's matches should return true when called with date: 2026-01-01 and status: null for " + task.mapTask());
    }

    @Test
    void matchesNullDateAndNullStatusTest() {
        assertTrue(task.matches(null, null),
                "Task's matches should return true when called with date: null and status: null for " + task.mapTask());
    }

    @Test
    void matchesNullDateAndIncorrectStatusTest() {
        assertFalse(task.matches(null, false),
                "Task's matches should return false when called with date: null and status: false for " + task.mapTask());
    }

    @Test
    void matchesIncorrectDateAndNullStatusTest() {
        assertFalse(task.matches(differentDate, null),
                "Task's matches should return false when called with date: '2026-01-02' and status: null for " + task.mapTask());
    }

    @Test
    void matchesCorrectDateTest() {
        assertTrue(task.matches(sameDate),
                "Task's matches should return true when called with date: '2026-01-01' for " + task.mapTask());
    }

    @Test
    void matchesNullDateTest() {
        assertTrue(task.matches(null),
                "Task's matches should return true when called with date: null for " + task.mapTask());
    }

    @Test
    void matchesIncorrectDateTest() {
        assertFalse(task.matches(differentDate),
                "Task's matches should return false when called with date: '2026-01-02' for " + task.mapTask());
    }

    @Test
    void matchesCorrectDateArgumentNullTaskDateTest() {
        assertFalse(nullOptionalArgumentsTask.matches(differentDate),
                "Task's matches should return false when called with date: '2026-01-02' for " + task.mapTask());
    }

    @Test
    void matchesNullDateArgumentNullTaskDateTest() {
        assertTrue(nullOptionalArgumentsTask.matches(null),
                "Task's matches should return true when called with date: null for " + task.mapTask());
    }

    @Test
    void ofValidTest() throws InvalidDateFormatException, DueDateBeforeDateException, InvalidArgumentsForCommandException {

        Task expected = new Task("name", sameDate, sameDate, "desc", false, null);
        Task actual = Task.of("name", "2026-01-01", "2026-01-01", "desc");

        assertEquals(expected, actual,
                "Task's method of expected " + expected.mapTask() + " but instead returned " + actual.mapTask());
    }

    @Test
    void ofNullOptionalDataTest() throws InvalidDateFormatException, DueDateBeforeDateException, InvalidArgumentsForCommandException {

        Task expected = new Task("name", null, null, null, false, null);
        Task actual = Task.of("name", null, null, null);

        assertEquals(expected, actual,
                "Task's method of expected " + expected.mapTask() + " but instead returned " + actual.mapTask());
    }

    @Test
    void ofNullNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> Task.of(null, "2026-01-01", "2026-01-01", "desc"),
                "Task's method of should throw IllegalArgumentException when called with null name.");
    }

    @Test
    void ofDueDateBeforeDateTest() {
        assertThrows(DueDateBeforeDateException.class,
                () -> Task.of("name", "2026-01-02", "2026-01-01", "desc"),
                "Task's method of should throw DueDateBeforeDateException when due-date is before date.");
    }

    @Test
    void withCompletedShouldReturnNewTaskWithUpdatedStatus() {
        Task updatedTask = task.withCompleted(false);

        assertFalse(updatedTask.isCompleted(),
                "New task should be uncompleted.");

        assertEquals(task.getName(),
                updatedTask.getName(),
                "Name should remain same.");

        assertTrue(task.isCompleted(),
                "Original task should remain completed (true).");

        assertNotSame(task, updatedTask,
                "Should return a new object instance, not modify the existing one.");
    }

    @Test
    void withAssigneeShouldReturnNewTaskWithUpdatedAssignee() {
        Task updatedTask = task.withAssignee("user2");

        assertEquals(task.getName(),
                updatedTask.getName(),
                "Name should remain same.");

        assertEquals("user1",
                task.getAssignee().get(),
                "Assignee's name should change.");

        assertEquals("user1",
                task.getAssignee().get(),
                "Original assignee should not change.");

        assertNotSame(task, updatedTask,
                "Should return a new object instance, not modify the existing one.");
    }

}