package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.model.TaskManagerStorage;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.AddCollaborationHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.AddTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.AddUserHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.AssignTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.CommandHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.DeleteCollaborationHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.DeleteTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.FinishTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.HelpHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.ListCollaborationsHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.ListDashboardHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.GetTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.ListTasksHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.ListUsersHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.LoginHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.RegisterHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.UpdateTaskHandler;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;

import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.taskmanager.command.handlers.CommandHandler.UNSUCCESS_MESSAGE;

public class CommandExecutor {

    private final Map<String, CommandHandler> availableCommands = new HashMap<>();
    private final Map<String, String> loggedInUsers = new HashMap<>();

    public CommandExecutor(TaskManagerStorage storage) {
        availableCommands.put("register", new RegisterHandler(storage));
        availableCommands.put("login", new LoginHandler(storage, loggedInUsers));
        availableCommands.put("add-task", new AddTaskHandler(storage));
        availableCommands.put("update-task", new UpdateTaskHandler(storage));
        availableCommands.put("delete-task", new DeleteTaskHandler(storage));
        availableCommands.put("get-task", new GetTaskHandler(storage));
        availableCommands.put("list-tasks", new ListTasksHandler(storage));
        availableCommands.put("list-dashboard", new ListDashboardHandler(storage));
        availableCommands.put("finish-task", new FinishTaskHandler(storage));
        availableCommands.put("add-collaboration", new AddCollaborationHandler(storage));
        availableCommands.put("delete-collaboration", new DeleteCollaborationHandler(storage));
        availableCommands.put("list-collaborations", new ListCollaborationsHandler(storage));
        availableCommands.put("add-user", new AddUserHandler(storage));
        availableCommands.put("assign-task", new AssignTaskHandler(storage));
        availableCommands.put("list-users", new ListUsersHandler(storage));
        availableCommands.put("help", new HelpHandler());
    }

    public String execute(Command command, String sessionId) {
        String username = loggedInUsers.get(sessionId);
        boolean isLoggedIn = (username != null);
        CommandHandler handler = availableCommands.get(command.command());

        if (handler == null) {
            return String.format(CommandHandler.UNSUCCESS_MESSAGE,
                    "Unknown command. Type 'help' to see available commands.");
        }
        try {
            CommandValidator.validateCommand(command, handler.getRules());
        } catch (InvalidArgumentsForCommandException e) {
            return String.format(UNSUCCESS_MESSAGE, e.getMessage());
        }

        if (!command.command().equals("help")) {

            if (isLoggedIn && handler.isPublic()) {
                String reason = String.format("You are already logged in as %s.", username);
                return String.format(CommandHandler.UNSUCCESS_MESSAGE, reason);
            }

            if (!isLoggedIn && !handler.isPublic()) {
                return String.format(CommandHandler.UNSUCCESS_MESSAGE, "Please login first.");
            }

        }
        String identifier = isLoggedIn ? username : sessionId;
        return handler.handle(command.arguments(), identifier);
    }

    public void removeSession(String sessionId) {
        loggedInUsers.remove(sessionId);
    }

}
