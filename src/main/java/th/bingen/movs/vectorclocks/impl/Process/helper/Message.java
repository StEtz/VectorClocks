package th.bingen.movs.vectorclocks.impl.Process.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;


/**
 * The representation of the messages which shall be send around
 */
@RequiredArgsConstructor(staticName = "create")
@Getter
public class Message {

  /**
   * The name of the sending process This is important to know in the {@Task} when processing the message
   */
  private final String senderName;

  /**
   * The {@link VectorClock} instance of the sender
   */
  private final VectorClock clock;

}
