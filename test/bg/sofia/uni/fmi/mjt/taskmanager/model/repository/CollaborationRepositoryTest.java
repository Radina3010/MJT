package bg.sofia.uni.fmi.mjt.taskmanager.model.repository;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistInCollaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Collaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollaborationRepositoryTest {

    CollaborationRepository underTest = new CollaborationRepository();

    Task task;
    Task taskToAdd;

    @BeforeEach
    void setUp() throws CollaborationAlreadyExistsException, CollaborationDoesNotExistException, TaskAlreadyExistsException, UserDoesNotExistException, UnauthorizedActionException {
        underTest.addCollaboration("user1", "collab");

        task = new Task("task1",
               null,
                null,
                "description",
                false,
                null);

        taskToAdd = new Task("task2",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1),
                null,
                true,
                null);

        underTest.addTask("user1", "collab", task);
    }

    @Test
    void addCollaborationWithNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addCollaboration(null, "newCollab"),
                "CollaborationRepository's addCollaboration should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void addCollaborationWithBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addCollaboration("   ", "newCollab"),
                "CollaborationRepository's addCollaboration should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void addCollaborationWithNullCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addCollaboration("username", null),
                "CollaborationRepository's addCollaboration should throw IllegalArgumentException when called with null collaboration name.");
    }

    @Test
    void addCollaborationWithBlankCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addCollaboration("user1", "   "),
                "CollaborationRepository's addCollaboration should throw IllegalArgumentException when called with blank collaboration name.");
    }

    @Test
    void addCollaborationExistingCollaborationNameTest() {
        assertThrows(CollaborationAlreadyExistsException.class,
                () -> underTest.addCollaboration("user1", "collab"),
                "CollaborationRepository's addCollaboration should throw CollaborationAlreadyExistsException when called with collaboration name that already exists.");
    }

    @Test
    void addCollaborationValidTest() throws CollaborationAlreadyExistsException {
        Collaboration newCollaboration = underTest.addCollaboration("username", "Collaboration name");

        String actualCreator = newCollaboration.getCreator();
        String actualName = newCollaboration.getName();

        assertEquals("username", actualCreator,
                "CollaborationRepository's addCollaboration expected to return Collaboration with creator 'username' but instead received '" + actualCreator + "'.");

        assertEquals("Collaboration name", actualName,
                "CollaborationRepository's addCollaboration expected to return Collaboration with name 'Collaboration name' but instead received '" + actualCreator + "'.");
    }

    @Test
    void deleteCollaborationWithNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteCollaboration(null, "newCollab"),
                "CollaborationRepository's deleteCollaboration should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void deleteCollaborationWithBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteCollaboration("   ", "newCollab"),
                "CollaborationRepository's deleteCollaboration should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void deleteCollaborationWithNullCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteCollaboration("user1", null),
                "CollaborationRepository's deleteCollaboration should throw IllegalArgumentException when called with null collaboration name.");
    }

    @Test
    void deleteCollaborationWithBlankCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteCollaboration("user1", "   "),
                "CollaborationRepository's deleteCollaboration should throw IllegalArgumentException when called with blank collaboration name.");
    }

    @Test
    void deleteCollaborationWithUserNotCreatorTest() throws CollaborationDoesNotExistException, UnauthorizedActionException, UserAlreadyExistInCollaboration {
        underTest.addUser("user1", "collab", "user2");
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.deleteCollaboration("user2", "collab"),
                "CollaborationRepository's deleteCollaboration should throw UnauthorizedActionException when user that is not the creator tries to delete it.");
    }

    @Test
    void deleteCollaborationWithNonExistingCollaborationTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.deleteCollaboration("not creator", "not a real collaboration name"),
                "CollaborationRepository's deleteCollaboration should throw UnauthorizedActionException when we try to delete a collaboration that does not exist.");
    }

    @Test
    void listCollaborationsAfterDeleteCollaborationValidTest() throws CollaborationDoesNotExistException, UnauthorizedActionException {
        underTest.deleteCollaboration("user1", "collab");

        Set<String> expectedUserCollaborations = Set.of();
        Set<String> actualUserCollaborations = underTest.listCollaborations("user1");

        assertIterableEquals(expectedUserCollaborations, actualUserCollaborations,
                "CollaborationRepository's listCollaborations expected to return an empty list but instead received " + actualUserCollaborations);
    }

    @Test
    void listCollaborationsWithNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.listCollaborations(null),
                "CollaborationRepository's listCollaborations should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void listCollaborationsWithBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.listCollaborations("   "),
                "CollaborationRepository's listCollaborations should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void listCollaborationsMultipleCollaborationsTest() throws CollaborationAlreadyExistsException {
        underTest.addCollaboration("user", "collaboration1");
        underTest.addCollaboration("user", "collaboration2");

        Set<String> expectedUserCollaborations = Set.of("collaboration1", "collaboration2");
        Set<String> actualUserCollaborations = underTest.listCollaborations("user");

        boolean areEqual = expectedUserCollaborations.containsAll(actualUserCollaborations) &&
                actualUserCollaborations.containsAll(expectedUserCollaborations);

        assertTrue(areEqual,
                "CollaborationRepository's listCollaborations expected to return " + expectedUserCollaborations + " but instead received " + actualUserCollaborations);
    }

    @Test
    void addUserWithNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser(null, "collab", "user"),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void addUserWithBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("   ", "collab", "user"),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void addUserWithNullCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("user1", null, "user"),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with null collaboration name.");
    }

    @Test
    void addUserWithBlankCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("user1", "    ", "user"),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with blank collaboration name.");
    }

    @Test
    void addUserWithNullUserToAddTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("user1", "collab", null),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with null username for person to add.");
    }

    @Test
    void addUserWithBlankUserToAddTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addUser("user1", "collab", "    "),
                "CollaborationRepository's addUser should throw IllegalArgumentException when called with blank username for person to add.");
    }

    @Test
    void addUserToNonExistingCollaborationTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.addUser("user1", "collab does not exist", "user2"),
                "CollaborationRepository's addUser should throw CollaborationDoesNotExistException when called with collaboration that does not exist.");
    }


    @Test
    void addUserShouldThrowUnauthorizedActionExceptionWhenThePersonIsNotAPartOfTheCollaborationTest() {
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.addUser("user not a part of the collaboration", "collab", "user"),
                "CollaborationRepository's addUser should throw UnauthorizedActionException when the user trying to add ne user is not a part of the collaboration.");
    }

    @Test
    void addUserShouldThrowUserAlreadyExistInCollaborationWhenThePersonIsAlreadyInTheCollaborationTest() throws CollaborationDoesNotExistException, UnauthorizedActionException, UserAlreadyExistInCollaboration {
        underTest.addUser("user1", "collab", "user2");

        assertThrows(UserAlreadyExistInCollaboration.class,
                () -> underTest.addUser("user1", "collab", "user2"),
                "CollaborationRepository's addUser should throw UserAlreadyExistInCollaboration when the user is already in the collaboration");
    }

    @Test
    void listCollaborationsAfterAddUserValidTest() throws CollaborationDoesNotExistException, UnauthorizedActionException, UserAlreadyExistInCollaboration {
        underTest.addUser("user1", "collab", "user2");

        Set<String> expectedUserCollaborations = Set.of("collab");
        Set<String> actualUserCollaborations = underTest.listCollaborations("user2");

        assertIterableEquals(expectedUserCollaborations, actualUserCollaborations,
                "CollaborationRepository's listCollaborations expected to return an empty list but instead received " + actualUserCollaborations);
    }

    @Test
    void addTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask(null, "collab", taskToAdd),
                "CollaborationRepository's addTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void addTaskBlankUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("   ", "collab", taskToAdd),
                "CollaborationRepository's addTask should throw IllegalArgumentException when called with blank username.");
    }

    @Test
    void addTaskNullCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("user1", null, taskToAdd),
                "CollaborationRepository's addTask should throw IllegalArgumentException when called with null collaboration name.");
    }

    @Test
    void addTaskBlankCollaborationNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("user1", "   ", taskToAdd),
                "CollaborationRepository's addTask should throw IllegalArgumentException when called with blank collaboration name.");
    }

    @Test
    void addTaskNullTaskTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.addTask("user1", "collab", null),
                "CollaborationRepository's addTask should throw IllegalArgumentException when called with null task.");
    }

    @Test
    void addTaskShouldThrowCollaborationDoesNotExistExceptionWhenTheCollaborationDoesNotExistTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.addTask("user1", "collaboration does not exist", taskToAdd),
                "CollaborationRepository's addTask should throw CollaborationDoesNotExistException when the collaboration does not exist.");
    }

    @Test
    void addTaskShouldThrowUnauthorizedActionExceptionWhenThePersonIsNotInTheCollaborationTest() {
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.addTask("user is not in the collaboration", "collab", taskToAdd),
                "CollaborationRepository's addTask should throw UnauthorizedActionException when the person is not a part of the collaboration.");
    }


    @Test
    void addTaskShouldThrowTaskAlreadyExistsExceptionWhenTheTaskIsAlreadyInTheCollaborationTest() throws CollaborationDoesNotExistException, TaskAlreadyExistsException, UserDoesNotExistException, UnauthorizedActionException {
        underTest.addTask("user1", "collab", taskToAdd);

        assertThrows(TaskAlreadyExistsException.class,
                () -> underTest.addTask("user1", "collab", taskToAdd),
                "CollaborationRepository's addTask should throw UnauthorizedActionException when the person is not a part of the collaboration.");
    }

    @Test
    void listTasksAfterAddTaskValidTest() throws CollaborationDoesNotExistException, TaskAlreadyExistsException, UserDoesNotExistException, UnauthorizedActionException {
        underTest.addTask("user1", "collab", taskToAdd);

        Set<Task> expected = Set.of(taskToAdd, task);
        Set<Task> actual = underTest.listTasks("user1", "collab", null, null);

        boolean areEqual = expected.containsAll(actual) &&
                actual.containsAll(expected);

        assertTrue(areEqual,
                "CollaborationRepository's listTasks expected to return " + expected + " but instead received " + actual);

    }
    @Test
    void finishTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask(null, "collab", task.getKey()),
                "CollaborationRepository's finishTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void finishTaskNullKeyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.finishTask("user1", "collab", null),
                "CollaborationRepository's finishTask should throw IllegalArgumentException when called with null task key.");
    }

    @Test
    void finishTaskToNonExistingCollaborationTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.finishTask("user1", "fakeCollab", task.getKey()),
                "CollaborationRepository's finishTask should throw CollaborationDoesNotExistException when collaboration does not exist.");
    }

    @Test
    void finishTaskUserIsNotAPartOfTheCollaborationTest() {
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.finishTask("user is not tin the collaboration", "collab", task.getKey()),
                "CollaborationRepository's finishTask should throw UnauthorizedActionException when user is not part of the collaboration.");
    }

    @Test
    void finishTaskWhenTaskDoesNotExistTest() {
        assertThrows(TaskDoesNotExistException.class,
                () -> underTest.finishTask("user1", "collab", taskToAdd.getKey()),
                "CollaborationRepository's finishTask should throw TaskDoesNotExistException when there is not a task with task key in the collaboration.");
    }

    @Test
    void finishTaskWhenUserIsNotTheAssigneeTest() throws TaskDoesNotExistException, NoChangesDetectedException, CollaborationDoesNotExistException, UserDoesNotExistException, UnauthorizedActionException, UserAlreadyExistInCollaboration {

        underTest.addUser("user1", "collab", "user2");
        underTest.assignTask("user1", "collab", task.getKey(), "user2");

        assertThrows(UnauthorizedActionException.class,
                () -> underTest.finishTask("user1", "collab", task.getKey()),
                "CollaborationRepository's finishTask should throw UnauthorizedActionException when we try to finish someone else's task.");

    }

    @Test
    void finishTaskValidTest() throws Exception {
        underTest.assignTask("user1", "collab", task.getKey(), "user1");

        Task finishedTask = underTest.finishTask("user1", "collab", task.getKey());

        assertTrue(finishedTask.isCompleted(),
                "CollaborationRepository's finishTask should mark the task as completed.");
    }

    @Test
    void assignTaskNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask(null, "collab", task.getKey(), "user2"),
                "CollaborationRepository's assignTask should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void assignTaskNullKeyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask("user1", "collab", null, "user2"),
                "CollaborationRepository's assignTask should throw IllegalArgumentException when called with null task key.");
    }

    @Test
    void assignTaskBlankAssigneeTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.assignTask("user1", "collab", task.getKey(), "   "),
                "CollaborationRepository's assignTask should throw IllegalArgumentException when called with blank assignee.");
    }

    @Test
    void assignTaskToNonExistingCollaborationTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.assignTask("user1", "collab does not exist", task.getKey(), "user2"),
                "CollaborationRepository's assignTask should throw CollaborationDoesNotExistException when collaboration does not exist.");
    }

    @Test
    void assignTaskUnauthorizedUserTest() {
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.assignTask("user is not in the collaboration", "collab", task.getKey(), "user2"),
                "CollaborationRepository's assignTask should throw UnauthorizedActionException when caller is not part of the collaboration.");
    }

    @Test
    void assignTaskValidTest() throws Exception {
        underTest.addUser("user1", "collab", "user2");
        Task assignedTask = underTest.assignTask("user1", "collab", task.getKey(), "user2");

        assertEquals("user2", assignedTask.getAssignee().get(),
                "CollaborationRepository's assignTask should update the assignee correctly.");
    }

    @Test
    void listTasksWithFilteringTest() throws CollaborationDoesNotExistException, TaskAlreadyExistsException, UserDoesNotExistException, UnauthorizedActionException {
        underTest.addTask("user1", "collab", taskToAdd);

        Set<Task> completedTasks = underTest.listTasks("user1", "collab", null, true);

        assertEquals(1, completedTasks.size());
        assertTrue(completedTasks.contains(taskToAdd), "Should return only the completed task.");

        Set<Task> dateTasks = underTest.listTasks("user1", "collab", LocalDate.of(2026, 1, 1), null);

        assertEquals(1, dateTasks.size());
        assertTrue(dateTasks.contains(taskToAdd), "Should return only the task with the specific date.");
    }
    @Test
    void listUsersNullUsernameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.listUsers(null, "collab"),
                "CollaborationRepository's listUsers should throw IllegalArgumentException when called with null username.");
    }

    @Test
    void listUsersToNonExistingCollaborationTest() {
        assertThrows(CollaborationDoesNotExistException.class,
                () -> underTest.listUsers("user1", "collab does not exist"),
                "CollaborationRepository's listUsers should throw CollaborationDoesNotExistException when collaboration does not exist.");
    }

    @Test
    void listUsersUnauthorizedTest() {
        assertThrows(UnauthorizedActionException.class,
                () -> underTest.listUsers("user is not in the collaboration", "collab"),
                "CollaborationRepository's listUsers should throw UnauthorizedActionException when user is not part of the collaboration.");
    }

    @Test
    void listUsersValidTest() throws Exception {
        underTest.addUser("user1", "collab", "user2");

        Set<String> expected = Set.of("user1", "user2");
        Set<String> actual = underTest.listUsers("user1", "collab");

        boolean isEquals = expected.containsAll(actual) &&
                actual.containsAll(expected);

        assertTrue(isEquals,
                "CollaborationRepository's listUsers should return all users in the collaboration.");

        assertEquals(expected.size(), actual.size(), "The returned Set should contain exactly 2 users.");
    }

}