package th.bingen.movs.vectorclocks.impl.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("Vector clock tests")
class ClockTest {


  private static VectorClock createStandardVectorClock() {
    return VectorClock.create(List.of("P1", "P2", "P3"));
  }

  @Nested
  @DisplayName("For a created Vector clock....")
  class StandardClockTests {

    @InjectMocks
    private final VectorClock vectorClock = Mockito.spy(createStandardVectorClock());

    @Mock
    private HashMap<String, Integer> counterMap;

    @BeforeEach
    void setupMocks() {
      MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("... when increasing an element by one, expect that the change is reflected in the internal map")
    void increaseByOne() {
      Mockito.when(counterMap.containsKey(Mockito.anyString())).thenReturn(true);
      Mockito.when(counterMap.get(Mockito.anyString())).thenReturn(0);

      vectorClock.incCounterForProcess("P2");

      Mockito.verify(counterMap).put("P2", 1);
    }

    @Test
    @DisplayName("... when trying to increase an unknown process, expect that an IllegalArgumentException is thrown")
    void expectToFailBecauseOfWrongProcessName() {
      assertThrows(IllegalArgumentException.class, () -> vectorClock.incCounterForProcess("P4"));
    }
  }

  @Nested
  @DisplayName("For multiple Vector clocks....")
  class ClockUpdateTests {

    private final VectorClock vectorClock1 = createStandardVectorClock();
    private final VectorClock vectorClock2 = createStandardVectorClock();

    @BeforeEach
    void inc2ndClock() {
      vectorClock2.incCounterForProcess("P2");
    }

    @Test
    @DisplayName("... when updating a vector clock with another, expect that the change is reflected in ")
    void updateReceiversVectorClock() {
      vectorClock1.updateClockWith(vectorClock2);
      assertEquals(vectorClock1, vectorClock2);
    }
  }
  

  @Nested
  @DisplayName("When duplicating vector clocks....")
  class ClockCreationTests {

    private final VectorClock vectorClock = createStandardVectorClock();

    @BeforeEach
    void increaseByOne() {
      vectorClock.incCounterForProcess("P2");
    }

    @Test
    @DisplayName("... by using the clone method, expect that the change is reflected in the new clock")
    void cloneAClock() {
      assertEquals(vectorClock.clone(), vectorClock);
    }
  }
}