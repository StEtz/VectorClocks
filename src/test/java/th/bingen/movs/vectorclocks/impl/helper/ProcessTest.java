package th.bingen.movs.vectorclocks.impl.helper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

@DisplayName("Process tests")
class ProcessTest {

  VectorClock vectorClock = VectorClock.create(new HashMap<>() {{
                                                 put("P1", 0);
                                                 put("P2", 0);
                                                 put("P3", 0);
                                               }}
  );


  @Nested
  @DisplayName("When creating a process...")
  class CreationTests {

    String processName = "P1";
    Process process = Process.create(processName, vectorClock.clone());

    @Test
    @DisplayName(".... make sure that process contains the assigned vector clock")
    void checkUsedVectorClock() {
      assertThat(process.getClock().getCounterMap()).containsAllEntriesOf(vectorClock.getCounterMap());
    }

    @Test
    @DisplayName(".... make sure that process holds its name")
    void checkAssignedName() {
      assertThat(process.isCalled(processName)).isTrue();
    }

  }


  @Nested
  @DisplayName("When exchanging messages between two processes...")
  class CommunicationTests {

    Process process2 = null;
    Process process3 = null;

    @BeforeEach
    void createNewProcess() {
      process2 = Process.create("P2", vectorClock.clone());
      process3 = Process.create("P3", vectorClock.clone());
    }

    @Test
    @DisplayName(".... make sure that the sender increases its vector clock entry before sending")
    void checkUsedVectorClock() {
      Message message = process2.createSendMsg();
      assertThat(message.getClock().getCounterMap())
          .containsAllEntriesOf(vectorClock.incCounterForProcess("P2").getCounterMap());
    }

    @Test
    @DisplayName(".... make sure that the receiver increases its vector clock entry after receiving")
    void checkProcessAfterReceivedMessage() {
      VectorClock expectedVectorClock = VectorClock.create(new HashMap<>() {{
        put("P1", 0);
        put("P2", 1);
        put("P3", 1);
      }});

      Message message = process2.createSendMsg();
      process3.recieveMessage(message);
      assertThat(process3.getClock().getCounterMap())
          .containsAllEntriesOf(expectedVectorClock.getCounterMap());
    }
  }
}