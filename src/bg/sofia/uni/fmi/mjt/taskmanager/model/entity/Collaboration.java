package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Collaboration {

    private final String name;
    private final String creator;
    private final Map<TaskKey, Task> tasks = new HashMap<>();
    private final Set<String> users;

    private static final String USER_IS_NOT_IN_COLLABORATION =
            "User '%s' is not a part of collaboration: %s. Could not assign task.";
    private static final String TASK_EXISTS_MESSAGE =
            "Task with the name: %s and date: %s already exists in collaboration: %s.";
    private static final String TASK_DOES_NOT_EXISTS_MESSAGE =
            "Task with the name: %s and date: %s does not exist in collaboration: %s.";
    private static final String ASSIGNEE_OPERATION_MESSAGE =
            "Only the assignee can finish this task.";
    private static final String TASK_STATE_IS_ALREADY_UP_TO_DATE =
            "Task state is already up to date.";

    private Collaboration(String creator, String name) {
        validateNotNullOrBlank(creator);
        validateNotNullOrBlank(name);

        this.name = name;
        this.creator = creator;
        this.users = new HashSet<>();
        this.users.add(creator);
    }

    public static Collaboration of(String name, String creator) {
        validateNotNullOrBlank(name);
        validateNotNullOrBlank(creator);

        return new Collaboration(creator, name);
    }

    public boolean containsUser(String userName) {
        return users.contains(userName);
    }

    public Task addTask(String username, Task task) throws TaskAlreadyExistsException, UserDoesNotExistException {

        validateUserIsPartOfCollaboration(username);
        validateNotNull(task);

        TaskKey searchKey = task.getKey();

        if (tasks.containsKey(searchKey)) {
            throw new TaskAlreadyExistsException(searchKey.formatExceptionMessage(TASK_EXISTS_MESSAGE, this.name));
        }

        tasks.put(searchKey, task);
        return task;

    }

    public Task assignTask(String username, TaskKey searchKey, String assignee)
            throws TaskDoesNotExistException, UserDoesNotExistException, NoChangesDetectedException {

        validateUserIsPartOfCollaboration(username);
        validateUserIsPartOfCollaboration(assignee);
        validateNotNull(searchKey);

        Task oldTask = tasks.get(searchKey);

        validateTaskExists(oldTask, searchKey);

        Task updatedTask = oldTask.withAssignee(assignee);

        validateChangesAreMade(oldTask, updatedTask);

        tasks.put(searchKey, updatedTask);
        return updatedTask;

    }

    public Task finishTask(String username, TaskKey searchKey)
            throws TaskDoesNotExistException, UnauthorizedActionException,
            NoChangesDetectedException, UserDoesNotExistException {

        validateUserIsPartOfCollaboration(username);
        validateNotNull(searchKey);

        Task oldTask = tasks.get(searchKey);

        validateTaskExists(oldTask, searchKey);

        Optional<String> assignee = oldTask.getAssignee();
        if (assignee.isPresent() && !assignee.get().equals(username)) {
            throw new UnauthorizedActionException(ASSIGNEE_OPERATION_MESSAGE);
        }

        Task updatedTask = oldTask.withCompleted(true);

        validateChangesAreMade(oldTask, updatedTask);

        tasks.put(searchKey, updatedTask);
        return updatedTask;
    }

    public boolean addUser(String userToAdd) {
        validateNotNullOrBlank(userToAdd);
        return users.add(userToAdd);
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public Set<String> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public Map<TaskKey, Task> getTasks() {
        return Collections.unmodifiableMap(tasks);
    }

    private <T> void validateNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument can not be null.");
        }
    }

    private static void validateNotNullOrBlank(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("String arguments can not be null or blank");
        }
    }

    private void validateUserIsPartOfCollaboration(String username) throws UserDoesNotExistException {
        validateNotNullOrBlank(username);

        if (!users.contains(username)) {
            String message = String.format(USER_IS_NOT_IN_COLLABORATION, username, name);
            throw new UserDoesNotExistException(message);
        }
    }

    private void validateTaskExists(Task oldTask, TaskKey searchKey) throws TaskDoesNotExistException {
        if (oldTask == null) {
            throw new TaskDoesNotExistException(
                    searchKey.formatExceptionMessage(TASK_DOES_NOT_EXISTS_MESSAGE, this.name));
        }
    }

    private void validateChangesAreMade(Task oldTask, Task updatedTask) throws NoChangesDetectedException {
        if (updatedTask.equals(oldTask)) {
            throw new NoChangesDetectedException(TASK_STATE_IS_ALREADY_UP_TO_DATE);
        }
    }

}
