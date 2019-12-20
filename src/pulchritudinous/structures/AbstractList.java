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

  protected boolean isValidIndex(int index) {
    return 0 <= index && index < size;
  }

  protected void resetToEmptyState() {
    size = 0;
  }

  public abstract void add(E item);

  public boolean isEmpty() {
    return size == 0;
  }

  public abstract boolean remove(E s);

  public int size() {
    return size;
  }
}
