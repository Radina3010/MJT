package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandValidatorTest {

    @Mock
    Command command;

    @Mock
    CommandRules rules;

    @Test
    void validateCommandMissingRequiredArgumentsTest() {
        when(rules.required()).thenReturn(Set.of("username", "password"));

        when(command.arguments()).thenReturn(Collections.emptyMap());

        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should throw exception when required argument is missing.");
    }

    @Test
    void validateCommandUnknownArgumentTest() {
        when(rules.required()).thenReturn(Collections.emptySet());

        when(command.arguments()).thenReturn(Map.of("username", "user1"));

        when(rules.allAllowed()).thenReturn(Collections.emptySet());

        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should throw exception when 'username' is provided but not allowed in rules.");
    }

    @Test
    void validateCommandValidTest() {
        when(rules.required()).thenReturn(Set.of("username"));

        when(command.arguments()).thenReturn(Map.of("username", "user1", "password", "1234"));

        when(rules.allAllowed()).thenReturn(Set.of("username", "password"));

        assertDoesNotThrow(() -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should not throw exception when all arguments are allowed.");
    }

    @Test
    void validateCommandWithPartialOptionalArgumentsTest() {
        when(rules.required()).thenReturn(Collections.emptySet());

        when(command.arguments()).thenReturn(Map.of("username", "user1"));

        when(rules.allAllowed()).thenReturn(Set.of("username", "password", "task"));

        assertDoesNotThrow(() -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should allow partial optional arguments.");
    }

    @Test
    void validateCommandWithNullValuesTest() {

        when(rules.required()).thenReturn(Collections.emptySet());
        when(rules.allAllowed()).thenReturn(Set.of("username", "password"));

        Map<String, String> arguments = new HashMap<>();
        arguments.put("username", null);

        when(command.arguments()).thenReturn(arguments);

        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should throw InvalidArgumentsForCommandException when when there is null value for required or optional arguments.");
    }

    @Test
    void validateCommandWithBlankValuesTest() {
        when(rules.required()).thenReturn(Collections.emptySet());

        when(command.arguments()).thenReturn(Map.of("username", "    "));

        when(rules.allAllowed()).thenReturn(Set.of("username", "password"));

        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandValidator.validateCommand(command, rules),
                "CommandValidator should throw InvalidArgumentsForCommandException when when there is blank value for required or optional arguments.");
    }

}