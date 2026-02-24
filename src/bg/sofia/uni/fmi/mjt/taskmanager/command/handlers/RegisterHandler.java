package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistsException;

import java.util.Map;
import java.util.Set;

public class RegisterHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(USERNAME_ATTRIBUTE, PASSWORD_ATTRIBUTE),
            Set.of());
    private static final String SUCCESS_MESSAGE =
            "Your have registered successfully.";

    public RegisterHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String username = args.get(USERNAME_ATTRIBUTE);
        String password = args.get(PASSWORD_ATTRIBUTE);

        try {

            storage.registerUser(username, password);
            return SUCCESS_MESSAGE;

        } catch (UserAlreadyExistsException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        } catch (Exception e) {

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
