package pulchritudinous.structures;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

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
    for (TreeMap.Entry<String, Integer> actualEntry : treeMap) {
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

    for (TreeMap.Entry<String, Integer> entry : treeMap) {
      curr = entry.getKey();
      assertTrue(curr.compareTo(prev) >= 0);
      prev = curr;
    }
  }

  @Test
  public void iteratingOverEntriesIsSynonymousWithIteratingOverMap() {
    for (int i = 1; i <= 8; i++) {
      treeMap.put("" + i, i);
    }

    List<TreeMap.Entry<String, Integer>> entries = treeMap.getEntries();
    List<String> keys = treeMap.getKeys();
    List<Integer> values = treeMap.getValues();

    for (TreeMap.Entry<String, Integer> actualEntry : treeMap) {
      TreeMap.Entry<String, Integer> expectedEntry = entries.poll();
      String expectedKey = keys.poll();
      Integer expectedValue = values.poll();

      assertThat(actualEntry, is(expectedEntry));
      assertThat(actualEntry.getKey(), is(expectedKey));
      assertThat(actualEntry.getValue(), is(expectedValue));
    }

    assertTrue(entries.isEmpty());
    assertTrue(keys.isEmpty());
    assertTrue(values.isEmpty());
  }

  @Test
  public void mappingsCorrectlyLinkKeysToValues() {
    treeMap.put("Height (cm)", 175);
    treeMap.put("Weight (Kg)", 75);
    treeMap.put("Age (Years)", 25);

    assertThat(treeMap.get("Height (cm)"), is(175));
    assertThat(treeMap.get("Weight (Kg)"), is(75));
    assertThat(treeMap.get("Age (Years)"), is(25));
    assertThat(treeMap.get("Income ($)"), is((Integer) null));
  }

  @Test
  public void onlyReplacesMappingsThatExists() {
    treeMap.put("Something", 1);

    assertThat(treeMap.replace("Something", 42), is(1));
    assertTrue(treeMap.containsKey("Something"));
    assertThat(treeMap.get("Something"), is(42));

    assertThat(treeMap.replace("Something else", 0), is((Integer) null));
    assertFalse(treeMap.containsKey("Something else"));
  }

  @Test
  public void isEmptyAfterClear() {
    treeMap.put("Something in particular", 1);
    treeMap.put("Something else", 2);
    treeMap.put("Another thing", 3);

    treeMap.clear();

    assertTrue(treeMap.isEmpty());
    assertFalse(treeMap.containsKey("Something in particular"));
    assertFalse(treeMap.containsKey("Something else"));
    assertFalse(treeMap.containsKey("Another thing"));
  }

  @Test
  public void isEqualToMapWithTheExactSameMappings() {
    /* Adds 1000 random entries to the map. */
    for (int i = 0; i < 1000; i++) {
      treeMap.put("" + random.nextInt(), 1);
    }

    TreeMap<String, Integer> anotherTreeMap = new TreeMap<>();
    for (TreeMap.Entry<String, Integer> entry : treeMap) {
      anotherTreeMap.put(entry.getKey(), entry.getValue());
    }

    assertThat(treeMap, is(anotherTreeMap));
    assertThat(treeMap.hashCode(), is(anotherTreeMap.hashCode()));
  }

  @Test
  public void returnsDefaultValueOnlyWhenMappingDoesNotExist() {
    treeMap.put("Something", 42);

    assertThat(treeMap.getOrDefault("Something", 0), is(42));
    assertThat(treeMap.getOrDefault("Something else", 1), is(1));
  }

  @Test
  public void putsIfAbsentOnlyWhenMappingDoesNotExist() {
    assertThat(treeMap.putIfAbsent("Something", 42), is((Integer) null));
    assertThat(treeMap.putIfAbsent("Something", 0), is(42));
    assertThat(treeMap.get("Something"), is(42));

  }

  @Test
  public void putsComputedValueWhenKeyIsUnmappedAndMergesWhenMapped() {
    TreeMap<String, Integer> anotherTreeMap = new TreeMap<>();
    String[] someWords = new String[]{
        "Only once",
        "Twice",
        "Thrice",
        "Five times!",
        "Twice",
        "Thrice",
        "Five times!",
        "Thrice",
        "Five times!",
        "Five times!",
        "Five times!"
    };

    for (String word : someWords) {
      treeMap.compute(word, (w, f) -> f != null ? f + 1 : 1);
      anotherTreeMap.merge(word, 1, (f, n) -> f + n);
    }

    assertThat(treeMap.size(), is(4));
    assertThat(treeMap.get("Only once"), is(1));
    assertThat(treeMap.get("Twice"), is(2));
    assertThat(treeMap.get("Thrice"), is(3));
    assertThat(treeMap.get("Five times!"), is(5));
    assertThat(treeMap, is(anotherTreeMap));
  }

  @Test
  public void removesNullComputations() {
    treeMap.put("Something", 0);
    treeMap.put("Remove me!", 1);
    treeMap.put("Something else", 2);
    treeMap.put("I also want to be REMOVED", 3);

    BiFunction<String, Integer, Integer> removeIfMarked
        = (k, v) -> k.toLowerCase().contains("remove") ? null : v;

    for (String key : treeMap.getKeys()) {
      treeMap.compute(key, removeIfMarked);
    }

    assertFalse(treeMap.containsKey("Remove me!"));
    assertFalse(treeMap.containsKey("I also want to be REMOVED"));
    assertThat(treeMap.get("Something"), is(0));
    assertThat(treeMap.get("Something else"), is(2));
  }

  @Test
  public void returnsResultOfComputation() {
    for (int i = 1; i <= 8; i++) {
      Integer j = i;
      assertThat(treeMap.compute("" + i, (k, v) -> j), is(j));
      assertTrue(treeMap.containsKey("" + i));
    }
  }

  @Test
  public void onlyComputesUnderCorrectConditions() {
    treeMap.put("Something", 0);
    Function<String, Integer> giveMeaning = (k) -> 42;
    BiFunction<String, Integer, Integer> giveMoreMeaning = (k, v) -> 42;

    assertThat(treeMap.computeIfPresent("Something", giveMoreMeaning), is(42));
    assertThat(treeMap.computeIfAbsent("Something", giveMeaning), is((Integer) null));

    assertThat(treeMap.computeIfPresent("Something else", giveMoreMeaning), is((Integer) null));
    assertThat(treeMap.computeIfAbsent("Something else", giveMeaning), is(42));
    assertThat(treeMap.get("Something else"), is(42));
  }

  @Test
  public void isDeepCloneable() {
    treeMap.put("Something in particular", 1);
    treeMap.put("Something else", 2);
    treeMap.put("Another thing", 3);

    TreeMap<String, Integer> treeMapClone = treeMap.clone();
    assertThat(treeMap, is(treeMapClone));
    treeMap.clear();

    assertThat(treeMapClone.get("Something in particular"), is(1));
    assertThat(treeMapClone.get("Something else"), is(2));
    assertThat(treeMapClone.get("Another thing"), is(3));
  }

  @Test
  public void containsAllItemsThatHaveBeenAdded() {
    TreeMap<String, Integer> someMappings = new TreeMap<>();
    someMappings.put("Something in particular", 1);
    someMappings.put("Something else", 2);
    someMappings.put("Another thing", 3);

    treeMap.put("I was here before anything else", 4);
    treeMap.put("Me too!", 5);
    treeMap.putAll(someMappings);

    assertThat(treeMap.get("I was here before anything else"), is(4));
    assertThat(treeMap.get("Me too!"), is(5));

    assertThat(someMappings.get("Something in particular"), is(1));
    assertThat(someMappings.get("Something else"), is(2));
    assertThat(someMappings.get("Another thing"), is(3));

    assertThat(treeMap.get("Something in particular"), is(1));
    assertThat(treeMap.get("Something else"), is(2));
    assertThat(treeMap.get("Another thing"), is(3));
  }

  @Test
  public void replacesAllValuesButDoesNotChangeKeys() {
    BiFunction<String, Integer, Integer> addKeyToValue = (s, x) -> {
      int y = Integer.parseInt(s);
      return x + y;
    };

    int n = 8;
    for (int i = 0; i <= n; i++) {
      treeMap.put("" + i, n - i);
    }

    treeMap.replaceAll(addKeyToValue);
    assertThat(treeMap.size(), is(n + 1));

    for (Integer value : treeMap.getValues()) {
      assertThat(value, is(n));
    }
  }

  @Test
  public void isRobust() {
    /* Adds and removes mappings in runs of increasing size.  */
    int numberOfMappings = 2500;
    int numberOfRuns = 10;
    int runSize = numberOfMappings / numberOfRuns;

    TreeMap.Entry<String, Integer>[] someMappings = new TreeMap.Entry[numberOfMappings];
    Integer[] someInts = TestUtils.getUniqueRandoms(random, numberOfMappings);

    for (int i = 0; i < numberOfMappings; i++) {
      someMappings[i] = new TreeMap.Entry<>("" + someInts[i], i);
    }

    for (int i = 0, n = runSize; i < numberOfRuns; i++, n += runSize) {

      for (int j = 0; j < n; j++) {
        TreeMap.Entry<String, Integer> mapping = someMappings[j];
        assertFalse(treeMap.containsKey(mapping.getKey()));
        assertThat(treeMap.put(mapping.getKey(), mapping.getValue()), is((Integer) null));
        assertThat(treeMap.size(), is(j + 1));
      }

      TestUtils.shuffle(random, someMappings, 0, n);

      for (int j = 0; j < n; j++) {
        TreeMap.Entry<String, Integer> mapping = someMappings[j];
        assertTrue(treeMap.containsKey(mapping.getKey()));
        assertThat(treeMap.get(mapping.getKey()), is(mapping.getValue()));
        assertTrue(treeMap.remove(mapping.getKey()));
        assertFalse(treeMap.containsKey(mapping.getKey()));
        assertThat(treeMap.size(), is(n - j - 1));
      }

      assertTrue(treeMap.isEmpty());
      assertTrue(treeMap.getEntries().isEmpty());
    }
  }
}
