package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;

import java.util.Map;
import java.util.Set;

public class DeleteCollaborationHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of());

    private static final String SUCCESS_MESSAGE = "Collaboration with the name %s was deleted successfully.";

    public DeleteCollaborationHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String collaborationName = args.get(NAME_ATTRIBUTE);

        try {

            storage.deleteCollaboration(identifier, collaborationName);
            return String.format(SUCCESS_MESSAGE, collaborationName);

        } catch (Exception e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        }
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}