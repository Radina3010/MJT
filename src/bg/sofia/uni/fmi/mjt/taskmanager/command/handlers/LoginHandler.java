package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.IncorrectPasswordException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;

import java.util.Map;
import java.util.Set;

public class LoginHandler implements CommandHandler {

    private final TaskManagerStorage storage;
    private final Map<String, String> loggedInUsers;

    private static final CommandRules RULES = new CommandRules(
            Set.of(USERNAME_ATTRIBUTE, PASSWORD_ATTRIBUTE),
            Set.of());

    private static final String SUCCESS_MESSAGE =
            "Welcome %s.";

    public LoginHandler(TaskManagerStorage storage, Map<String, String> loggedInUsers) {
        this.storage = storage;
        this.loggedInUsers = loggedInUsers;
    }

    @Override
    public String handle(Map<String, String> args, String sessionId) {
        String username = args.get(USERNAME_ATTRIBUTE);
        String password = args.get(PASSWORD_ATTRIBUTE);

        try {

            storage.logInUser(username, password);
            loggedInUsers.put(sessionId, username);
            return String.format(SUCCESS_MESSAGE, username);

        }  catch (IncorrectPasswordException | UserDoesNotExistException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        }  catch (Exception e) {

            return FATAL_ERROR_MESSAGE;

        }
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}
