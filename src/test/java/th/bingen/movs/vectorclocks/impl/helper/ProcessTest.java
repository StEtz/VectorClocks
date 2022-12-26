package th.bingen.movs.vectorclocks.impl.helper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Process tests")
@TestInstance(Lifecycle.PER_CLASS)
class ProcessTest {

  private final VectorClock mockedVectorClock = Mockito.mock(VectorClock.class);

  @Nested
  @DisplayName("When creating a process...")
  class CreationTests {

    private final String processName = "P1";
    private final Process process = Process.create(processName, mockedVectorClock);

    @Test
    @DisplayName(".... make sure that process contains the assigned vector clock")
    void checkUsedVectorClock() {
      assertThat(process.getClock()).isEqualTo(mockedVectorClock);
    }

    @Test
    @DisplayName(".... make sure that process holds its name")
    void checkAssignedName() {
      assertThat(process.isCalled(processName)).isTrue();
    }

  }


  @Nested
  @DisplayName("When exchanging messages between two processes...")
  @TestInstance(Lifecycle.PER_CLASS)
  class CommunicationTests {

    private Process process2 = null;
    private Process process3 = null;

    @BeforeAll
    void createNewProcess() {
      Mockito.when(mockedVectorClock.clone()).thenReturn(Mockito.mock(VectorClock.class));
      process2 = Process.create("P2", mockedVectorClock.clone());
      process3 = Process.create("P3", mockedVectorClock.clone());

      Message message = process2.createSendMsg();
      process3.recieveMessage(message);
    }

    @Test
    @DisplayName(".... make sure that the sender increases its vector clock entry before sending")
    void checkUsedVectorClock() {
      Mockito.verify(process2.getClock()).incCounterForProcess("P2");
    }

    @Test
    @DisplayName(".... make sure that the receiver increases its vector clock entry after receiving")
    void checkReceiverProcessAfterReceivedMessage() {
      Mockito.verify(process3.getClock()).incCounterForProcess("P3");
    }

    @Test
    @DisplayName(".... make sure that the receiver syncs its vector clock with the one from the sender")
    void checkReceiverProcessUpdatedItsClockWithTheSenders() {
      Mockito.verify(process3.getClock()).updateClockWith(process2.getClock());
    }
  }
}