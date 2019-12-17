package pulchritudinous.structures;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LinkedListTest {

  private final LinkedList<String> linkedList = new LinkedList<>();
  private final Random random = new Random();

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

  @Test
  public void isIterable() {
    /* Adds items 1-8 to the list. */
    for (int i = 1; i <= 8; i++) {
      linkedList.add("" + i);
    }

    int expectedValue = 1;
    for (String actualValue : linkedList) {
      assertThat(actualValue, is("" + expectedValue));
      expectedValue++;
    }
  }

  @Test
  public void sortsItemsIntoAscendingOrder() {
    /* Adds 1000 random items to the list. */
    for (int i = 0; i < 1000; i++) {
      linkedList.add("" + random.nextInt());
    }

    linkedList.sort(String::compareTo);

    String prev = linkedList.poll();
    String curr = linkedList.poll();

    while (curr != null) {
      assertTrue(prev.compareTo(curr) <= 0);
      prev = curr;
      curr = linkedList.poll();
    }
  }

  @Test
  public void sortingItemsRetainsContents() {
    /* Adds 1000 random items to the list. */
    String[] contents = new String[1000];
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      linkedList.add(item);
      contents[i] = item;
    }

    linkedList.sort(String::compareTo);

    for (int i = 0; i < 1000; i++) {
      assertTrue(linkedList.remove(contents[i]));
    }

    assertTrue(linkedList.isEmpty());
  }

  @Test
  public void isEmptyAfterClear() {
    linkedList.add("Something in particular");
    linkedList.add("Something else");
    linkedList.add("Another thing");

    linkedList.clear();

    assertTrue(linkedList.isEmpty());
    assertFalse(linkedList.contains("Something in particular"));
    assertFalse(linkedList.contains("Something else"));
    assertFalse(linkedList.contains("Another thing"));
  }

  @Test
  public void containsAllItemsThatHaveBeenAdded() {
    LinkedList<String> someItems = new LinkedList<>();
    someItems.add("Something in particular");
    someItems.add("Something else");
    someItems.add("Another thing");

    linkedList.add("I was here before anything else");
    linkedList.add("Me too!");
    linkedList.addAll(someItems);

    assertTrue(linkedList.contains("I was here before anything else"));
    assertTrue(linkedList.contains("Me too!"));

    assertTrue(someItems.contains("Something in particular"));
    assertTrue(someItems.contains("Something else"));
    assertTrue(someItems.contains("Another thing"));

    assertTrue(linkedList.contains("Something in particular"));
    assertTrue(linkedList.contains("Something else"));
    assertTrue(linkedList.contains("Another thing"));
  }

  @Test
  public void isNotEqualToListOfDifferentType() {
    linkedList.add("Something");
    LinkedList<Integer> anotherLinkedList = new LinkedList<>();
    anotherLinkedList.add(42);

    assertThat(linkedList, is(not(anotherLinkedList)));
  }

  @Test
  public void isEqualToListWithSameOrderAndContents() {
    LinkedList<String> anotherLinkedList = new LinkedList<>();
    /* Adds 1000 random items to both lists. */
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      linkedList.add(item);
      anotherLinkedList.add(item);
    }

    assertThat(linkedList, is(anotherLinkedList));
    assertThat(linkedList.hashCode(), is(anotherLinkedList.hashCode()));
  }

  @Test
  public void conversionToArrayRetainsOrderAndContents() {
    /* Adds 1000 random items to the list. */
    String[] contents = new String[1000];
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      linkedList.add(item);
      contents[i] = item;
    }

    assertTrue(Arrays.equals(contents, linkedList.toArray()));
  }

  @Test
  public void isDeepCloneable() {
    linkedList.add("Something in particular");
    linkedList.add("Something else");
    linkedList.add("Another thing");

    LinkedList<String> linkedListClone = linkedList.clone();
    linkedList.clear();

    assertTrue(linkedListClone.contains("Something in particular"));
    assertTrue(linkedListClone.contains("Something else"));
    assertTrue(linkedListClone.contains("Another thing"));
  }

  @Test
  public void retainsOrderAfterAddingFirst() {
    linkedList.addFirst("2");
    linkedList.addFirst("1");
    linkedList.add("3");

    assertThat(linkedList.get(0), is("1"));
    assertThat(linkedList.get(1), is("2"));
    assertThat(linkedList.get(2), is("3"));
  }
}
