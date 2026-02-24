package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserAlreadyExistInCollaboration;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;

import java.util.Map;
import java.util.Set;

public class AddUserHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(COLLABORATION_ATTRIBUTE, USER_ATTRIBUTE),
            Set.of());

    private static final String SUCCESS_MESSAGE = "%s was added to %s successfully.";

    public AddUserHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String collaborationName = args.get(COLLABORATION_ATTRIBUTE);
        String userToAdd = args.get(USER_ATTRIBUTE);

        try {

            storage.addUserToCollaboration(identifier, collaborationName, userToAdd);
            return String.format(SUCCESS_MESSAGE, userToAdd, collaborationName);

        } catch (UserDoesNotExistException | CollaborationDoesNotExistException |
                UserAlreadyExistInCollaboration | UnauthorizedActionException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        } catch (Exception e) {

            return FATAL_ERROR_MESSAGE;

        }
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}
