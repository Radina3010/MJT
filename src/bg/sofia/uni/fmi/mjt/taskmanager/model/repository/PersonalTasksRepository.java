package bg.sofia.uni.fmi.mjt.taskmanager.model.repository;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonalTasksRepository  {

    private final Map<String, Map<TaskKey, Task>> tasks = new HashMap<>();

    private static final String NO_CHANGES_MESSAGE =
            "No changes detected. The task is already up to date.";
    private static final String TASK_EXISTS_MESSAGE =
            "Task with the name: %s and date: %s already exists in your profile.";
    private static final String TASK_DOES_NOT_EXISTS_MESSAGE =
            "Task with the name: %s and date: %s does not exist in your profile.";

    public Task addTask(String username, Task task) throws TaskAlreadyExistsException {

        validateNotNullOrBlank(username);
        validateNotNull(task);

        tasks.putIfAbsent(username, new HashMap<>());

        TaskKey searchKey = task.getKey();
        Map<TaskKey, Task> userTasks = validateTaskDoesNotExists(username, searchKey);

        userTasks.put(searchKey, task);
        return task;

    }

    public Task updateTask(String username, Task updatedTask)
            throws TaskDoesNotExistException, NoChangesDetectedException {

        validateNotNullOrBlank(username);
        validateNotNull(updatedTask);

        TaskKey searchKey = updatedTask.getKey();
        Map<TaskKey, Task> userTasks = validateTaskExists(username, searchKey);

        Task oldTask = userTasks.get(searchKey);
        if (updatedTask.equals(oldTask)) {
            throw new NoChangesDetectedException(NO_CHANGES_MESSAGE);
        }
        userTasks.put(searchKey, updatedTask);

        return updatedTask;
    }

    public Task deleteTask(String username, TaskKey searchKey) throws TaskDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        Map<TaskKey, Task> userTasks = validateTaskExists(username, searchKey);

        return userTasks.remove(searchKey);

    }

    public Task getTask(String username, TaskKey searchKey) throws TaskDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        Map<TaskKey, Task> userTasks = validateTaskExists(username, searchKey);

        return userTasks.get(searchKey);

    }

    public Set<Task> listTasks(String username, LocalDate date, Boolean completed) {

        validateNotNullOrBlank(username);

        return  tasks.getOrDefault(username, Map.of())
                .values()
                .stream()
                .filter(t -> t.matches(date, completed))
                .collect(Collectors.toSet());

    }

    public Set<Task> getDashBoard(String username) {

        validateNotNullOrBlank(username);

        LocalDate dateNow = LocalDate.now();

        return  tasks.getOrDefault(username, Map.of())
                .values()
                .stream()
                .filter(t -> t.matches(dateNow))
                .collect(Collectors.toSet());

    }

    public Task finishTask(String username, TaskKey searchKey)
            throws TaskDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        Map<TaskKey, Task> userTasks = validateTaskExists(username, searchKey);

        Task oldTask = userTasks.get(searchKey);

        Task updatedTask = oldTask.withCompleted(true);

        userTasks.put(searchKey, updatedTask);
        return updatedTask;

    }

    private Map<TaskKey, Task> validateTaskDoesNotExists(String username, TaskKey searchKey)
            throws TaskAlreadyExistsException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        Map<TaskKey, Task> userTasks = tasks.get(username);

        if (userTasks != null && userTasks.containsKey(searchKey)) {
            throw new TaskAlreadyExistsException(searchKey.formatExceptionMessage(TASK_EXISTS_MESSAGE));
        }

        return userTasks;

    }

    private Map<TaskKey, Task> validateTaskExists(String username, TaskKey searchKey)
            throws TaskDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        Map<TaskKey, Task> userTasks = tasks.get(username);

        if (userTasks == null || !userTasks.containsKey(searchKey)) {
            throw new TaskDoesNotExistException(searchKey.formatExceptionMessage(TASK_DOES_NOT_EXISTS_MESSAGE));
        }

        return  userTasks;

    }

    private <T> void validateNotNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument can not be null.");
        }
    }

    private void validateNotNullOrBlank(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("String arguments can not be null or blank");
        }
    }

}
