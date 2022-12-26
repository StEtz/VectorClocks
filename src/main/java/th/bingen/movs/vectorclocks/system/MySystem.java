package th.bingen.movs.vectorclocks.system;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.helper.Process;

/**
 * This class represents the system in which we are running our processes with our clocks
 */
@RequiredArgsConstructor(staticName = "withProcesses")
public class MySystem {

  /**
   * The running processes in our system
   */
  private final List<Process> processesList;

  /**
   * This is used for batch-processing some tasks (e. g. sending messages to multiple receivers...)
   */
  private final List<Task> runningTasks = new ArrayList<>();

  /**
   * This holds the actual task which is in progress
   * <p>
   * So we can do: task.from("test0").writeTo("test1").writeTo("test2").writeTo("test3").go()
   */
  private String activeTaskId = "";

  /**
   * Helper method to safely get a process with a given name from the own map
   *
   * @param name The requested process
   * @return The {@link Process} instance
   */
  private Process get(String name) {
    return processesList.stream().filter(p -> p.isCalled(name)).findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Method for executing a task in batch-mode
   *
   * @param task The {@link Task} instance which shall be executed
   */
  private void executeTask(Task task) {
    var sourceProcess = get(task.getSource());
    var msg = sourceProcess.createSendMsg();
    task.getDestinations().forEach(destProcessName -> {
      get(destProcessName).recieveMessage(msg);
    });
  }

  /**
   * Method to create a new task for sending a message
   *
   * @param name The name of the sending process
   * @return the instance of {@link MySystem} to do a chained call
   */
  public MySystem from(String name) {
    var p = get(name);
    if (!activeTaskId.isEmpty()) {
      throw new IllegalCallerException();
    }
    activeTaskId = UUID.randomUUID().toString();
    runningTasks.add(Task.create(activeTaskId, name));
    return this;
  }

  /**
   * Method for executing triggering an action on a known process Basically only updates its vector clock by one...
   *
   * @param name The target process
   */
  public void trigger(String name) {
    get(name).trigger();
  }

  /**
   * Method for adding a receiver to the actual task
   *
   * @param name The name of the target process
   * @return the instance of {@link MySystem} to do a chained call
   */
  public MySystem writeTo(String name) {
    get(name);
    runningTasks
        .stream()
        .filter(task -> task.getId().equals(activeTaskId))
        .findFirst().orElseThrow(NoSuchElementException::new)
        .addReceiver(name);
    return this;
  }

  /**
   * Method for executing the task
   */
  public void go() {
    runningTasks.forEach(task -> executeTask(task));
    runningTasks.clear();
    activeTaskId = "";
  }

  /**
   * Helper method for displaying the processes and their internal state
   */
  public void displayProcesses() {
    List<List<Integer>> matrix = new ArrayList<>(List.of());
    processesList.forEach(process -> {
      matrix.add(process.getClock().getCounterMap().values().stream().collect(Collectors.toList()));
    });

    List<String> l = new ArrayList();
    l.add("");
    for (int x = 0; x < matrix.size(); x++) {
      l.add("P" + x);
    }
    System.out.println(l.stream().collect(Collectors.joining("\t")));

    for (int x = 0; x < matrix.size(); x++) {
      for (int y = 0; y < matrix.get(x).size(); y++) {
        System.out.print("\t" + matrix.get(y).get(x) + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }
}
