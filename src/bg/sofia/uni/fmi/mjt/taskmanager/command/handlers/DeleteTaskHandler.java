package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;

public class DeleteTaskHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of(DATE_ATTRIBUTE));

    private static final String SUCCESS_MESSAGE =
            "%s was deleted successfully.";

    public DeleteTaskHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String taskName = args.get(NAME_ATTRIBUTE);
        String dateString = args.get(DATE_ATTRIBUTE);

        try {

            TaskKey searchKey = TaskKey.of(taskName, getDateFromString(dateString));
            Task deletedTask = storage.deleteTask(identifier, searchKey);
            return String.format(SUCCESS_MESSAGE, deletedTask.mapTask());

        } catch (DateTimeParseException e) {

            return String.format(UNSUCCESS_MESSAGE, INVALID_DATE_FORMAT_MESSAGE);

        } catch (TaskDoesNotExistException | InvalidArgumentsForCommandException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        } catch (Exception e) {

            return String.format(FATAL_ERROR_MESSAGE);

        }
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}
