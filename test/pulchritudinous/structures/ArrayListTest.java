package pulchritudinous.structures;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArrayListTest extends ListTest {
  private final ArrayList<String> arrayList = new ArrayList<>();

  @Override
  protected AbstractList newEmptyList() {
    return new ArrayList();
  }

  @Test
  public void canHoldLotsOfItems() {
    int largeSize = 1_000_000;
    for (int i = 0; i < largeSize; i++) {
      arrayList.add("" + i);
    }

    assertThat(arrayList.size(), is(largeSize));

    for (int i = 0; i < largeSize; i++) {
      assertThat(arrayList.get(i), is("" + i));
    }
  }
}
