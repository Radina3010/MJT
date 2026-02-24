package bg.sofia.uni.fmi.mjt.taskmanager.command;

import bg.sofia.uni.fmi.mjt.taskmanager.exception.InvalidArgumentsForCommandException;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandCreatorTest {

    @Test
    void newCommandWithEmptyStringTest() {
        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandCreator.newCommand(null),
                "CommandCreator's newCommand should throw InvalidArgumentsForCommandException when called with empty string.");
    }

    @Test
    void newCommandWithOnlySpacesTest() {
        assertThrows(InvalidArgumentsForCommandException.class,
                () -> CommandCreator.newCommand("   "),
                "CommandCreator's newCommand should throw InvalidArgumentsForCommandException when called with blank string.");
    }

    @Test
    void newCommandNoArgumentsTest() throws InvalidArgumentsForCommandException {
        String input = "login";
        Command actual = CommandCreator.newCommand(input);

        assertEquals("login", actual.command(),
                "CommandCreator's newCommand expected to return command with name 'login' but returned " + actual.command());

        assertTrue(actual.arguments().isEmpty(),
                "CommandCreator's newCommand expected to return empty parameters map for command with no arguments.");
    }

    @Test
    void newCommandWithValidArgumentsTest() throws InvalidArgumentsForCommandException {
        String input = "register --username=user1 --password=1234";
        Command actual = CommandCreator.newCommand(input);

        assertEquals("register", actual.command(),
                "CommandCreator's newCommand expected to return command with name 'register' but returned " + actual.command());

        Map<String, String> arguments = actual.arguments();

        assertEquals(2, arguments.size(),
                "Command arguments map should contain exactly 2 entries.");

        String usernameArgument = arguments.get("username");
        assertEquals("user1", usernameArgument,
                "Expected argument 'username' to be 'user1' but returned " + usernameArgument);

        String passwordArgument = arguments.get("password");
        assertEquals("1234", passwordArgument,
                "Expected argument 'password' to be '1234' but returned " + passwordArgument);
    }

    @Test
    void newCommandWithQuotedArgumentsTest() throws InvalidArgumentsForCommandException {
        String input = "add-task --name=\"task name\" --description=\"testing the quotes\"";
        Command actual = CommandCreator.newCommand(input);

        assertEquals("add-task", actual.command(),
                "CommandCreator's newCommand expected to return command with name 'add-task'.");

        Map<String, String> arguments = actual.arguments();

        assertEquals("task name", arguments.get("name"),
                "CommandCreator should handle spaces correctly when they are inside quotes.");

        assertEquals("testing the quotes", arguments.get("description"),
                "CommandCreator should handle spaces correctly when they are inside quotes.");
    }

    @Test
    void newCommandWithEmptyQuotedArgumentTest() throws InvalidArgumentsForCommandException {
        String input = "update-task --description=\"\"";
        Command actual = CommandCreator.newCommand(input);

        assertEquals("", actual.arguments().get("description"),
                "CommandCreator should handle empty strings inside quotes correctly.");
    }

    @Test
    void newCommandShouldIgnoreArgumentsWithoutDoubleDashTest() throws InvalidArgumentsForCommandException {

        String input = "login --username=user1 something --password=1234";
        Command actual = CommandCreator.newCommand(input);

        Map<String, String> arguments = actual.arguments();

        assertEquals(2, arguments.size(),
                "CommandCreator should ignore tokens that do not start with '--'.");

        assertTrue(arguments.containsKey("username") && arguments.containsKey("password"),
                "Only valid arguments should be present in the map.");
    }

    @Test
    void newCommandShouldIgnoreArgumentsWithoutEqualsSignTest() throws InvalidArgumentsForCommandException {
        String input = "list-users --collaboration ";
        Command actual = CommandCreator.newCommand(input);

        Map<String, String> arguments = actual.arguments();

        assertEquals(0, arguments.size(),
                "CommandCreator's newCommand should ignore arguments that do not contain '='.");
    }

    @Test
    void newCommandWithMultipleSpacesTest() throws InvalidArgumentsForCommandException {

        String input = "login   --username=user1";
        Command actual = CommandCreator.newCommand(input);

        assertEquals("login", actual.command(),
                "Command name should be parsed correctly even with multiple spaces.");

        assertEquals("user1", actual.arguments().get("username"),
                "Arguments should be parsed correctly even with multiple spaces.");
    }

}