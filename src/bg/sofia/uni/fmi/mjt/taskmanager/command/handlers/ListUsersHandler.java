package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;

import java.util.Map;
import java.util.Set;

public class ListUsersHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(COLLABORATION_ATTRIBUTE),
            Set.of());

    private static final String SUCCESS_MESSAGE =
            "%s 's users: %s";

    public ListUsersHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String collaboration = args.get(COLLABORATION_ATTRIBUTE);

        try {
            Set<String> usersFromCollaboration = storage.listUsersFromCollaboration(identifier, collaboration);
            String result = String.join(", ", usersFromCollaboration);
            return String.format(SUCCESS_MESSAGE, collaboration, result);

        } catch (CollaborationDoesNotExistException | UnauthorizedActionException e) {

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
