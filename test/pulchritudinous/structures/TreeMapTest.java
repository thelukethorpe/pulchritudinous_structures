package pulchritudinous.structures;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TreeMapTest {
  private final TreeMap treeMap = new TreeMap();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(new TreeMap().isEmpty());
  }
}
