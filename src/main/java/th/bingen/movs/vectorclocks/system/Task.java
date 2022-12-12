package th.bingen.movs.vectorclocks.system;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")

public class Task {

  private final String id;

  @Getter
  private final String source;

  @Getter
  private final List<String> destinations = new ArrayList<>();

  public void addReceiver(String p) {
    destinations.add(p);
  }

  public boolean hasId(String id) {
    return id.equals(id);
  }
}
