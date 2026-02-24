package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListTasksHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(),
            Set.of(DATE_ATTRIBUTE, COMPLETED_ATTRIBUTE, COLLABORATION_ATTRIBUTE));

    private static final String NO_TASK_FOUND_SUCCESS_MESSAGE =
            "No tasks found.";
    private static final String SUCCESS_MESSAGE =
            "Found a total of %d tasks. %s";

    public ListTasksHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String date = args.get(DATE_ATTRIBUTE);
        String completed = args.get(COMPLETED_ATTRIBUTE);
        String collaboration = args.get(COLLABORATION_ATTRIBUTE);

        try {
            Set<Task> tasks = storage.getTasks(
                    identifier, getDateFromString(date), getBooleanFromString(completed), collaboration);

            if (tasks.isEmpty()) {
                return NO_TASK_FOUND_SUCCESS_MESSAGE;
            }

            String tasksString = tasks.stream()
                    .map(Task::mapTask)
                    .collect(Collectors.joining("\n"));

            return String.format(SUCCESS_MESSAGE, tasks.size(), tasksString);

        } catch (CollaborationDoesNotExistException | UnauthorizedActionException |
                 InvalidArgumentsForCommandException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        }  catch (Exception e) {

            return FATAL_ERROR_MESSAGE;

        }
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}
