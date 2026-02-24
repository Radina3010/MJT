package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListDashboardHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final String SUCCESS_MESSAGE =
            " Found a total of %d tasks. %s";

    public ListDashboardHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        Set<Task> tasks = storage.getDashBoard(identifier);

        if (tasks.isEmpty()) {
            return "No tasks were found for today. You can rest!";
        }

        String tasksString = tasks.stream()
                .map(Task::mapTask)
                .collect(Collectors.joining("\n"));

        return String.format(SUCCESS_MESSAGE, tasks.size(), tasksString);
    }

}
