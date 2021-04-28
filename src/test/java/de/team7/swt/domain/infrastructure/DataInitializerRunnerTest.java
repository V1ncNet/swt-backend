package de.team7.swt.domain.infrastructure;

import de.team7.swt.domain.infrastructure.DataInitializerRunner.NoopDataInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.data.util.Streamable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

/**
 * @author Vincent Nadoll
 */
class DataInitializerRunnerTest {

    private ApplicationArguments args;
    private DataInitializerRunner runner;

    @BeforeEach
    void setUp() {
        args = new DefaultApplicationArguments();
        runner = new DataInitializerRunner();
    }

    @Test
    void defaultOrder_shouldMatchOrderConstant() {
        assertEquals(DataInitializerRunner.ORDER, runner.getOrder());
    }

    @Test
    void initializeNullArguments_shouldNotTrowException() {
        assertDoesNotThrow(() -> runner.setDataInitializers(null));
    }

    @Test
    void name() {
        DataInitializer noopInitializer = spy(new NoopDataInitializer());
        DataInitializer orderedInitializer = spy(new DataInitializer() {
            @Override
            public void initialize() {
            }

            @Override
            public int getOrder() {
                return NoopDataInitializer.DEFAULT_ORDER + 1;
            }
        });
        InOrder inOrder = Mockito.inOrder(noopInitializer, orderedInitializer);

        runner.setDataInitializers(Streamable.of(noopInitializer, orderedInitializer).toSet());
        runner.run(args);

        inOrder.verify(noopInitializer, times(1)).initialize();
        inOrder.verify(orderedInitializer, times(1)).initialize();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void runTwice_shouldThrowException() {
        assertDoesNotThrow(() -> runner.run(args));
        assertThrows(UnsupportedOperationException.class, () -> runner.run(args));
    }
}
