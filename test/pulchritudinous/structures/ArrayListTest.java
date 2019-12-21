package pulchritudinous.structures;

public class ArrayListTest extends ListTest {

  @Override
  protected AbstractList newEmptyList() {
    return new ArrayList();
  }
}
