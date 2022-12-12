import static java.lang.Math.max;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Clock {

  @Getter
  private HashMap<String, Integer> counterMap;

  public static Clock create(HashMap<String, Integer> p1) {
    var c = new Clock();
    c.counterMap = p1;
    return c;
  }


  public Clock incCounterForProcess(String name) {
    counterMap.put(name, counterMap.get(name) + 1);
    return this;
  }

  public Clock updateClock(Clock newClock) {

    newClock.counterMap.keySet()
        .forEach(key ->
            counterMap.put(key, max(counterMap.getOrDefault(key, 0), newClock.counterMap.get(key))
            )
        );

    return this;
  }

  public Clock clone() {
    return Clock.create(new HashMap<>(counterMap));
  }
}
