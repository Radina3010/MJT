package bg.sofia.uni.fmi.mjt.taskmanager.extractors;

import java.util.Optional;

public class BooleanExtractor implements Extractor<Boolean> {

    @Override
    public Optional<Boolean> extract(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true")) {
            return Optional.of(Boolean.valueOf(value));
        }

        throw new IllegalArgumentException("Only true or false are accepted as boolean value.");
    }

}
