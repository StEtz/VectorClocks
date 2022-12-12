package th.bingen.movs.vectorclocks.impl.clock;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Vector clock tests")
class ClockTest {

  @Nested
  @DisplayName("For a created Vector clock....")
  class StandardClockTests {

    private final VectorClock vectorClock = VectorClock.create(new HashMap<>() {{
      put("P1", 0);
      put("P2", 0);
      put("P3", 0);
    }});

    @Test
    @DisplayName("... when increasing an element by one, expect that the change is reflected in the internal map")
    void increaseByOne() {
      assertThat(vectorClock.incCounterForProcess("P2").getCounterMap()).contains(new SimpleEntry<>("P2", 1));
    }

    @Test
    @DisplayName("... when trying to increase an unknown process, expect that an IllegalArgumentException is thrown")
    void expectToFailBecauseOfWrongProcessName() {
      assertThrows(IllegalArgumentException.class, () -> {
        vectorClock.incCounterForProcess("P4");
      });
    }
  }

  @Nested
  @DisplayName("For multiple Vector clocks....")
  class ClockUpdateTests {

    private final VectorClock vectorClock1 = VectorClock.create(new HashMap<>() {{
      put("P1", 0);
      put("P2", 0);
      put("P3", 0);
    }});

    private final VectorClock vectorClock2 = VectorClock.create(new HashMap<>() {{
      put("P1", 0);
      put("P2", 2);
      put("P3", 0);
    }});

    @Test
    @DisplayName("... when updating a vector clock with another, expect that the change is reflected in ")
    void increaseByOne() {
      vectorClock1.updateClockWith(vectorClock2);
      assertThat(vectorClock1.getCounterMap()).containsAllEntriesOf(vectorClock2.getCounterMap());
    }
  }


  @Nested
  @DisplayName("When duplicating vector clocks....")
  class ClockCreationTests {

    private final VectorClock vectorClock1 = VectorClock.create(new HashMap<>() {{
      put("P1", 0);
      put("P2", 0);
      put("P3", 0);
    }});

    @BeforeEach
    void increaseByOne() {
      vectorClock1.incCounterForProcess("P2");
    }

    @Test
    @DisplayName("... by using the clone method, expect that the change is reflected in the new clock")
    void clockAClock() {
      assertThat(vectorClock1.clone().getCounterMap()).containsAllEntriesOf(vectorClock1.getCounterMap());
    }
  }
}