package pulchritudinous.structures;

import java.util.Random;

public final class TestUtils {

  public static int getRandom(Random random, int min, int max) {
    return random.nextInt(max - min) + min;
  }

  public static Integer[] getUniqueRandoms(Random random, int length) {
    int interval = Integer.MAX_VALUE / length;
    Integer[] uniqueRandoms = new Integer[length];

    for (int i = 0, min = 0, max = interval; i < length; i++, min += interval, max += interval) {
      uniqueRandoms[i] = getRandom(random, min, max);
    }

    shuffle(random, uniqueRandoms);
    return uniqueRandoms;
  }

  public static <E> void shuffle(Random random, E[] array) {
    shuffle(random, array, 0, array.length);
  }

  public static <E> void shuffle(Random random, E[] array, int start, int length) {
    assert (start + length <= array.length);
    for (int i = 0; i < length; i++) {
      int j = getRandom(random, 0, length);
      E temp = array[i + start];
      array[i + start] = array[j + start];
      array[j + start] = temp;
    }
  }

  public static class Pair<FirstT, SecondT> {
    private final FirstT first;
    private final SecondT second;

    public Pair(FirstT first, SecondT second) {
      this.first = first;
      this.second = second;
    }

    public FirstT getFirst() {
      return first;
    }

    public SecondT getSecond() {
      return second;
    }
  }
}
