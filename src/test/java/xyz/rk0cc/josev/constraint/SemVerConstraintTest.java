package xyz.rk0cc.josev.constraint;

import org.junit.jupiter.api.*;
import xyz.rk0cc.josev.NonStandardSemVerException;
import xyz.rk0cc.josev.SemVer;
import xyz.rk0cc.josev.collections.SemVerMultipleRange;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class SemVerConstraintTest {
    @DisplayName("Test parser on implemented class")
    @Order(1)
    @Test
    void testSubclassParser() {
        assertDoesNotThrow(() -> SampleConstraint.parse(">=1.0.0 <1.1.0"));
        assertDoesNotThrow(() -> SampleConstraint.parse("^1.0.0"));
        assertThrows(
                AssertionError.class,
                () -> SampleConstraint.parse("12.2")
        );
    }

    @DisplayName("Test parser on parent class")
    @Order(2)
    @Test
    void testParentClassParser() {
        assertDoesNotThrow(() -> SemVerConstraint.parse(SampleConstraint.class, ">=2.0.0 <3.0.0"));
    }

    @DisplayName("Test in range condition with provided information about constraint")
    @Order(3)
    @Test
    void testRangeDetection() {
        final SampleConstraint sc = SampleConstraint.parse(">=1.0.0 <2.0.0");
        try {
            assertTrue(sc.isInRange("1.0.0"));
            assertFalse(sc.isInRange("1.0.0-rc.1"));
            assertTrue(sc.isInRange("1.0.0+1"));
            assertTrue(sc.isInRange("1.9.0"));
            assertTrue(sc.isInRange("1.0.1-beta"));
            assertFalse(sc.isInRange("0.9.0"));
            assertFalse(sc.isInRange("2.0.0"));
            assertFalse(sc.isInRange("2.0.0-alpha"));
        } catch (NonStandardSemVerException e) {
            fail(e);
        }
    }

    @DisplayName("Test multiple range")
    @Order(4)
    @Test
    void testMultipleRange() {
        SemVerMultipleRange<SampleConstraint> mr = new SemVerMultipleRange<>() {
            @Override
            public boolean isInRange(@Nonnull SemVer semVer) {
                return this.stream().anyMatch(s -> s.isInRange(semVer));
            }
        };

        mr.add(SampleConstraint.parse(">=1.0.0 <2.0.0"));
        mr.add(SampleConstraint.parse(">=3.0.0 <4.0.0"));

        try {
            assertTrue(mr.isInRange("1.0.0"));
            assertFalse(mr.isInRange("1.0.0-rc.1"));
            assertTrue(mr.isInRange("1.0.0+1"));
            assertTrue(mr.isInRange("1.9.0"));
            assertTrue(mr.isInRange("1.0.1-beta"));
            assertFalse(mr.isInRange("0.9.0"));
            assertFalse(mr.isInRange("2.0.0"));
            assertFalse(mr.isInRange("2.0.0-alpha"));
            assertTrue(mr.isInRange("3.0.0"));
            assertTrue(mr.isInRange("3.1.0"));
            assertFalse(mr.isInRange("4.0.0"));
        } catch (NonStandardSemVerException e) {
            fail(e);
        }
    }
}
