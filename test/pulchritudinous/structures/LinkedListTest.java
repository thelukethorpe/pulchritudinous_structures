package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class LinkedListTest {
  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new LinkedList().isEmpty());
  }
}
