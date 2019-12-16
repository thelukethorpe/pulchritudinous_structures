package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LinkedListTest {

  private final LinkedList<String> linkedList = new LinkedList<>();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new LinkedList().isEmpty());
  }

  @Test
  public void hasSizeOfZeroUponInitialization() {
    assertThat(new LinkedList().size(), is(0));
  }

  @Test
  public void isNotEmptyAfterItemHasBeenAdded() {
    linkedList.add("Something");
    assertFalse(linkedList.isEmpty());
  }

  @Test
  public void hasSizeEqualToNumberOfItemsAdded() {
    linkedList.add("1");
    linkedList.add("2");
    linkedList.add("3");
    assertThat(linkedList.size(), is(3));
  }
}
