package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.TaskDoesNotExistException;
import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.Task;
import bg.sofia.uni.fmi.mjt.taskmanager.model.entity.TaskKey;

import java.util.Map;
import java.util.Set;

public class GetTaskHandler implements  CommandHandler {

    private final TaskManagerStorage storage;

    private static final CommandRules RULES = new CommandRules(
            Set.of(NAME_ATTRIBUTE),
            Set.of(DATE_ATTRIBUTE));

    public GetTaskHandler(TaskManagerStorage storage) {
        this.storage = storage;
    }

    @Override
    public String handle(Map<String, String> args, String identifier) {
        String taskName = args.get(NAME_ATTRIBUTE);
        String dateString = args.get(DATE_ATTRIBUTE);

        try {

            TaskKey searchKey = TaskKey.of(taskName, getDateFromString(dateString));
            Task task = storage.getPersonalTask(identifier, searchKey);
            return task.mapTask();

        }  catch (TaskDoesNotExistException | InvalidArgumentsForCommandException e) {

            return String.format(UNSUCCESS_MESSAGE, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return FATAL_ERROR_MESSAGE;

        }
    }

    @Override
    public CommandRules getRules() {
        return RULES;
    }

}
