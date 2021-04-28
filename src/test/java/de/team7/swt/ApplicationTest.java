package de.team7.swt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Vincent Nadoll
 */
@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void arbitraryMainExecution_shouldNotThrowException() {
        assertDoesNotThrow(() -> Application.main(new String[]{}));
    }
}
