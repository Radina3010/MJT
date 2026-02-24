package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.DueDateBeforeDateException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidDateFormatException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;

public class UpdateTaskHandler implements  CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of(DATE_ATTRIBUTE, DUE_DATE_ATTRIBUTE, DESCRIPTION_ATTRIBUTE));

    private static final String SUCCESS_MESSAGE =
            "Your have updated task successfully. %s";

    public UpdateTaskHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String taskName = args.get(NAME_ATTRIBUTE);
        String date = args.get(DATE_ATTRIBUTE);
        String dueDate = args.get(DUE_DATE_ATTRIBUTE);
        String description = args.get(DESCRIPTION_ATTRIBUTE);

        try {

            Task updatedTask = Task.of(taskName, date, dueDate, description);
            storage.updatePersonalTask(identifier, updatedTask);
            return String.format(SUCCESS_MESSAGE, updatedTask.mapTask());

        } catch (DateTimeParseException e) {

            return String.format(UNSUCCESS_MESSAGE, INVALID_DATE_FORMAT_MESSAGE);

        } catch (TaskDoesNotExistException | NoChangesDetectedException | InvalidDateFormatException |
                 DueDateBeforeDateException | InvalidArgumentsForCommandException e) {

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
