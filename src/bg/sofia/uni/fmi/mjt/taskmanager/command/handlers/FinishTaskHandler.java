package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.util.Map;
import java.util.Set;

public class FinishTaskHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final String SUCCESS_MESSAGE =
            "Task finished successfully! %s";
    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of(DATE_ATTRIBUTE, COLLABORATION_ATTRIBUTE));

    public FinishTaskHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String taskName = args.get(NAME_ATTRIBUTE);
        String dateString = args.get(DATE_ATTRIBUTE);
        String collaboration = args.get(COLLABORATION_ATTRIBUTE);

        try {

            TaskKey searchKey = TaskKey.of(taskName, getDateFromString(dateString));
            Task finishedTask = storage.finishTask(identifier, searchKey, collaboration);
            return String.format(SUCCESS_MESSAGE, finishedTask.mapTask());

        } catch (TaskDoesNotExistException | CollaborationDoesNotExistException
                 | UnauthorizedActionException | InvalidArgumentsForCommandException e) {

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
