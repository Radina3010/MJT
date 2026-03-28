package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Collaboration;

import java.util.Map;
import java.util.Set;

public class AddCollaborationHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of());

    private static final String SUCCESS_MESSAGE = "Collaboration with the name %s was created successfully.";

    public AddCollaborationHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String collaborationName = args.get(NAME_ATTRIBUTE);

        try {

            Collaboration collab = storage.addCollaboration(identifier, collaborationName);
            return String.format(SUCCESS_MESSAGE, collaborationName);

        } catch (CollaborationAlreadyExistsException e) {

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
