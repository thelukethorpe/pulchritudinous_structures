package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public abstract class AbstractListTest {
  private final AbstractList<String> abstractList;

  public AbstractListTest() {
    this.abstractList = newEmptyList();
  }

  protected abstract AbstractList newEmptyList();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(newEmptyList().isEmpty());
  }

  @Test
  public void hasSizeOfZeroUponInitialization() {
    assertThat(newEmptyList().size(), is(0));
  }

  @Test
  public void isNotEmptyAfterItemHasBeenAdded() {
    abstractList.add("Something");
    assertFalse(abstractList.isEmpty());
  }

  @Test
  public void hasSizeEqualToNumberOfItems() {
    abstractList.add("1");
    abstractList.add("2");
    abstractList.add("3");
    assertThat(abstractList.size(), is(3));

    abstractList.remove("3");
    abstractList.remove("2");
    assertThat(abstractList.size(), is(1));
  }
}
