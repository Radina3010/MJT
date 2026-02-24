package bg.sofia.uni.fmi.mjt.taskmanager.extractors;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanExtractorTest {

    Extractor<Boolean> underTest = new BooleanExtractor();

    @Test
    void extractWithNullTest() {
        Optional<Boolean> expected = Optional.empty();
        Optional<Boolean> actual = underTest.extract(null);
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return Optional.empty() when called with null.");
    }

    @Test
    void extractWithBlankStringTest() {
        Optional<Boolean> expected = Optional.empty();
        Optional<Boolean> actual = underTest.extract("   ");
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return Optional.empty() when called with blank String.");
    }

    @Test
    void extractWithTrueTest() {
        Optional<Boolean> expected = Optional.of(Boolean.TRUE);
        Optional<Boolean> actual = underTest.extract("true");
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return true when called with 'true'.");
    }

    @Test
    void extractWithTrueCaseInsensitiveTest() {
        Optional<Boolean> expected = Optional.of(Boolean.TRUE);
        Optional<Boolean> actual = underTest.extract("tRuE");
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return true when called with 'tRuE'.");
    }

    @Test
    void extractWithFalseTest() {
        Optional<Boolean> expected = Optional.of(Boolean.FALSE);
        Optional<Boolean> actual = underTest.extract("false");
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return false when called with 'false'.");
    }

    @Test
    void extractWithFalseCaseInsensitiveTest() {
        Optional<Boolean> expected = Optional.of(Boolean.FALSE);
        Optional<Boolean> actual = underTest.extract("FalSe");
        assertEquals(expected, actual,
                "BooleanExtractor's method extract should return false when called with 'FalSe'.");
    }

    @Test
    void extractWithInvalidBooleanValueTest() {
        assertThrows(IllegalArgumentException.class,
                () -> underTest.extract("value"),
                "BooleanExtractor's method extract should throw IllegalArgumentException when called with String different than 'true' or 'false' (case insensitive).");
    }

}