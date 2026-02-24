package bg.sofia.uni.fmi.mjt.taskmanager.extractors;

import java.time.LocalDate;
import java.util.Optional;

public class LocalDateExtractor implements Extractor<LocalDate> {

    @Override
    public Optional<LocalDate> extract(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(LocalDate.parse(value));
    }

}
