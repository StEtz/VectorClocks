package th.bingen.movs.vectorclocks.impl.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.clock.VectorClock;

/**
 * The Process which sends/receives messages
 * <p>
 * This is basically the owner of the vector clock
 */
@RequiredArgsConstructor(staticName = "create")
public class Process {

  /**
   * The name of the process
   */
  private final String name;

  /**
   * The actual {@link VectorClock} instance which is owned by the process
   */
  @Getter
  private final VectorClock clock;

  /**
   * Syntactic sugar for better readability
   * <p>
   * process.isCalled("test") is nicer to read than process.getName().equals("test")
   *
   * @param name The test name
   * @return returns if the name is correct or not
   */
  public boolean isCalled(String name) {
    return this.name.equals(name);
  }

  /**
   * Method for creating a message which can be "send"
   *
   * @return the {@Link Message} instance which shall be send
   */
  public Message createSendMsg() {
    trigger();
    return Message.create(name, clock);
  }

  /**
   * This is a method which just updates the vector clock by one. It can be seen as any other operation which causes an update in the clock
   * It is only used in the SystemE2E test
   */
  public void trigger() {
    clock.incCounterForProcess(name);
  }

  /**
   * Method for "receiving" a message
   *
   * @param msg The new message
   */
  public void recieveMessage(Message msg) {
    var sendersClock = msg.getClock();

    trigger();
    clock.updateClockWith(sendersClock);
  }

  public void display() {
    System.out.print(name + ": ");
    clock.display();
  }
}
