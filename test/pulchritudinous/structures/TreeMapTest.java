package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class TreeMapTest {
  private final TreeMap<String, Integer> treeMap = new TreeMap<>();

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
}
