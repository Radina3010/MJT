package bg.sofia.uni.fmi.mjt.taskmanager.model.entity;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.DueDateBeforeDateException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidDateFormatException;
import bg.sofia.uni.fmi.mjt.taskmanager.extractors.Extractor;
import bg.sofia.uni.fmi.mjt.taskmanager.extractors.LocalDateExtractor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Task {

    private final String name;
    private final LocalDate date;
    private final LocalDate dueDate;
    private final String description;
    private final boolean completed;
    private final String assignee;

    private static final Extractor<LocalDate> LOCAL_DATE_EXTRACTOR = new LocalDateExtractor();

    private static final String DUE_DATE_BEFORE_DATE_MESSAGE =
            "Due date can not be before the start date.";

    private static final String TASK_EMPTY_MESSAGE =
            "Task name can not be null or empty.";

    public Task(String name, LocalDate date, LocalDate dueDate, String description,
                boolean completed, String assignee) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(TASK_EMPTY_MESSAGE);
        }
        this.name = name;

        this.date = date;
        this.dueDate = dueDate;
        this.description = description;
        this.completed = completed;
        this.assignee = assignee;
    }

    public TaskKey getKey() {
        return TaskKey.of(name, date);
    }

    public Task withCompleted(boolean status) {
        return new Task(name, date, dueDate, description, status, assignee);
    }

    public Task withAssignee(String newAssignee) {
        return new Task(name, date, dueDate, description, completed, newAssignee);
    }

    public String mapTask() {
        StringBuilder taskAsString = new StringBuilder("Task name: " + name);

        appendIfArgumentIsPresent(taskAsString, " Date: ", date);
        appendIfArgumentIsPresent(taskAsString, " Due date: ", dueDate);
        appendIfArgumentIsPresent(taskAsString, " Description: ", description);

        taskAsString.append(" Completed status: ").append(completed);

        appendIfArgumentIsPresent(taskAsString, " Assignee: ", assignee);

        return taskAsString.toString();
    }

    public boolean matches(LocalDate searchDate, Boolean searchCompleted) {
        return dateMatches(searchDate) && completesMatches(searchCompleted);
    }

    public boolean matches(LocalDate searchDate) {
        return dateMatches(searchDate);
    }

    private boolean dateMatches(LocalDate searchDate) {
        return (searchDate == null) ||
                (this.date != null && this.date.equals(searchDate));
    }

    private boolean completesMatches(Boolean searchCompleted) {
        return (searchCompleted == null) ||
                (searchCompleted.equals(this.completed));
    }

    private <T> void appendIfArgumentIsPresent(StringBuilder taskAsString,
                                                      String argumentName, T argument) {
        if (argument != null) {
            taskAsString.append(argumentName).append(argument);
        }
    }

    public static Task of(String name, String date, String dueDate, String description)
            throws InvalidDateFormatException, DueDateBeforeDateException, InvalidArgumentsForCommandException {

        Optional<LocalDate> dateArgument = LOCAL_DATE_EXTRACTOR.extract(date);
        Optional<LocalDate> dueDateArgument = LOCAL_DATE_EXTRACTOR.extract(dueDate);

        if (dateArgument.isPresent() && dueDateArgument.isPresent()) {
            if (dueDateArgument.get().isBefore(dateArgument.get())) {
                throw new DueDateBeforeDateException(DUE_DATE_BEFORE_DATE_MESSAGE);
            }
        }

        return new Task(
                name,
                dateArgument.orElse(null),
                dueDateArgument.orElse(null),
                description,
                false,
                null
        );

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return completed == task.completed
                && Objects.equals(name, task.name)
                && Objects.equals(date, task.date)
                && Objects.equals(dueDate, task.dueDate)
                && Objects.equals(description, task.description)
                && Objects.equals(assignee, task.assignee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, dueDate, description, completed, assignee);
    }

    public String getName() {
        return name;
    }

    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public Optional<LocalDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public boolean isCompleted() {
        return completed;
    }

    public Optional<String> getAssignee() {
        return Optional.ofNullable(assignee);
    }

}
