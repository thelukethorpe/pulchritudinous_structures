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

public abstract class ListTest {
  private final List<String> list;
  private final Random random = new Random();

  public ListTest() {
    this.list = newEmptyList();
  }

  protected abstract List newEmptyList();

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
    list.add("Something");
    assertFalse(list.isEmpty());
  }

  @Test
  public void hasSizeEqualToNumberOfItems() {
    list.add("1");
    list.add("2");
    list.add("3");
    assertThat(list.size(), is(3));

    list.remove("3");
    list.remove("2");
    assertThat(list.size(), is(1));
  }

  @Test
  public void onlyContainsItemsThatHaveBeenAdded() {
    list.add("Something");
    assertTrue(list.contains("Something"));
    assertFalse(list.contains("Something else"));
  }

  @Test
  public void throwsNullPointerExceptionWhenNullIsAdded() {
    try {
      list.add(null);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemoved() {
    list.add("Something");
    assertTrue(list.remove("Something"));
    assertFalse(list.contains("Something"));
    assertFalse(list.remove("Something"));
  }

  @Test
  public void onlyOneItemIsRemovedAtATime() {
    list.add("Something in particular");
    list.add("Something in particular");

    assertTrue(list.remove("Something in particular"));
    assertTrue(list.contains("Something in particular"));

    assertTrue(list.remove("Something in particular"));
    assertFalse(list.contains("Something in particular"));
  }

  @Test
  public void doesNotContainDuplicatesAfterAllHaveBeenRemoved() {
    list.add("Something in particular");
    list.add("Something else");
    list.add("Something in particular");
    list.add("Another thing");
    list.add("Something in particular");

    list.removeAll("Something in particular");
    assertFalse(list.contains("Something in particular"));
    assertTrue(list.contains("Something else"));
    assertTrue(list.contains("Another thing"));
  }

  @Test
  public void retainsOrderOfAddition() {
    list.add("1");
    list.add("2");
    list.add("3");

    assertThat(list.get(0), is("1"));
    assertThat(list.get(1), is("2"));
    assertThat(list.get(2), is("3"));
  }

  @Test
  public void returnsNullWhenBadlyIndexed() {
    assertThat(newEmptyList().get(1), is((Object) null));
  }

  @Test
  public void retainsOrderAfterInsertion() {
    list.add("1");
    list.add("3");

    assertThat(list.get(0), is("1"));
    assertThat(list.get(1), is("3"));

    list.insertAt("2", 1);

    assertThat(list.get(1), is("2"));
    assertThat(list.get(2), is("3"));
  }

  @Test
  public void retainsOrderAfterAddingFirstThenLast() {
    list.addFirst("2");
    list.addFirst("1");
    list.addLast("3");

    assertThat(list.get(0), is("1"));
    assertThat(list.get(1), is("2"));
    assertThat(list.get(2), is("3"));
  }

  @Test
  public void returnsNullAfterEmptyPoll() {
    assertThat(newEmptyList().poll(), is((Object) null));
    assertThat(newEmptyList().poll(), is((Object) null));
  }

  @Test
  public void doesNotContainFirstItemAfterBeingPolled() {
    list.add("Something in particular");
    list.add("Something else");
    list.add("Another thing");

    assertThat(list.first(), is("Something in particular"));
    assertThat(list.poll(), is("Something in particular"));
    assertThat(list.poll(), is("Something else"));
    assertThat(list.first(), is("Another thing"));

    assertFalse(list.contains("Something in particular"));
    assertFalse(list.contains("Something else"));
    assertTrue(list.contains("Another thing"));
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemovedAtTheGivenIndex() {
    list.add("1");
    list.add("Something");
    list.add("2");
    list.add("Something");
    list.add("3");

    list.removeAt(3);
    list.removeAt(1);

    assertThat(list.get(0), is("1"));
    assertThat(list.get(1), is("2"));
    assertThat(list.get(2), is("3"));
  }

  @Test
  public void allContentsAreCorrectlyMutatedAfterReplacement() {
    list.add("1");
    list.add("2");
    list.add("3");

    list.replaceAll(s -> "Item Number " + s);

    assertThat(list.get(0), is("Item Number 1"));
    assertThat(list.get(1), is("Item Number 2"));
    assertThat(list.get(2), is("Item Number 3"));
  }

  @Test
  public void itemsAreCorrectlyReplacedWhenSettingAtTheGivenIndex() {
    list.add("Something in particular");
    list.add("Something else");
    list.add("Another thing");

    assertThat(list.set("1", 0), is("Something in particular"));
    assertThat(list.set("2", 1), is("Something else"));
    assertThat(list.set("3", 2), is("Another thing"));

    assertThat(list.get(0), is("1"));
    assertThat(list.get(1), is("2"));
    assertThat(list.get(2), is("3"));
  }

  @Test
  public void isIterable() {
    /* Adds items 1-8 to the list. */
    for (int i = 1; i <= 8; i++) {
      list.add("" + i);
    }

    int expectedValue = 1;
    for (String actualValue : list) {
      assertThat(actualValue, is("" + expectedValue));
      expectedValue++;
    }
  }

  @Test
  public void looksUpIndexBasedOnFirstOccurrence() {
    list.add("1");
    list.add("2");
    list.add("3");
    list.add("Something");
    list.add("Something");

    assertThat(list.indexOf("1"), is(0));
    assertThat(list.indexOf("2"), is(1));
    assertThat(list.indexOf("3"), is(2));
    assertThat(list.indexOf("Something"), is(3));

  }

  @Test
  public void badIndexLookUpsReturnsNegativeOne() {
    assertThat(newEmptyList().indexOf("Something"), is(-1));
  }

  @Test
  public void isEmptyAfterClear() {
    list.add("Something in particular");
    list.add("Something else");
    list.add("Another thing");

    list.clear();

    assertTrue(list.isEmpty());
    assertFalse(list.contains("Something in particular"));
    assertFalse(list.contains("Something else"));
    assertFalse(list.contains("Another thing"));
  }

  @Test
  public void returnsNullWhenTooManyItemsArePolled() {
    assertThat(newEmptyList().pollMany(1), is((List) null));
  }

  @Test
  public void retainsOrderAndCorrectlyUpdatesContentsAfterPollingMany() {
    /* Adds items 1-8 to the list. */
    for (int i = 1; i <= 8; i++) {
      list.add("" + i);
    }

    List<String> anotherList = list.pollMany(3);

    for (int i = 1; i <= 3; i++) {
      assertTrue(anotherList.contains("" + i));
    }

    for (int i = 4; i <= 8; i++) {
      assertTrue(list.contains("" + i));
    }
  }

  @Test
  public void sortsItemsIntoAscendingOrder() {
    /* Adds 1000 random items to the list. */
    for (int i = 0; i < 1000; i++) {
      list.add("" + random.nextInt());
    }

    list.sort(String::compareTo);

    String prev = list.poll();
    String curr = list.poll();

    while (curr != null) {
      assertTrue(prev.compareTo(curr) <= 0);
      prev = curr;
      curr = list.poll();
    }
  }

  @Test
  public void sortingItemsRetainsContents() {
    /* Adds 1000 random items to the list. */
    String[] contents = new String[1000];
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      list.add(item);
      contents[i] = item;
    }

    list.sort(String::compareTo);

    for (int i = 0; i < 1000; i++) {
      assertTrue(list.remove(contents[i]));
    }

    assertTrue(list.isEmpty());
  }

  @Test
  public void containsAllItemsThatHaveBeenAdded() {
    List<String> someItems = newEmptyList();
    someItems.add("Something in particular");
    someItems.add("Something else");
    someItems.add("Another thing");

    list.add("I was here before anything else");
    list.add("Me too!");
    list.addAll(someItems);

    assertTrue(list.contains("I was here before anything else"));
    assertTrue(list.contains("Me too!"));

    assertTrue(someItems.contains("Something in particular"));
    assertTrue(someItems.contains("Something else"));
    assertTrue(someItems.contains("Another thing"));

    assertTrue(list.contains("Something in particular"));
    assertTrue(list.contains("Something else"));
    assertTrue(list.contains("Another thing"));
  }

  @Test
  public void isNotEqualToListOfDifferentType() {
    list.add("Something");
    List<Integer> anotherList = newEmptyList();
    anotherList.add(42);

    assertThat(list, is(not(anotherList)));
  }

  @Test
  public void isEqualToListWithSameOrderAndContents() {
    List<String> anotherList = newEmptyList();
    /* Adds 1000 random items to both lists. */
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      list.add(item);
      anotherList.add(item);
    }

    assertThat(list, is(anotherList));
    assertThat(list.hashCode(), is(anotherList.hashCode()));
  }

  @Test
  public void conversionToArrayRetainsOrderAndContents() {
    /* Adds 1000 random items to the list. */
    String[] contents = new String[1000];
    for (int i = 0; i < 1000; i++) {
      String item = "" + random.nextInt();
      list.add(item);
      contents[i] = item;
    }

    assertTrue(Arrays.equals(contents, list.toArray()));
  }

  @Test
  public void isDeepCloneable() {
    list.add("Something in particular");
    list.add("Something else");
    list.add("Another thing");

    List<String> listClone = list.clone();
    list.clear();

    assertTrue(listClone.contains("Something in particular"));
    assertTrue(listClone.contains("Something else"));
    assertTrue(listClone.contains("Another thing"));
  }
}
