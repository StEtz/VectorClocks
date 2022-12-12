import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(staticName = "create")
@ToString
@Getter
public class Process {

  private final String name;


  private final Clock clock;

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
    clock.updateClock(sendersClock);
  }
}
