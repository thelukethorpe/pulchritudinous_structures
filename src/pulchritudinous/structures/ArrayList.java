package pulchritudinous.structures;

public class ArrayList<E> extends AbstractList<E> {

  private static final int INITIAL_LENGTH = 128;
  private static final int NULL_INDEX = -1;

  private int firstIndex;
  private int length;
  private Object[] contents;

  public ArrayList() {
    super();
    this.resetToEmptyState();
  }

  @Override
  protected E findByIndex(int index) {
    assert (isValidIndex(index));
    return (E) getAtIndex(index);
  }

  @Override
  protected E findByItem(E item) {
    int index = findIndexByItem(item);
    return index != NULL_INDEX ? (E) getAtIndex(index) : null;
  }

  private int findIndexByItem(E item) {
    for (int i = 0; i < size(); i++) {
      if (getAtIndex(i).equals(item)) {
        return i;
      }
    }
    return NULL_INDEX;
  }

  private void expand() {
    int length = this.length << 1;
    Object[] contents = new Object[length];

    for (int i = 0; i < this.length; i++) {
      contents[i] = getAtIndex(i);
    }

    this.firstIndex = 0;
    this.length = length;
    this.contents = contents;
  }

  private Object getAtIndex(int index) {
    return contents[offset(index)];
  }

  private int lastIndex() {
    return size() - 1;
  }

  private int offset(int index) {
    return (firstIndex + index) & (length - 1);
  }

  @Override
  protected void resetToEmptyState() {
    super.resetToEmptyState();
    firstIndex = 0;
    length = INITIAL_LENGTH;
    contents = new Object[length];
  }

  private void setAtIndex(Object item, int index) {
    contents[offset(index)] = item;
  }

  @Override
  public void insertAt(E item, int index) {
    if (item == null) {
      throw new NullPointerException("Cannot insert null into ArrayList.");
    }

    if (isValidInclusiveIndex(index)) {
      if (size() == length) {
        this.expand();
      }

      for (int i = size(); i > index; i--) {
        Object prev = getAtIndex(i - 1);
        setAtIndex(prev, i);
      }
      setAtIndex(item, index);
      incrementSize();
    }
  }

  @Override
  public E poll() {
    E first = first();
    if (first != null) {
      setAtIndex(null, 0);
      firstIndex = offset(1);
      decrementSize();
    }
    return first;
  }

  @Override
  public boolean remove(E item) {
    int index = findIndexByItem(item);
    if (!isValidIndex(index)) {
      return false;
    }

    decrementSize();
    for (int i = index; i < size(); i++) {
      Object next = getAtIndex(i + 1);
      setAtIndex(next, i);
    }
    return true;
  }

  @Override
  public void removeAll(E item) {
    int size = size();

    for (int i = 0, j = 0; i < size; i++) {
      Object curr = getAtIndex(i);
      if (!curr.equals(item)) {
        setAtIndex(curr, j++);
      } else {
        decrementSize();
      }
    }

    for (int i = size(); i < size; i++) {
      setAtIndex(null, i);
    }
  }
}
