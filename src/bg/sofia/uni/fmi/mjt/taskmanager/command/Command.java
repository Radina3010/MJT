package bg.sofia.uni.fmi.mjt.taskmanager.command;

import java.util.Map;

public record Command(String command, Map<String, String> arguments) {

}