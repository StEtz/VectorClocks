package th.bingen.movs.vectorclocks.system;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;
import th.bingen.movs.vectorclocks.impl.helper.Process;

@DisplayName("Doing an E2E test.... ")
class SystemE2ETest {

  private final VectorClock vectorClock = VectorClock.create(List.of("P1", "P2", "P3"));

  @Nested
  @DisplayName(".... with one process which fails because ....")
  class OneFailingProcess {

    private final Process p1 = Process.create("P1", vectorClock.clone());

    @Test
    @DisplayName(".... if calls the send method more than ones before sending, expect an IllegalStateException is raised")
    void createMultipleWriteAttempts() {
      MySystem system = MySystem.withProcesses(List.of(p1));
      assertThrows(IllegalCallerException.class, () -> system.from("P1").from("P1"));
    }

  }

  @Nested
  @DisplayName(".... with three processes....")
  class ThreeProcesses {

    private final Process p1 = Process.create("P1", vectorClock.clone());
    private final Process p2 = Process.create("P2", vectorClock.clone());
    private final Process p3 = Process.create("P3", vectorClock.clone());

    @Test
    @DisplayName(".... when exchanging messages between the processes, expect the vector clocks reflect the state accordingly")
    void validExchangeTest() {

      MySystem system = MySystem.withProcesses(List.of(p1, p2, p3));

      system.from("P1").writeTo("P3").go();
      system.from("P1").writeTo("P3").go();

      system.trigger("P2");

      system.from("P2").writeTo("P3").go();
      system.from("P2").writeTo("P3").go();

      system.from("P2").writeTo("P1").writeTo("P3").go();

      system.displayProcesses();

      assertThat(p1.getClock().getCounterMap()).containsAllEntriesOf(new HashMap<>() {{
        put("P1", 3);
        put("P2", 4);
        put("P3", 0);
      }});

      assertThat(p2.getClock().getCounterMap()).containsAllEntriesOf(new HashMap<>() {{
        put("P1", 0);
        put("P2", 4);
        put("P3", 0);
      }});

      assertThat(p3.getClock().getCounterMap()).containsAllEntriesOf(new HashMap<>() {{
        put("P1", 2);
        put("P2", 4);
        put("P3", 5);
      }});

    }
  }
}