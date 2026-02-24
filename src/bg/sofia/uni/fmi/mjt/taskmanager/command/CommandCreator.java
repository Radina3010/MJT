package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandCreator {

    private static final String UNSUCCESS_MESSAGE =
            "Please enter a command!";

    private static final String PREFIX = "--";
    private static final String SEPARATOR = "=";

    public static Command newCommand(String clientInput) throws InvalidArgumentsForCommandException {
        if (clientInput == null || clientInput.isBlank()) {
            throw new InvalidArgumentsForCommandException(UNSUCCESS_MESSAGE);
        }

        List<String> tokens = getCommandArguments(clientInput);
        String commandName = tokens.getFirst();

        Map<String, String> params = new HashMap<>();

        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.startsWith(PREFIX)) {
                String[] parts = token.substring(2).split(SEPARATOR, 2);
                if (parts.length == 2) {
                    params.put(parts[0], parts[1]);
                }
            }
        }

        return new Command(commandName, params);
    }

    private static List<String> getCommandArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean insideQuote = false;

        for (char c : input.toCharArray()) {
            if (c == '"') {
                insideQuote = !insideQuote;
            }
            if (c == ' ' && !insideQuote) {
                tokens.add(sb.toString().replace("\"", ""));
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().replace("\"", ""));
        return tokens;
    }

}