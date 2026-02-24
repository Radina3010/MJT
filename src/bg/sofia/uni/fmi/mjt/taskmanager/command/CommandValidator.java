package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandValidator {

    public static void validateCommand(Command command, CommandRules rules) throws InvalidArgumentsForCommandException {

        Map<String, String> arguments = command.arguments();
        Set<String> providedKeys = arguments.keySet();

        if (!providedKeys.containsAll(rules.required())) {
            Set<String> missing = new HashSet<>(rules.required());
            missing.removeAll(providedKeys);
            throw new InvalidArgumentsForCommandException("Missing required arguments: " + missing);
        }

        Set<String> unknown = new HashSet<>(providedKeys);
        unknown.removeAll(rules.allAllowed());

        if (!unknown.isEmpty()) {
            throw new InvalidArgumentsForCommandException("Unknown arguments provided: " + unknown);
        }

        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                throw new InvalidArgumentsForCommandException(
                        String.format("Argument '%s' cannot be empty or blank.", entry.getKey()));
            }
        }

    }

}