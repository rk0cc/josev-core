package xyz.rk0cc.josev.constraint;

import org.junit.jupiter.api.*;

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
}
