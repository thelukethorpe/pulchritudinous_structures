package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkedListTest {
  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new LinkedList().isEmpty());
  }

  @Test
  public void hasSizeOfZeroUponInitialization() {
    assertThat(new LinkedList().size(), is(0));
  }
}
