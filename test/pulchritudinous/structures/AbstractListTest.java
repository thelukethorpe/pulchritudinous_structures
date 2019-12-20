package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
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

  @Test
  public void onlyContainsItemsThatHaveBeenAdded() {
    abstractList.add("Something");
    assertTrue(abstractList.contains("Something"));
    assertFalse(abstractList.contains("Something else"));
  }

  @Test
  public void throwsNullPointerExceptionWhenNullIsAdded() {
    try {
      abstractList.add(null);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemoved() {
    abstractList.add("Something");
    assertTrue(abstractList.remove("Something"));
    assertFalse(abstractList.contains("Something"));
    assertFalse(abstractList.remove("Something"));
  }

  @Test
  public void onlyOneItemIsRemovedAtATime() {
    abstractList.add("Something in particular");
    abstractList.add("Something in particular");

    assertTrue(abstractList.remove("Something in particular"));
    assertTrue(abstractList.contains("Something in particular"));

    assertTrue(abstractList.remove("Something in particular"));
    assertFalse(abstractList.contains("Something in particular"));
  }

  @Test
  public void doesNotContainDuplicatesAfterAllHaveBeenRemoved() {
    abstractList.add("Something in particular");
    abstractList.add("Something else");
    abstractList.add("Something in particular");
    abstractList.add("Another thing");
    abstractList.add("Something in particular");

    abstractList.removeAll("Something in particular");
    assertFalse(abstractList.contains("Something in particular"));
    assertTrue(abstractList.contains("Something else"));
    assertTrue(abstractList.contains("Another thing"));
  }
}
