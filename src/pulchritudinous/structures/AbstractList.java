package pulchritudinous.structures;

import java.util.function.UnaryOperator;

public abstract class AbstractList<E> implements Iterable<E> {

  protected static final int NULL_INDEX = -1;

  private int size;

  protected AbstractList() {
    size = 0;
  }

  protected void incrementSize() {
    size++;
  }

  protected void decrementSize() {
    size--;
  }

  protected abstract E findByIndex(int index);

  protected abstract E findByItem(E item);

  protected boolean isValidIndex(int index) {
    return 0 <= index && index < size;
  }

  protected boolean isValidInclusiveIndex(int index) {
    return isValidIndex(index) || index == size();
  }

  protected void resetToEmptyState() {
    size = 0;
  }

  public void add(E item) {
    this.addLast(item);
  }

  public void addFirst(E item) {
    this.insertAt(item, 0);
  }

  public void addLast(E item) {
    this.insertAt(item, size());
  }

  public void clear() {
    this.resetToEmptyState();
  }

  public boolean contains(E item) {
    return findByItem(item) != null;
  }

  public E first() {
    return this.get(0);
  }

  public E get(int index) {
    return isValidIndex(index) ? findByIndex(index) : null;
  }

  public int indexOf(E item) {
    int index = 0;
    for (E curr : this) {
      if (curr.equals(item)) {
        return index;
      } else {
        index++;
      }
    }
    return NULL_INDEX;
  }

  public abstract void insertAt(E item, int index);

  public boolean isEmpty() {
    return size == 0;
  }

  public E poll() {
    E first = first();
    removeAt(0);
    return first;
  }

  public abstract boolean remove(E item);

  public abstract void removeAll(E item);

  public abstract void removeAt(int index);

  public abstract void replaceAll(UnaryOperator<E> operator);

  public abstract E set(E item, int index);

  public int size() {
    return size;
  }
}
