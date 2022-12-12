package th.bingen.movs.vectorclocks.impl.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

@RequiredArgsConstructor(staticName = "create")

public class Process {

  private final String name;

  @Getter
  private final VectorClock clock;

  public boolean isCalled(String name) {
    return this.name.equals(name);
  }

  public Message createSendMsg() {
    trigger();
    return Message.create(name, clock);
  }

  public void trigger() {
    clock.incCounterForProcess(name);
  }


  public void recieveMessage(Message msg) {
    var sendersClock = msg.getClock();

    trigger();
    clock.updateClockWith(sendersClock);
  }
}
