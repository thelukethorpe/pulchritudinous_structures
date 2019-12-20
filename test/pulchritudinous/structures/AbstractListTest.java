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

  @Test
  public void retainsOrderOfAddition() {
    abstractList.add("1");
    abstractList.add("2");
    abstractList.add("3");

    assertThat(abstractList.get(0), is("1"));
    assertThat(abstractList.get(1), is("2"));
    assertThat(abstractList.get(2), is("3"));
  }

  @Test
  public void returnsNullWhenBadlyIndexed() {
    assertThat(newEmptyList().get(1), is((Object) null));
  }

  @Test
  public void retainsOrderAfterInsertion() {
    abstractList.add("1");
    abstractList.add("3");

    assertThat(abstractList.get(0), is("1"));
    assertThat(abstractList.get(1), is("3"));

    abstractList.insertAt("2", 1);

    assertThat(abstractList.get(1), is("2"));
    assertThat(abstractList.get(2), is("3"));
  }

  @Test
  public void retainsOrderAfterAddingFirstThenLast() {
    abstractList.addFirst("2");
    abstractList.addFirst("1");
    abstractList.addLast("3");

    assertThat(abstractList.get(0), is("1"));
    assertThat(abstractList.get(1), is("2"));
    assertThat(abstractList.get(2), is("3"));
  }

  @Test
  public void returnsNullAfterEmptyPoll() {
    assertThat(newEmptyList().poll(), is((Object) null));
    assertThat(newEmptyList().poll(), is((Object) null));
  }

  @Test
  public void doesNotContainFirstItemAfterBeingPolled() {
    abstractList.add("Something in particular");
    abstractList.add("Something else");
    abstractList.add("Another thing");

    assertThat(abstractList.first(), is("Something in particular"));
    assertThat(abstractList.poll(), is("Something in particular"));
    assertThat(abstractList.poll(), is("Something else"));
    assertThat(abstractList.first(), is("Another thing"));

    assertFalse(abstractList.contains("Something in particular"));
    assertFalse(abstractList.contains("Something else"));
    assertTrue(abstractList.contains("Another thing"));
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemovedAtTheGivenIndex() {
    abstractList.add("1");
    abstractList.add("Something");
    abstractList.add("2");
    abstractList.add("Something");
    abstractList.add("3");

    abstractList.removeAt(3);
    abstractList.removeAt(1);

    assertThat(abstractList.get(0), is("1"));
    assertThat(abstractList.get(1), is("2"));
    assertThat(abstractList.get(2), is("3"));
  }

  @Test
  public void allContentsAreCorrectlyMutatedAfterReplacement() {
    abstractList.add("1");
    abstractList.add("2");
    abstractList.add("3");

    abstractList.replaceAll(s -> "Item Number " + s);

    assertThat(abstractList.get(0), is("Item Number 1"));
    assertThat(abstractList.get(1), is("Item Number 2"));
    assertThat(abstractList.get(2), is("Item Number 3"));
  }
}
