package pulchritudinous.structures;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public abstract class AbstractListTest {
  private final AbstractList<String> abstractList;

  public AbstractListTest() {
    this.abstractList = newEmptyList();
  }

  protected abstract AbstractList newEmptyList();

  @Test
  public void isEmptyUponInitialization() {
    assertTrue(newEmptyList().isEmpty());
  }
}
