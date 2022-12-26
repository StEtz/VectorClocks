package th.bingen.movs.vectorclocks.impl.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Message tests")
@TestInstance(Lifecycle.PER_CLASS)
class MessageTest {

  private final String senderName = "TEST_SENDER";
  private final VectorClock mockedVectorClock = Mockito.mock(VectorClock.class);
  private final Message message = Message.create(senderName, mockedVectorClock);


  @Test
  @DisplayName("Check that a message has got a sender name")
  void checkSenderName() {
    assertThat(message.getSenderName()).isEqualTo(senderName);
  }

  @Test
  @DisplayName("Check that a message has got a vector clock")
  void checksVectorClock() {
    assertThat(message.getClock()).isEqualTo(mockedVectorClock);
  }
}