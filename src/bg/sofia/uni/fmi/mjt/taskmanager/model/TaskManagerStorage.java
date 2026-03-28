package bg.sofia.uni.fmi.mjt.taskmanager.model;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.IncorrectPasswordException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistInCollaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Collaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.User;
import bg.sofia.uni.fmi.mjt.taskmanager.model.repository.CollaborationRepository;
import bg.sofia.uni.fmi.mjt.taskmanager.model.repository.PersonalTasksRepository;
import bg.sofia.uni.fmi.mjt.taskmanager.model.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;

public class TaskManagerStorage {

    private final UserRepository users;
    private final PersonalTasksRepository personalTasks;
    private final CollaborationRepository collaborations;

    private static final String USERS_FILE = "users.json";
    private static final String TASKS_FILE = "tasks.json";
    private static final String COLLABS_FILE = "collabs.json";

    private static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalDate.parse(json.getAsString());
                }
            })
            .setPrettyPrinting()
            .create();

    public TaskManagerStorage() {
        UserRepository loadedUsers = loadOrDefault(USERS_FILE, UserRepository.class, UserRepository::new);

        if (loadedUsers == null) {

            this.users = new UserRepository();
            this.personalTasks = new PersonalTasksRepository();
            this.collaborations = new CollaborationRepository();

        } else {

            this.users = loadedUsers;
            this.personalTasks = loadOrDefault(TASKS_FILE, PersonalTasksRepository.class,
                    PersonalTasksRepository::new);
            this.collaborations = loadOrDefault(COLLABS_FILE, CollaborationRepository.class,
                    CollaborationRepository::new);
        }
    }

    private <T> T loadOrDefault(String file, Class<T> repositoryType, Supplier<T> defaultCreator) {
        T loaded = loadRepository(file, repositoryType);
        if (loaded != null) {
            return loaded;
        }
        return defaultCreator.get();
    }

    private <T> T loadRepository(String fileName, Class<T> repositoryType) {
        Path filePath = Path.of(fileName);

        if (!Files.exists(filePath)) {
            return null;
        }

        try (var inputStreamReader = new InputStreamReader(Files.newInputStream(filePath))) {
            return GSON.fromJson(inputStreamReader, repositoryType);
        } catch (EOFException e) {
            return null;
        } catch (IOException e) {
            System.out.println("Could not load " + fileName + ".");
            return null;
        }
    }

    public void registerUser(String username, String encodedPassword) throws UserAlreadyExistsException {
        users.addUser(username, encodedPassword);
    }

    public User logInUser(String username, String password)
            throws UserDoesNotExistException, IncorrectPasswordException {

        return users.loginUser(username, password);
    }

    public Task addTask(String username, String collaboration, Task task) throws TaskAlreadyExistsException,
            CollaborationDoesNotExistException, UnauthorizedActionException, UserDoesNotExistException {

        if (collaboration == null || collaboration.isBlank()) {
            return personalTasks.addTask(username, task);
        }
        return collaborations.addTask(username, collaboration, task);
    }

    public Task updatePersonalTask(String username, Task task)
            throws TaskDoesNotExistException, NoChangesDetectedException {

        return personalTasks.updateTask(username, task);
    }

    public Task deleteTask(String username, TaskKey searchKey) throws TaskDoesNotExistException {
        return personalTasks.deleteTask(username, searchKey);
    }

    public Task getPersonalTask(String username, TaskKey searchKey) throws TaskDoesNotExistException {
        return personalTasks.getTask(username, searchKey);
    }

    public Set<Task> getTasks(String username, LocalDate date, Boolean completed, String collaboration)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        if (collaboration == null || collaboration.isBlank()) {
            return personalTasks.listTasks(username, date, completed);
        }
        return collaborations.listTasks(username, collaboration, date, completed);
    }

    public Set<Task> getDashBoard(String username) {
        return personalTasks.getDashBoard(username);
    }

    public Task finishTask(String username, TaskKey searchKey, String collaboration)
            throws TaskDoesNotExistException, CollaborationDoesNotExistException,
            UnauthorizedActionException, NoChangesDetectedException, UserDoesNotExistException {

        if (collaboration == null || collaboration.isBlank()) {
            return personalTasks.finishTask(username, searchKey);
        }
        return collaborations.finishTask(username, collaboration, searchKey);
    }

    public Collaboration addCollaboration(String username, String collaboration)
            throws CollaborationAlreadyExistsException {

        return collaborations.addCollaboration(username, collaboration);
    }

    public Collaboration deleteCollaboration(String username, String collaboration)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        return collaborations.deleteCollaboration(username, collaboration);
    }

    public Set<String> listCollaborations(String username) {
        return collaborations.listCollaborations(username);
    }

    public void addUserToCollaboration(String username, String collaboration, String personToAdd)
            throws UserDoesNotExistException, CollaborationDoesNotExistException,
            UserAlreadyExistInCollaboration, UnauthorizedActionException {

        if (!users.isUserRegistered(personToAdd)) {
            String message = String.format("A user with the username: %s does not exist.", personToAdd);
            throw new UserDoesNotExistException(message);
        }
        collaborations.addUser(username, collaboration, personToAdd);
    }

    public Set<String> listUsersFromCollaboration(String username, String collaboration)
            throws CollaborationDoesNotExistException, UnauthorizedActionException {

        return collaborations.listUsers(username, collaboration);
    }

    public Task assignTask(String username, String collaboration, TaskKey searchKey, String assignee)
            throws TaskDoesNotExistException, CollaborationDoesNotExistException,
            UnauthorizedActionException, UserDoesNotExistException, NoChangesDetectedException {

        return collaborations.assignTask(username, collaboration, searchKey, assignee);
    }

    private void saveRepository(Object repository, String fileName) {
        try (var outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Path.of(fileName)))) {
            GSON.toJson(repository, outputStreamWriter);
        } catch (IOException e) {
            System.out.println("" + fileName + ": " + e.getMessage());
        }
    }

    public void saveRepositories() {
        saveRepository(collaborations, COLLABS_FILE);
        saveRepository(personalTasks, TASKS_FILE);
        saveRepository(users, USERS_FILE);
    }

}
