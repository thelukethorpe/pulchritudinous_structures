package pulchritudinous.structures;

public class ArrayList<E> extends AbstractList<E> {

  private static final int INITIAL_LENGTH = 128;

  private int length;
  private int lastIndex;
  private Object[] contents;

  public ArrayList() {
    super();
    this.resetToEmptyState();
  }

  private void expand() {
    int length = this.length << 1;
    Object[] contents = new Object[length];

    for (int i = 0; i < this.length; i++) {
      contents[i] = this.contents[i];
    }

    this.length = length;
    this.contents = contents;
  }

  @Override
  protected void resetToEmptyState() {
    super.resetToEmptyState();
    length = INITIAL_LENGTH;
    lastIndex = 0;
    contents = new Object[length];
  }

  @Override
  public void add(E item) {
    int nextIndex = lastIndex + 1;
    if (!isValidIndex(nextIndex)) {
      this.expand();
    }
    contents[nextIndex] = item;
    incrementSize();
    lastIndex = nextIndex;
  }
}
