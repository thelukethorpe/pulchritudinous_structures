package pulchritudinous.structures;

public class ArrayList<E> extends AbstractList<E> {

  private static final int INITIAL_LENGTH = 128;
  private static final int NULL_INDEX = -1;

  private int length;
  private Object[] contents;

  public ArrayList() {
    super();
    this.resetToEmptyState();
  }

  @Override
  protected E findByIndex(int index) {
    assert(isValidIndex(index));
    return (E) contents[index];
  }

  @Override
  protected E findByItem(E item) {
    int index = findIndexByItem(item);
    return index != NULL_INDEX ? (E) contents[index] : null;
  }

  private int findIndexByItem(E item) {
    for (int i = 0; i < size(); i++) {
      if (contents[i].equals(item)) {
        return i;
      }
    }
    return NULL_INDEX;
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
    if (item == null) {
      throw new NullPointerException("Cannot insert null into ArrayList.");
    }

    int nextIndex = lastIndex() + 1;
    if (nextIndex == length) {
      this.expand();
    }
    contents[nextIndex] = item;
    incrementSize();
  }

  @Override
  public void insertAt(E item, int index) {
    if (isValidInclusiveIndex(index)) {
      for (int i = size(); i > index; i--) {
        contents[i] = contents[i - 1];
      }
      contents[index] = item;
      incrementSize();
    }
  }

  @Override
  public boolean remove(E item) {
    int index = findIndexByItem(item);
    if (!isValidIndex(index)) {
      return false;
    }

    decrementSize();
    for (int i = index; i < size(); i++) {
      contents[i] = contents[i + 1];
    }
    return true;
  }

  @Override
  public void removeAll(E item) {
    int size = size();

    for (int i = 0, j = 0; i < size; i++) {
      if (!contents[i].equals(item)) {
        contents[j++] = contents[i];
      } else {
        decrementSize();
      }
    }

    for (int i = size(); i < size; i++) {
      contents[i] = null;
    }
  }
}
