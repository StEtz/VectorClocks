package th.bingen.movs.vectorclocks.impl.clock;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Implementation of our VectorClock
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class VectorClock {

  /**
   * This map holds the actual state for each known process
   */
  private HashMap<String, Integer> counterMap = new HashMap<>();

  /**
   * Factory method for creating an instance of our Vector clock
   *
   * @param processList A List which contains the names of the known processes
   * @return a new {@link VectorClock} instance
   */
  public static VectorClock create(List<String> processList) {
    var c = new VectorClock();
    HashMap<String, Integer> hm = new HashMap<>();
    processList.forEach(processName -> hm.put(processName, 0));
    c.counterMap = hm;
    return c;
  }

  /**
   * Display method for showing the content of the vector clock
   */
  public void display() {

    System.out.println(
        counterMap.keySet()
            .stream()
            .map(processName -> String.valueOf(counterMap.get(processName)))
            .collect(Collectors.joining(" "))
    );
  }

  /**
   * Method for incrementing the counter of a known process
   *
   * @param name The name of the process which state shall be incremented
   * @return the current {@link VectorClock} instance
   */
  public VectorClock incCounterForProcess(String name) {
    if (!counterMap.containsKey(name)) {
      throw new IllegalArgumentException();
    }

    counterMap.put(name, counterMap.get(name) + 1);
    return this;
  }

  /**
   * Method for synchronising the state of the own clock with another
   *
   * @param newClock The target clock
   * @return the current {@link VectorClock} instance
   */
  public VectorClock updateClockWith(VectorClock newClock) {

    newClock.counterMap.keySet()
        .forEach(key ->
            counterMap.put(key, Integer.min(counterMap.getOrDefault(key, 0), newClock.counterMap.get(key))
            )
        );

    return this;
  }

  /**
   * Method for creating a copy of the current {@link VectorClock} instance
   *
   * @return the new {@link VectorClock} instance
   */
  public VectorClock clone() {
    VectorClock vc = new VectorClock();
    vc.counterMap.putAll(counterMap);
    return vc;
  }
}
