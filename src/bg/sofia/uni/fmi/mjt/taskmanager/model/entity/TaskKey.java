package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

public record TaskKey(String name, LocalDate date) implements Serializable {

    private static final String TASK_EMPTY_MESSAGE =
            "Task name can not be null or empty.";
    private static final String EMPTY_DATE_STRING =
            "NO DATE";

    public TaskKey {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(TASK_EMPTY_MESSAGE);
        }
    }

    public static TaskKey of(String name, LocalDate date) {
        return new TaskKey(name, date);
    }

    public String formatExceptionMessage(String template) {
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("Error template cannot be null or empty.");
        }

        String dateStr = date != null ? date.toString() : EMPTY_DATE_STRING;
        return String.format(template, name, dateStr);
    }

    public String formatExceptionMessage(String template, String outsideData) {
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("Error template cannot be null or empty.");
        }

        String dateStr = date != null ? date.toString() : EMPTY_DATE_STRING;
        return String.format(template, name, dateStr, outsideData);
    }

}
