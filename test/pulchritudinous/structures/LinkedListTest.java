package pulchritudinous.structures;

public class LinkedListTest extends ListTest {

  @Override
  protected AbstractList newEmptyList() {
    return new LinkedList();
  }
}
