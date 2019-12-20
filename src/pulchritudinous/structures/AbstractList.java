package pulchritudinous.structures;

public abstract class AbstractList<E> {

  private int size;

  protected AbstractList() {
    this.resetToEmptyState();
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

  protected void resetToEmptyState() {
    size = 0;
  }

  public abstract void add(E item);

  public boolean contains(E item) {
    return findByItem(item) != null;
  }

  public E get(int index) {
    return isValidIndex(index) ? findByIndex(index) : null;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public abstract boolean remove(E item);

  public abstract void removeAll(E item);

  public int size() {
    return size;
  }
}
