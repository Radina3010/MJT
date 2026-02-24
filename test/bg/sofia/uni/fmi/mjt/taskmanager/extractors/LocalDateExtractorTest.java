package bg.sofia.uni.fmi.mjt.taskmanager.extractors;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateExtractorTest {

    Extractor<LocalDate> underTest = new LocalDateExtractor();

    @Test
    void extractWithNullTest() {
        Optional<LocalDate> expected = Optional.empty();
        Optional<LocalDate> actual = underTest.extract(null);
        assertEquals(expected, actual,
                "LocalDateExtractor's method extract should return Optional.empty() when called with null.");
    }

    @Test
    void extractWithBlankStringTest() {
        Optional<LocalDate> expected = Optional.empty();
        Optional<LocalDate> actual = underTest.extract("   ");
        assertEquals(expected, actual,
                "LocalDateExtractor's method extract should return Optional.empty() when called with blank String.");
    }

    @Test
    void extractValidDateTest() {
        Optional<LocalDate> expected = Optional.of(LocalDate.of(2026, 1, 1));
        Optional<LocalDate> actual = underTest.extract("2026-01-01");
        assertEquals(expected, actual,
                "LocalDateExtractor's method extract should return the correct LocalDate object when called with a valid date string.");
    }

    @Test
    void extractInvalidDateFormatTest() {
        assertThrows(DateTimeParseException.class,
                () -> underTest.extract("2026.01.01"),
                "LocalDateExtractor's method extract should throw DateTimeParseException when called with '2026.01.01'.The only accepted format is YYYY-MM-dd.");
    }

    @Test
    void extractInvalidStringTest() {
        assertThrows(DateTimeParseException.class,
                () -> underTest.extract("value"),
                "LocalDateExtractor's method extract should throw DateTimeParseException when called with String that is not a date.");
    }

}