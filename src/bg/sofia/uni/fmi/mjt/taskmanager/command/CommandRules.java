package bg.sofia.uni.fmi.mjt.taskmanager.command;

import java.util.HashSet;
import java.util.Set;

public record CommandRules(Set<String> required, Set<String> optional) {

    public Set<String> allAllowed() {
        Set<String> all = new HashSet<>(required);
        all.addAll(optional);
        return all;
    }

}
