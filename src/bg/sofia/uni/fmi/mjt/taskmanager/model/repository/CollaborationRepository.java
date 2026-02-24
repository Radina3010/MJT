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
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollaborationRepository {

    private final Map<String, Collaboration> collaborations = new HashMap<>();

    public Collaboration addCollaboration(String username, String collaborationName)
            throws CollaborationAlreadyExistsException {

        validateNotNullOrBlank(username);

        collaborationDoesNotExistValidation(collaborationName);

        Collaboration newCollab = Collaboration.of(collaborationName, username);
        collaborations.put(collaborationName, newCollab);

        return newCollab;
    }

    public Collaboration deleteCollaboration(String username, String collaborationName)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        validateNotNullOrBlank(username);

        Collaboration collaboration = getVerifiedCollaboration(username, collaborationName);
        userIsCreatorValidation(collaboration, username);

        return collaborations.remove(collaborationName);
    }

    public Set<String> listCollaborations(String username) {
        validateNotNullOrBlank(username);

        return collaborations.values()
                .stream()
                .filter(c -> c.containsUser(username))
                .map(Collaboration::getName)
                .collect(Collectors.toSet());

    }

    public void addUser(String username, String collaborationName, String userToAdd)
            throws CollaborationDoesNotExistException, UserAlreadyExistInCollaboration, UnauthorizedActionException {

        validateNotNullOrBlank(username);
        validateNotNullOrBlank(userToAdd);

        Collaboration collaboration = getVerifiedCollaboration(username, collaborationName);

        if (!collaboration.addUser(userToAdd)) {
            String message = String.format("User: %s is already a part of collaboration: %s",
                    userToAdd, collaborationName);
            throw new UserAlreadyExistInCollaboration(message);
        }
    }

    public Task addTask(String username, String collaborationName, Task task) throws TaskAlreadyExistsException,
            CollaborationDoesNotExistException, UnauthorizedActionException, UserDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(task);

        return getVerifiedCollaboration(username, collaborationName).addTask(username, task);
    }

    public Task finishTask(String username, String collaborationName, TaskKey searchKey)
            throws TaskDoesNotExistException, UnauthorizedActionException,
            CollaborationDoesNotExistException, NoChangesDetectedException, UserDoesNotExistException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);

        return getVerifiedCollaboration(username, collaborationName).finishTask(username, searchKey);
    }

    public Task assignTask(String username, String collaborationName, TaskKey searchKey, String assignee)
            throws TaskDoesNotExistException, CollaborationDoesNotExistException, UnauthorizedActionException,
            UserDoesNotExistException, NoChangesDetectedException {

        validateNotNullOrBlank(username);
        validateNotNull(searchKey);
        validateNotNullOrBlank(assignee);

        Collaboration collaboration =  getVerifiedCollaboration(username, collaborationName);

        return collaboration.assignTask(username, searchKey, assignee);
    }

    public Set<Task> listTasks(String username, String collaborationName, LocalDate date, Boolean completed)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        validateNotNullOrBlank(username);

        return getVerifiedCollaboration(username, collaborationName)
                .getTasks()
                .values()
                .stream()
                .filter(t -> t.matches(date, completed))
                .collect(Collectors.toSet());
    }

    public Set<String> listUsers(String username, String collaborationName)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        validateNotNullOrBlank(username);
        Collaboration collaboration = getVerifiedCollaboration(username, collaborationName);
        return Collections.unmodifiableSet(collaboration.getUsers());

    }

    private Collaboration getVerifiedCollaboration(String username, String collaborationName)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        collaborationExistsValidation(collaborationName);

        Collaboration collaboration = collaborations.get(collaborationName);

        if (!collaboration.containsUser(username)) {
            throw new UnauthorizedActionException("You are not a part of this collaboration.");
        }

        return collaboration;
    }

    private void collaborationExistsValidation(String collaborationName) throws CollaborationDoesNotExistException {

        validateNotNullOrBlank(collaborationName);
        if (!collaborations.containsKey(collaborationName)) {
            String message = String.format("Collaboration with the name: %s does not exist.", collaborationName);
            throw new CollaborationDoesNotExistException(message);
        }

    }

    private void collaborationDoesNotExistValidation(String collaborationName)
            throws CollaborationAlreadyExistsException {

        if (collaborations.containsKey(collaborationName)) {
            String message = String.format("Collaboration with the name: %s already exists.", collaborationName);
            throw new CollaborationAlreadyExistsException(message);
        }

    }

    private void userIsCreatorValidation(Collaboration collaboration, String username)
            throws UnauthorizedActionException {

        String creator = collaboration.getCreator();

        if (!creator.equals(username)) {
            String message = String.format("Only the creator of the collaboration (%s) can delete it.", creator);
            throw new UnauthorizedActionException(message);
        }
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
