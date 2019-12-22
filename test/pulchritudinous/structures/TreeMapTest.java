package pulchritudinous.structures;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TreeMapTest {
  private final TreeMap<String, Integer> treeMap = new TreeMap<>();
  private final Random random = new Random();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new TreeMap<>().isEmpty());
  }

  @Test
  public void hasSizeOfZeroUponInitialization() {
    assertThat(new TreeMap<>().size(), is(0));
  }

  @Test
  public void isNotEmptyAfterMappingHasBeenAdded() {
    treeMap.put("Something", 1);
    assertFalse(treeMap.isEmpty());
  }

  @Test
  public void remappingReturnsOldValue() {
    int n = 5;
    Integer[] squares = new Integer[]{null, 1, 4, 9, 16, 25};
    Integer[] fibs = new Integer[]{null, 1, 1, 2, 3, 5};
    Integer[] primes = new Integer[]{null, 2, 3, 5, 7, 11};

    for (int i = 0; i < n; i++) {
      assertThat(treeMap.put("Square", squares[i + 1]), is(squares[i]));
      assertThat(treeMap.put("Fib", fibs[i + 1]), is(fibs[i]));
      assertThat(treeMap.put("Prime", primes[i + 1]), is(primes[i]));
    }
  }

  @Test
  public void hasSizeEqualToNumberOfMappings() {
    treeMap.put("1", 1);
    treeMap.put("2", 2);
    treeMap.put("3", 3);
    assertThat(treeMap.size(), is(3));

    treeMap.remove("3");
    treeMap.remove("2");
    assertThat(treeMap.size(), is(1));
  }

  @Test
  public void throwsNullPointerExceptionWhenNullKeyIsMapped() {
    try {
      treeMap.put(null, 0);
      fail();
    } catch (Exception e) {
      TestCase.assertTrue(e instanceof NullPointerException);
    }
  }

  @Test
  public void onlyContainsItemsThatHaveBeenAdded() {
    treeMap.put("Something", 1);
    assertTrue(treeMap.containsKey("Something"));
    assertTrue(treeMap.containsValue(1));
    assertFalse(treeMap.containsKey("Something else"));
    assertFalse(treeMap.containsValue(2));
  }

  @Test
  public void doesNotContainItemsThatHaveBeenRemoved() {
    treeMap.put("Something", 1);
    assertTrue(treeMap.containsKey("Something"));
    assertTrue(treeMap.remove("Something"));
    assertFalse(treeMap.containsKey("Something"));
    assertFalse(treeMap.remove("Something"));
  }

  @Test
  public void isIterable() {
    int n = 8;
    for (int i = 1; i <= n; i++) {
      treeMap.put("" + i, i);
    }

    int expectedValue = 1;
    for (TreeMap<String, Integer>.Entry<String, Integer> actualEntry : treeMap) {
      String actualKey = actualEntry.getKey();
      String expectedKey = ("" + expectedValue);
      assertThat(actualKey, is(expectedKey));

      Integer actualValue = actualEntry.getValue();
      assertThat(actualValue, is(expectedValue++));
    }

    assertThat(expectedValue, is(n + 1));
  }

  @Test
  public void iteratesInAscendingOrderByKey() {
    /* Adds 1000 random entries to the map. */
    for (int i = 0; i < 1000; i++) {
      treeMap.put("" + random.nextInt(), 1);
    }

    String prev = "";
    String curr = "";

    for (TreeMap<String, Integer>.Entry<String, Integer> entry : treeMap) {
      curr = entry.getKey();
      assertTrue(curr.compareTo(prev) >= 0);
      prev = curr;
    }

  }
}
