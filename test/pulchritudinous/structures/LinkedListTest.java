package pulchritudinous.structures;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LinkedListTest extends AbstractListTest {

  private final LinkedList<String> linkedList = new LinkedList<>();
  private final Random random = new Random();

  @Override
  protected AbstractList newEmptyList() {
    return new LinkedList();
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

    List<String> anotherLinkedList = linkedList.pollMany(3);

    for (int i = 1; i <= 3; i++) {
      assertTrue(anotherLinkedList.contains("" + i));
    }

    for (int i = 4; i <= 8; i++) {
      assertTrue(linkedList.contains("" + i));
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
  public void looksUpIndexBasedOnFirstOccurrence() {
    linkedList.add("1");
    linkedList.add("2");
    linkedList.add("3");
    linkedList.add("Something");
    linkedList.add("Something");

    assertThat(linkedList.indexOf("1"), is(0));
    assertThat(linkedList.indexOf("2"), is(1));
    assertThat(linkedList.indexOf("3"), is(2));
    assertThat(linkedList.indexOf("Something"), is(3));

  }

  @Test
  public void badIndexLookUpsReturnsNegativeOne() {
    assertThat(new LinkedList<>().indexOf("Something"), is(-1));
  }
}
