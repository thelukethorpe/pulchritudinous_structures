package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
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
  public void hasSizeEqualToNumberOfItems() {
    linkedList.add("1");
    linkedList.add("2");
    linkedList.add("3");
    assertThat(linkedList.size(), is(3));

    linkedList.remove("3");
    linkedList.remove("2");
    assertThat(linkedList.size(), is(1));
  }

  @Test
  public void onlyContainsItemsThatHaveBeenAdded() {
    linkedList.add("Something");
    assertTrue(linkedList.contains("Something"));
    assertFalse(linkedList.contains("Something else"));
  }

  @Test
  public void throwsNullPointerExceptionWhenNullIsAdded() {
    try {
      linkedList.add(null);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemoved() {
    linkedList.add("Something");
    assertTrue(linkedList.remove("Something"));
    assertFalse(linkedList.contains("Something"));
    assertFalse(linkedList.remove("Something"));
  }

  @Test
  public void onlyOneItemIsRemovedAtATime() {
    linkedList.add("Something in particular");
    linkedList.add("Something in particular");

    assertTrue(linkedList.remove("Something in particular"));
    assertTrue(linkedList.contains("Something in particular"));

    assertTrue(linkedList.remove("Something in particular"));
    assertFalse(linkedList.contains("Something in particular"));
  }

  @Test
  public void doesNotContainDuplicatesAfterAllHaveBeenRemoved() {
    linkedList.add("Something in particular");
    linkedList.add("Something else");
    linkedList.add("Something in particular");
    linkedList.add("Another thing");
    linkedList.add("Something in particular");

    linkedList.removeAll("Something in particular");
    assertFalse(linkedList.contains("Something in particular"));
    assertTrue(linkedList.contains("Something else"));
    assertTrue(linkedList.contains("Another thing"));
  }

  @Test
  public void retainsOrderOfAddition() {
    linkedList.add("1");
    linkedList.add("2");
    linkedList.add("3");

    assertThat(linkedList.get(0), is("1"));
    assertThat(linkedList.get(1), is("2"));
    assertThat(linkedList.get(2), is("3"));
  }

  @Test
  public void returnsNullWhenBadlyIndexed() {
    assertThat(new LinkedList<>().get(1), is((Object) null));
  }

  @Test
  public void retainsOrderAfterInsertion() {
    linkedList.add("1");
    linkedList.add("3");

    assertThat(linkedList.get(0), is("1"));
    assertThat(linkedList.get(1), is("3"));

    linkedList.insertAt("2", 1);

    assertThat(linkedList.get(1), is("2"));
    assertThat(linkedList.get(2), is("3"));
  }

  @Test
  public void returnsNullWhenTooManyItemsArePolled() {
    assertThat(new LinkedList<>().pollMany(1), is((LinkedList) null));
  }

  @Test
  public void retainsOrderAndCorrectlyUpdatesContentsAfterPollingMany() {
    /* Adds items 1-8 to the list. */
    for (int i = 1; i <= 8; i++) {
      linkedList.add("" + i);
    }

    LinkedList<String> anotherLinkedList = linkedList.pollMany(3);

    for (int i = 1; i <= 3; i++) {
      assertTrue(anotherLinkedList.contains("" + i));
    }

    for (int i = 4; i <= 8; i++) {
      assertTrue(linkedList.contains("" + i));
    }
  }

  @Test
  public void returnsNullAfterEmptyPoll() {
    assertThat(new LinkedList<>().poll(), is((Object) null));
    assertThat(new LinkedList<>().poll(), is((Object) null));
  }

  @Test
  public void doesNotContainFirstItemAfterBeingPolled() {
    linkedList.add("Something in particular");
    linkedList.add("Something else");
    linkedList.add("Another thing");

    assertThat(linkedList.first(), is("Something in particular"));
    assertThat(linkedList.poll(), is("Something in particular"));
    assertThat(linkedList.poll(), is("Something else"));
    assertThat(linkedList.first(), is("Another thing"));

    assertFalse(linkedList.contains("Something in particular"));
    assertFalse(linkedList.contains("Something else"));
    assertTrue(linkedList.contains("Another thing"));
  }
}
