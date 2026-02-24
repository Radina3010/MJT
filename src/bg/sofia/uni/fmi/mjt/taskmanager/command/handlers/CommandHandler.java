package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import bg.sofia.uni.fmi.mjt.taskmanager.command.CommandRules;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.extractors.BooleanExtractor;
import bg.sofia.uni.fmi.mjt.taskmanager.extractors.Extractor;
import bg.sofia.uni.fmi.mjt.taskmanager.extractors.LocalDateExtractor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;

public interface CommandHandler {

    String UNSUCCESS_MESSAGE =
            "The last command couldn't be completed. %s";

    String INVALID_DATE_FORMAT_MESSAGE =
            "The date format must be YYYY-MM-dd";

    String FATAL_ERROR_MESSAGE =
            "Unexpected error occurred. Please try again.";

    Extractor<LocalDate> LOCAL_DATE_EXTRACTOR = new LocalDateExtractor();
    Extractor<Boolean> BOOLEAN_EXTRACTOR = new BooleanExtractor();

    String NAME_ATTRIBUTE = "name";
    String DATE_ATTRIBUTE = "date";
    String DUE_DATE_ATTRIBUTE = "due-date";
    String DESCRIPTION_ATTRIBUTE = "description";
    String COLLABORATION_ATTRIBUTE = "collaboration";
    String USER_ATTRIBUTE = "user";
    String TASK_ATTRIBUTE = "task";
    String COMPLETED_ATTRIBUTE = "completed";
    String USERNAME_ATTRIBUTE = "username";
    String PASSWORD_ATTRIBUTE = "password";

    String handle(Map<String, String> args, String identifier);

    default LocalDate getDateFromString(String dateString) throws InvalidArgumentsForCommandException {
        try {
            return LOCAL_DATE_EXTRACTOR.extract(dateString).orElse(null);
        } catch (DateTimeParseException e) {
            throw new InvalidArgumentsForCommandException(INVALID_DATE_FORMAT_MESSAGE);
        }
    }

    default Boolean getBooleanFromString(String input) throws InvalidArgumentsForCommandException {
        try {
            return BOOLEAN_EXTRACTOR.extract(input).orElse(null);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentsForCommandException("The only accepted value for --completed is true or false");
        }
    }

    default boolean isPublic() {
        return false;
    }

    default CommandRules getRules() {
        return new CommandRules(Set.of(), Set.of());
    }

}
