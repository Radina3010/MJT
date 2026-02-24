package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;

import java.util.Map;
import java.util.Set;

public class ListCollaborationsHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    public ListCollaborationsHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        Set<String> collaborations = storage.listCollaborations(identifier);

        if (collaborations.isEmpty()) {
            return "No collaborations found";
        }

        String result = String.join(", ", collaborations);
        return "Your collaborations: " + result;

    }

}
