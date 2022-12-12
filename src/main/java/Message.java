import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
@Getter
public class Message {

  private final String senderName;
  private final Clock clock;

}
