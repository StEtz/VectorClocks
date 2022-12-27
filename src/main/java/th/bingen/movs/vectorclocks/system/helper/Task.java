package th.bingen.movs.vectorclocks.system.helper;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.system.MySystem;

/**
 * This class represents a task which is executed in the{@link MySystem} class
 */
@RequiredArgsConstructor(staticName = "create")
@Getter
public class Task {

  /**
   * The id of the task
   */
  private final String id;

  /**
   * The name of the sender process
   */
  private final String source;

  /**
   * The list of recipients
   */
  private final List<String> destinations = new ArrayList<>();

  /**
   * Method for adding another receiver
   *
   * @param name The name of the receiving process
   */
  public void addReceiver(String name) {
    destinations.add(name);
  }
}
