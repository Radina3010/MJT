package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.NoChangesDetectedException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UserDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.CollaborationDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.UnauthorizedActionException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.util.Map;
import java.util.Set;

public class AssignTaskHandler implements CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(COLLABORATION_ATTRIBUTE, TASK_ATTRIBUTE, USER_ATTRIBUTE),
            Set.of(DATE_ATTRIBUTE));

    private static final String SUCCESS_MESSAGE =
            " %s was assigned to %s successfully.";

    public AssignTaskHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String collaboration = args.get(COLLABORATION_ATTRIBUTE);
        String task = args.get(TASK_ATTRIBUTE);
        String user = args.get(USER_ATTRIBUTE);
        String dateString = args.get(DATE_ATTRIBUTE);

        try {
            TaskKey searchKey = TaskKey.of(task, getDateFromString(dateString)) ;
            Task assignedTask = storage.assignTask(identifier, collaboration, searchKey, user);
            return String.format(SUCCESS_MESSAGE, assignedTask.mapTask(), user);

        }  catch (TaskDoesNotExistException | CollaborationDoesNotExistException | UnauthorizedActionException |
                  InvalidArgumentsForCommandException | UserDoesNotExistException | NoChangesDetectedException e) {

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
