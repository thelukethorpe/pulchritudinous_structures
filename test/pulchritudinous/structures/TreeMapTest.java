package pulchritudinous.structures;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class TreeMapTest {
  private final TreeMap treeMap = new TreeMap();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new TreeMap().isEmpty());
  }

  @Test
  public void hasSizeOfZeroUponInitialization() {
    assertThat(new TreeMap().size(), is(0));
  }
}
