package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollaborationTest {

    Collaboration underTest;
    Task taskWithoutAssignee;
    Task taskNotPresentInCollaborationTasks;

    @BeforeEach
    void setUp() throws TaskAlreadyExistsException, UserDoesNotExistException {
        underTest = Collaboration.of("name", "user1");

        taskWithoutAssignee = new Task("task1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1),
                "description",
                false,
                null);

        taskNotPresentInCollaborationTasks = new Task("task2",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1),
                "description",
                true,
                null);

        underTest.addTask("user1", taskWithoutAssignee);
        underTest.addUser("user2");
    }

    @Test
    void addTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("user1", null),
                "Collaboration's addTask should throw IllegalArgumentException when called with null.");
    }

    @Test
    void addTaskNewTaskTest() throws TaskAlreadyExistsException, UserDoesNotExistException {
        Task addedTask = underTest.addTask("user1", taskNotPresentInCollaborationTasks);

        assertSame(taskNotPresentInCollaborationTasks,
                addedTask,
                "Collaboration's addTask should return the exact same Task instance that was passed to it.");

        assertTrue(underTest.getTasks().containsKey(taskNotPresentInCollaborationTasks.getKey()),
                "Collaboration's tasks map should contain the key of the newly added task.");

        assertTrue(underTest.getTasks().containsValue(taskNotPresentInCollaborationTasks),
                "Collaboration's tasks map should contain the added task object as a value.");
    }

    @Test
    void addTaskWithExistingTaskShouldThrowTest() {
        assertThrows(TaskAlreadyExistsException.class,
                () -> underTest.addTask("user1", taskWithoutAssignee),
                "Collaborations's addTask should throw TaskAlreadyExistsException when you try to add already existing task.");
    }

    @Test
    void addTaskWithUserNotAPartOfTheCollaborationTaskShouldThrowTest() {
        assertThrows(UserDoesNotExistException.class,
                () -> underTest.addTask("user not present in collaboration", taskWithoutAssignee),
                "Collaborations's addTask should throw UserDoesNotExistException when a person not a part of the collaboration tries to add task.");
    }


    @Test
    void assignTaskNullTaskKeyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask("user2", null, "user2"),
                "Collaboration's assignTask should throw IllegalArgumentException when called with null task key parameter.");
    }


    @Test
    void assignTaskNullAssigneeTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask("user2", taskWithoutAssignee.getKey(), null),
                "Collaboration's assignTask should throw IllegalArgumentException when called with null assignee parameter.");
    }

    @Test
    void assignTaskBlankAssigneeTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask("user2", taskWithoutAssignee.getKey(), "   "),
                "Collaboration's assignTask should throw IllegalArgumentException when called with blank assignee parameter.");
    }

    @Test
    void assignTaskValidTest() throws TaskDoesNotExistException, UserDoesNotExistException, NoChangesDetectedException {
        Task assignedTask = underTest.assignTask("user1", taskWithoutAssignee.getKey(), "user2");
        Optional<String> expected = Optional.of("user2");
        Optional<String> actual = assignedTask.getAssignee();

        assertEquals(expected,
                actual,
                "Collaboration's assignTask should return a task with the assignee updated to the specified user ('user2').");
    }

    @Test
    void assignTaskNotExistingTaskTest() {

        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.assignTask("user2",taskNotPresentInCollaborationTasks.getKey(), "user2"),
                "Collaboration's assignTask should throw TaskDoesNotExistException when attempting to assign a task that is not present in the collaboration.");
    }

    @Test
    void assignTaskToUserNotPresentInCollaborationTest() {

        assertThrows(UserDoesNotExistException.class,
                () -> underTest.assignTask("user2", taskWithoutAssignee.getKey(), "user not present in collaboration"),
                "Collaboration's assignTask should throw UserDoesNotExistException when attempting to assign a task to a person that is not a part of the collaboration.");
    }
    @Test
    void assignTaskShouldThrowExceptionWhenUserIsNotInCollaborationTest() {
        assertThrows(UserDoesNotExistException.class,
                () -> underTest.assignTask("user not in collab", taskWithoutAssignee.getKey(), "user2"),
                "Collaboration's assignTask should throw UserDoesNotExistException when the caller is not part of the collaboration.");
    }

    @Test
    void assignTaskShouldThrowNoChangesExceptionWhenReassigningSameUserTest()
            throws TaskDoesNotExistException, UserDoesNotExistException, NoChangesDetectedException {

        underTest.assignTask("user1", taskWithoutAssignee.getKey(), "user2");
        assertThrows(NoChangesDetectedException.class,
                () -> underTest.assignTask("user1", taskWithoutAssignee.getKey(), "user2"),
                "Collaboration's assignTask should throw NoChangesDetectedException when assigning the same user again.");
    }
    @Test
    void finishTaskNullTaskKeyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask("user2", null),
                "Collaboration's finishTask should throw IllegalArgumentException when called with null task key parameter.");
    }

    @Test
    void finishTaskNullUserTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask(null, taskWithoutAssignee.getKey()),
                "Collaboration's finishTask should throw IllegalArgumentException when called with null assignee parameter.");
    }

    @Test
    void finishTaskBlankUserTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask("    ", taskWithoutAssignee.getKey()),
                "Collaboration's finishTask should throw IllegalArgumentException when called with blank assignee parameter.");
    }

    @Test
    void finishTaskWithoutAssigneeValidTest() throws TaskDoesNotExistException, UnauthorizedActionException, NoChangesDetectedException, UserDoesNotExistException {
        Task finishTask = underTest.finishTask("user2", taskWithoutAssignee.getKey());

        assertTrue(finishTask.isCompleted(),
                "Collaboration's finishTask should return a task with the completed status updated to true.");
    }

    @Test
    void finishTaskNotExistingTaskTest() {

        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.finishTask("user2", taskNotPresentInCollaborationTasks.getKey()),
                "Collaboration's assignTask should throw TaskDoesNotExistException when attempting to assign a task that is not present in the collaboration.");
    }

    @Test
    void finishTaskToUserNotPresentInCollaborationTest() {

        assertThrows(UserDoesNotExistException.class,
                () -> underTest.finishTask("user not present in collaboration", taskWithoutAssignee.getKey()),
                "Collaboration's assignTask should throw UserDoesNotExistException when attempting to assign a task to a person that is not a part of the collaboration.");
    }

    @Test
    void finishTaskShouldThrowUnauthorizedWhenUserIsNotTheAssigneeTest()
            throws TaskDoesNotExistException, UserDoesNotExistException, NoChangesDetectedException {

        underTest.assignTask("user1", taskWithoutAssignee.getKey(), "user1");

        assertThrows(UnauthorizedActionException.class,
                () -> underTest.finishTask("user2", taskWithoutAssignee.getKey()),
                "Collaboration's finishTask should throw UnauthorizedActionException when a user tries to finish someone else's task.");
    }

    @Test
    void finishTaskShouldSucceedWhenUserIsTheAssigneeTest()
            throws TaskDoesNotExistException, UnauthorizedActionException, NoChangesDetectedException, UserDoesNotExistException {

        underTest.assignTask("user1", taskWithoutAssignee.getKey(), "user2");

        Task finishedTask = underTest.finishTask("user2", taskWithoutAssignee.getKey());

        assertTrue(finishedTask.isCompleted(),
                "Task should be completed successfully when the correct assignee finishes it.");
    }

    @Test
    void finishTaskShouldThrowNoChangesExceptionWhenTaskAlreadyCompletedTest()
            throws TaskDoesNotExistException, UnauthorizedActionException, NoChangesDetectedException, UserDoesNotExistException {

        underTest.finishTask("user1", taskWithoutAssignee.getKey());

        assertThrows(NoChangesDetectedException.class,
                () -> underTest.finishTask("user1", taskWithoutAssignee.getKey()),
                "Collaboration's finishTask should throw NoChangesDetectedException if task is already completed.");
    }

    @Test
    void addUserAddingNewUserTest() {
        assertTrue(underTest.addUser("newUser"),
                "addUser should return true when a new user is added.");

        assertTrue(underTest.containsUser("newUser"),
                "Collaboration should contain the newly added user.");
    }

    @Test
    void addUserAddingExistingUserTest() {
        assertFalse(underTest.addUser("user2"),
                "addUser should return false when adding an existing user.");
    }

}
