package xyz.rk0cc.josev;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class SemVerParseTest {
    @DisplayName("Get semver from constructor")
    @Order(1)
    @Test
    void testConstructSemVer() {
        assertDoesNotThrow(() -> new SemVer(1, 0, 0, null, "1"));
        assertDoesNotThrow(() -> new SemVer(1, 2, 1, "rc.1", null));
        assertDoesNotThrow(() -> new SemVer(2, 0, 3, "rc.2", "1"));
        assertDoesNotThrow(() -> new SemVer(0, 0, 2, "---rc.2", "1"));
        assertDoesNotThrow(() -> new SemVer(0, 25, 3, "pre-beta", null));
        assertDoesNotThrow(() -> new SemVer(3, 26, 12, null, "build-foo"));
        assertDoesNotThrow(() -> new SemVer(99, 99, 32, "RTM", "build.X"));
        assertDoesNotThrow(() -> new SemVer(5, 6, 9, null, "1a.f.2"));
        assertThrows(
                NonStandardSemVerException.class,
                () -> new SemVer(1, 4, 6, "beta...3", null)
        );
        assertThrows(
                NonStandardSemVerException.class,
                () -> new SemVer(1, 9, 3, null, "_sike")
        );
        assertThrows(
                NonStandardSemVerException.class,
                () -> new SemVer(2, 12, 0, "nullsafety--3--", "+123456789")
        );
        assertThrows(
                NonStandardSemVerException.class,
                () -> new SemVer(0x0f, 0x20, 0x5c, "alpha+build", "wtf")
        );
        assertThrows(
                NonStandardSemVerException.class,
                () -> new SemVer(0b00101001, 0b00010011, 0b10000100, "d._.a", null)
        );
    }

    @DisplayName("Get semver from parser")
    @Order(2)
    @Test
    void testSemVerParser() {
        try {
            CSVReader csvr = new CSVReader(
                    new FileReader(
                            Paths.get(
                                    Objects.requireNonNull(
                                            SemVerParseTest.class.getResource("semver_sample.csv")
                                    ).toURI()
                            ).toFile()
                    )
            );
            List<String[]> csvrow = csvr.readAll();

            for (String[] csvp : csvrow) {
                boolean expectedParsed = Boolean.parseBoolean(csvp[1]);

                if (expectedParsed) assertDoesNotThrow(() -> SemVer.parse(csvp[0]));
                else assertThrows(
                        NonStandardSemVerException.class,
                        () -> SemVer.parse(csvp[0])
                );
            }
        } catch (URISyntaxException | IOException | CsvException e) {
            fail(e);
        }
    }

    @DisplayName("Check semver try parse that return null if invalid")
    @Order(3)
    @Test
    void testTryParse() {
        assertNotNull(SemVer.tryParse("1.0.0"));
        assertNull(SemVer.tryParse("1.0"));
    }

    @DisplayName("Semver compare matching")
    @Order(4)
    @Test
    void testCompare() {
        try {
            assertTrue(SemVer.parse("1.0.0").isGreater(SemVer.parse("1.0.0-alpha")));
            assertFalse(SemVer.parse("1.0.0").isGreater(SemVer.parse("1.0.0")));
            assertTrue(SemVer.parse("1.0.0").isGreaterOrEquals(SemVer.parse("1.0.0")));
            assertEquals(SemVer.parse("1.0.0"), SemVer.parse("1.0.0"));
            assertFalse(SemVer.parse("1.0.0-bravo").isLower(SemVer.parse("1.0.0-charlie")));
            assertFalse(SemVer.parse("1.0.1").isGreater(SemVer.parse("1.0.1+1")));
        } catch (NonStandardSemVerException e) {
            fail(e);
        }
    }
}
