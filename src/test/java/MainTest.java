import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MainTest {

  void display(MySystem system) {
    System.out.println("");
    List<List<Integer>> matrix = new ArrayList<>(List.of());
    system.processes().forEach(process -> {
      matrix.add(process.getClock().getCounterMap().values().stream().collect(Collectors.toList()));
      //System.out.println(process.getClock());
    });

    System.out.println("\tP1\tP2\tP3");

    for (int x = 0; x < matrix.size(); x++) {
      for (int y = 0; y < matrix.get(x).size(); y++) {
        System.out.print("\t" + matrix.get(y).get(x) + "\t");
      }
      System.out.println();
    }
  }


  @Nested
  @DisplayName("With four processes....")
  class FourProcesses {

    @Test
    void doThis() {

      Clock vectorClock = Clock.create(new HashMap<>() {{
                                         put("P1", 0);
                                         put("P2", 0);
                                         put("P3", 0);
                                         put("P4", 0);
                                       }}
      );

      MySystem system = MySystem.withProcesses(
          List.of(
              Process.create("P1", vectorClock.clone()),
              Process.create("P2", vectorClock.clone()),
              Process.create("P3", vectorClock.clone()),
              Process.create("P4", vectorClock.clone())

          )
      );
      system.from("P1").writeTo("P2").writeTo("P3").writeTo("P4").go();

      display(system);

      system.from("P1").writeTo("P2").writeTo("P3").writeTo("P4").go();
      display(system);
      system.from("P2").writeTo("P3").writeTo("P4").go();

      display(system);

    }
  }

  @Nested
  @DisplayName("With three processes....")
  class ThreeProcesses {

    @Test
    void doThis() {

      Clock vectorClock = Clock.create(new HashMap<>() {{
                                         put("P1", 0);
                                         put("P2", 0);
                                         put("P3", 0);
                                       }}
      );

      MySystem system = MySystem.withProcesses(
          List.of(
              Process.create("P1", vectorClock.clone()),
              Process.create("P2", vectorClock.clone()),
              Process.create("P3", vectorClock.clone())
          )
      );
      system.from("P1").writeTo("P3").go();

      display(system);

      system.from("P1").writeTo("P3").go();
      display(system);

      system.trigger("P2");
      system.from("P2").writeTo("P3").go();

      display(system);
      system.from("P2").writeTo("P3").go();
      display(system);

      system.from("P2").writeTo("P1").writeTo("P3").go();
      display(system);
    }
  }
}