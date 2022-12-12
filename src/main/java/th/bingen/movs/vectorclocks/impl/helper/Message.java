package th.bingen.movs.vectorclocks.impl.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

@RequiredArgsConstructor(staticName = "create")
@Getter
public class Message {

  private final String senderName;
  private final VectorClock clock;

}
