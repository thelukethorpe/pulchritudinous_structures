package pulchritudinous.structures;

import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class ArrayListTest {

  private final ArrayList<String> arrayList = new ArrayList<>();
  private final Random random = new Random();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new ArrayList().isEmpty());
  }
}
