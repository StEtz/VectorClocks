package th.bingen.movs.vectorclocks.impl.clock;

import static java.lang.Math.max;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Implementation of our VectorClock
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VectorClock {

  /**
   * This map holds the actual state for each known process
   */
  @Getter
  private HashMap<String, Integer> counterMap;

  /**
   * Factory method for creating an instance of our Vector clock
   *
   * @param processMap A Map which contains an inital 0 for each known process
   * @return a new {@link VectorClock} instance
   */
  public static VectorClock create(HashMap<String, Integer> processMap) {
    var c = new VectorClock();
    c.counterMap = processMap;
    return c;
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
            counterMap.put(key, max(counterMap.getOrDefault(key, 0), newClock.counterMap.get(key))
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
    return VectorClock.create(new HashMap<>(counterMap));
  }
}
