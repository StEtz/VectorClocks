package th.bingen.movs.vectorclocks.system;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import th.bingen.movs.vectorclocks.impl.helper.Process;

@RequiredArgsConstructor(staticName = "withProcesses")
public class MySystem {

  private final List<Process> processesList;

  List<Task> runningTasks = new ArrayList<>();

  private String activeTaskId = "";

  public List<Process> processes() {
    return processesList;
  }

  private Process get(String name) {
    return processesList.stream().filter(p -> p.isCalled(name)).findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  public MySystem from(String name) {
    var p = get(name);
    if (!activeTaskId.isEmpty()) {
      throw new IllegalCallerException();
    }
    activeTaskId = UUID.randomUUID().toString();
    runningTasks.add(Task.create(activeTaskId, name));
    return this;
  }

  public void trigger(String name) {
    get(name).trigger();
  }

  public MySystem writeTo(String name) {
    var p = get(name);
    runningTasks
        .stream()
        .filter(task -> task.hasId(activeTaskId))
        .findFirst().orElseThrow(NoSuchElementException::new)
        .addReceiver(name);
    return this;
  }

  public void go() {
    runningTasks.forEach(task -> {
      executeTask(task);
    });
    runningTasks.clear();
    activeTaskId = "";
  }

  private void executeTask(Task task) {
    var sourceProcess = get(task.getSource());
    var msg = sourceProcess.createSendMsg();
    task.getDestinations().forEach(destProcessName -> {
      get(destProcessName).recieveMessage(msg);
    });

  }
}
