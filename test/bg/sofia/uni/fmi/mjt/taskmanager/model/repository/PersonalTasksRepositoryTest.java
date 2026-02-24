package bg.sofia.uni.fmi.mjt.taskmanager.model.repository;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonalTasksRepositoryTest {

    PersonalTasksRepository underTest = new PersonalTasksRepository();
    Task toAdd = new Task("taskName",
            LocalDate.of(2026, 1, 1),
            null,
            "desc",
            false,
            null);

    @Test
    void addTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask(null, toAdd),
                "PersonalTasksRepository's addTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void addTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("   ", toAdd),
                "PersonalTasksRepository's addTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void addTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("username", null),
                "PersonalTasksRepository's addTask should throw IllegalArgumentException when called with null task.");
    }

    @Test
    void addTaskValidTest() throws TaskAlreadyExistsException {
        Task expected = toAdd;
        Task actual = underTest.addTask("user1", toAdd);

        assertEquals(expected, actual,
       "PersonalTasksRepository's addTask expected to return " + expected.mapTask() + " but instead returned " + actual.mapTask());
    }

    @Test
    void addTaskShouldThrowWhenWeTryToAddAlreadyExistingTaskTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        assertThrows(TaskAlreadyExistsException.class,
                () -> underTest.addTask("user1", toAdd),
                "PersonalTasksRepository's addTask should throw TaskAlreadyExistsException when we try to add already existing task.");
    }

    @Test
    void updateTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.updateTask(null, toAdd),
                "PersonalTasksRepository's updateTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void updateTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.updateTask("   ", toAdd),
                "PersonalTasksRepository's updateTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void updateTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.updateTask("username", null),
                "PersonalTasksRepository's updateTask should throw IllegalArgumentException when called with null task.");
    }

    @Test
    void updateTaskNonExistingTaskTest() {
        Task taskDoesNotExist = new Task("taskName",
                LocalDate.of(2026, 1, 2),
                null,
                "something",
                false,
                null);

        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.updateTask("user1", taskDoesNotExist),
                "PersonalTasksRepository's updateTask should throw TaskDoesNotExistException when there is an existing task with the same name but different date.");
    }

    @Test
    void updateTaskNoUpdatesTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        assertThrows(NoChangesDetectedException.class,
                () -> underTest.updateTask("user1", toAdd),
                "PersonalTasksRepository's updateTask should throw NoChangesDetectedException when it gets called with task with the exact same attributes as already existing task.");
    }

    @Test
    void updateTaskValidTest() throws TaskDoesNotExistException, NoChangesDetectedException, TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Task updatedTask = new Task("taskName",
                LocalDate.of(2026, 1, 1),
                null,
                "something",
                false,
                null);

        Task actual = underTest.updateTask("user1", updatedTask);

        assertEquals(updatedTask, actual,
                "PersonalTasksRepository's updateTask expected to return " + updatedTask.mapTask() + " but instead received " + actual.mapTask());
    }

    @Test
    void deleteTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteTask(null, toAdd.getKey()),
                "PersonalTasksRepository's deleteTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void deleteTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteTask("   ", toAdd.getKey()),
                "PersonalTasksRepository's deleteTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void deleteTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteTask("username", null),
                "PersonalTasksRepository's deleteTask should throw IllegalArgumentException when called with null task.");
    }

    @Test
    void deleteTaskShouldThrowWhenTaskDoesNotExistTest() {
        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.deleteTask("user1", toAdd.getKey()),
                "PersonalTasksRepository's deleteTask should throw TaskDoesNotExistException when called with Task that doesn't exist for user.");
    }

    @Test
    void getTaskAfterDeletingExistingTaskTest() throws TaskAlreadyExistsException, TaskDoesNotExistException {
        underTest.addTask("user1", toAdd);
        underTest.deleteTask("user1", new TaskKey("taskName", LocalDate.of(2026, 1, 1)));

        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.getTask("user1", new TaskKey("taskName", LocalDate.of(2026, 1, 1))),
                "PersonalTasksRepository's getTask should throw TaskDoesNotExistException when called with TaskKey that doesn't exist for user.");
    }

    @Test
    void getTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.getTask(null, toAdd.getKey()),
                "PersonalTasksRepository's getTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void getTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.getTask("   ", toAdd.getKey()),
                "PersonalTasksRepository's getTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void getTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.getTask("username", null),
                "PersonalTasksRepository's getTask should throw IllegalArgumentException when called with null task.");
    }

    @Test
    void getTaskValidTest() throws TaskAlreadyExistsException, TaskDoesNotExistException {
        underTest.addTask("user1", toAdd);
        Task actual = underTest.getTask("user1", new TaskKey("taskName", LocalDate.of(2026, 1, 1)));
        assertEquals(toAdd, actual,
                "PersonalTasksRepository's getTask expected to return" + toAdd.mapTask() + " but instead received " + actual.mapTask());
    }

    @Test
    void listTasksNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.listTasks(null, null, null),
                "PersonalTasksRepository's listTasks should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void listTasksBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.listTasks("   ", null, null),
                "PersonalTasksRepository's listTasks should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void listTasksLocalDateNoBooleanTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Set<Task> expected = Set.of(toAdd);
        Set<Task> actual = underTest.listTasks("user1", LocalDate.of(2026, 1, 1), null);

        assertIterableEquals(expected, actual);
    }

    @Test
    void listTasksLocalDateAndBooleanTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Set<Task> expected = Set.of(toAdd);
        Set<Task> actual = underTest.listTasks("user1", LocalDate.of(2026, 1, 1), false);

        assertIterableEquals(expected, actual);
    }

    @Test
    void listTasksLocalDateMatchesBooleanDoesNotMatchTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Set<Task> expected = Set.of();
        Set<Task> actual = underTest.listTasks("user1", LocalDate.of(2026, 1, 1), true);

        assertIterableEquals(expected, actual);
    }

    @Test
    void getDashBoardNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.getDashBoard(null),
                "PersonalTasksRepository's getDashBoard should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void getDashBoardBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.getDashBoard("   "),
                "PersonalTasksRepository's getDashBoard should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void getDashBoardValidTest() throws TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Set<Task> expected = Set.of();
        Set<Task> actual = underTest.getDashBoard("user1");
        assertIterableEquals(expected, actual);
    }

    @Test
    void finishTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask(null, toAdd.getKey()),
                "PersonalTasksRepository's finishTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void finishTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask("   ", toAdd.getKey()),
                "PersonalTasksRepository's finishTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void finishTaskNullSearchKeyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask("user1", null),
                "PersonalTasksRepository's finishTask should throw IllegalArgumentException when called with null task key.");
    }

    @Test
    void finishTaskNonExistingTaskTest() {
        Task taskDoesNotExist = new Task("taskName",
                LocalDate.of(2026, 1, 2),
                null,
                "something",
                false,
                null);

        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.updateTask("user1", taskDoesNotExist),
                "PersonalTasksRepository's finishTask should throw TaskDoesNotExistException when there is an existing task with the same name but different date.");
    }

    @Test
    void finishTaskValidTest() throws TaskDoesNotExistException, TaskAlreadyExistsException {
        underTest.addTask("user1", toAdd);

        Task finishedTask = underTest.finishTask("user1", toAdd.getKey());

        assertTrue(finishedTask.isCompleted(),
                "PersonalTasksRepository's updateTask expected to return a task with isCompleted = true ");
    }


}