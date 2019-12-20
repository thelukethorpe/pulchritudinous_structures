package pulchritudinous.structures;

public class ArrayList<E> extends AbstractList<E> {

  private static final int INITIAL_LENGTH = 128;

  private int length;
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

  private int findByItem(E item) {
    for (int i = 0; i < size(); i++) {
      if (contents[i].equals(item)) {
        return i;
      }
    }
    return -1;
  }

  private int lastIndex() {
    return size() - 1;
  }

  @Override
  protected void resetToEmptyState() {
    super.resetToEmptyState();
    length = INITIAL_LENGTH;
    contents = new Object[length];
  }

  @Override
  public void add(E item) {
    int nextIndex = lastIndex() + 1;
    if (nextIndex == length) {
      this.expand();
    }
    contents[nextIndex] = item;
    incrementSize();
  }

  @Override
  public boolean remove(E item) {
    int index = findByItem(item);
    if (!isValidIndex(index)) {
      return false;
    }

    decrementSize();
    for (int i = index; i < size(); i++) {
      contents[i] = contents[i + 1];
    }
    return true;
  }
}
