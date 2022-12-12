import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
@Getter
class Task {

  private final String id;
  private final String source;
  private final List<String> destinations = new ArrayList<>();

  public void addReceiver(String p) {
    destinations.add(p);
  }

  public boolean containsTaskFromSource(String n) {
    return source.equals(n);
  }

  public boolean hasId(String id) {
    return id.equals(id);
  }
}

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
