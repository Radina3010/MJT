package bg.sofia.uni.fmi.mjt.taskmanager.extractors;

import java.util.Optional;

public interface Extractor<T> {

    Optional<T> extract(String value);

}
