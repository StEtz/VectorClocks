package th.bingen.movs.vectorclocks.system;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Task tests")
class TaskTest {

  private final String source = "TEST_SOURCE";
  private final String id = "TEST_ID";
  private final Task t = Task.create(id, source);

  @Test
  @DisplayName("Check that a Task has got a source")
  void checkTheTaskHoldsTheSource() {
    assertThat(t.getSource()).isEqualTo(source);
  }

  @Test
  @DisplayName("Check that a Task has got an id")
  void checkTheTaskHasAnId() {
    assertThat(t.getId()).isEqualTo(id);
  }

  @Test
  @DisplayName("Ensure that a Task can add a Receiver in a list")
  void checkTheTaskCanAddDestinations() {
    t.addReceiver("d");
    assertThat(t.getDestinations()).contains("d");
  }
}