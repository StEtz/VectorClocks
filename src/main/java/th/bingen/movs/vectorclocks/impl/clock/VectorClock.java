package th.bingen.movs.vectorclocks.impl.clock;

import static java.lang.Math.max;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VectorClock {

  @Getter
  private HashMap<String, Integer> counterMap;

  public static VectorClock create(HashMap<String, Integer> p1) {
    var c = new VectorClock();
    c.counterMap = p1;
    return c;
  }


  public VectorClock incCounterForProcess(String name) {
    if (!counterMap.containsKey(name)) {
      throw new IllegalArgumentException();
    }

    counterMap.put(name, counterMap.get(name) + 1);
    return this;
  }

  public VectorClock updateClockWith(VectorClock newClock) {

    newClock.counterMap.keySet()
        .forEach(key ->
            counterMap.put(key, max(counterMap.getOrDefault(key, 0), newClock.counterMap.get(key))
            )
        );

    return this;
  }

  public VectorClock clone() {
    return VectorClock.create(new HashMap<>(counterMap));
  }
}
