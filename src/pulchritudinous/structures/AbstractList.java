package pulchritudinous.structures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public abstract class AbstractList<E> implements List<E> {

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

  protected abstract AbstractList<E> newEmptyList();

  protected void resetToEmptyState() {
    size = 0;
  }

  @Override
  public void add(E item) {
    this.addLast(item);
  }

  @Override
  public void addAll(List<E> items) {
    for (E item : items) {
      this.add(item);
    }
  }

  @Override
  public void addFirst(E item) {
    this.insertAt(item, 0);
  }

  @Override
  public void addLast(E item) {
    this.insertAt(item, size());
  }

  @Override
  public void clear() {
    this.resetToEmptyState();
  }

  @Override
  public List<E> clone() {
    List<E> clone = newEmptyList();
    for (E item : this) {
      clone.add(item);
    }
    return clone;
  }

  @Override
  public boolean contains(E item) {
    return findByItem(item) != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof List) {

      List that = (List) obj;
      if (this.size() != that.size()) {
        return false;
      }

      Iterator thisIterator = this.iterator();
      Iterator thatIterator = that.iterator();

      while (thisIterator.hasNext() && thatIterator.hasNext()) {
        Object thisItem = thisIterator.next();
        Object thatItem = thatIterator.next();
        if (!thisItem.equals(thatItem)) {
          return false;
        }
      }

      assert (thisIterator.hasNext() == thatIterator.hasNext());
      return true;
    } else {
      return false;
    }
  }

  @Override
  public E first() {
    return this.get(0);
  }

  @Override
  public E get(int index) {
    return isValidIndex(index) ? findByIndex(index) : null;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.toArray());
  }

  @Override
  public E last() {
    return this.get(size() - 1);
  }

  @Override
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

  @Override
  public abstract void insertAt(E item, int index);

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public E poll() {
    E first = first();
    removeAt(0);
    return first;
  }

  @Override
  public List<E> pollMany(int n) {
    if (!isValidInclusiveIndex(n)) {
      return null;
    }

    List<E> that = newEmptyList();
    for (int i = 0; i < n; i++) {
      that.add(this.poll());
    }
    return that;
  }

  @Override
  public abstract boolean remove(E item);

  @Override
  public abstract void removeAll(E item);

  @Override
  public abstract void removeAt(int index);

  @Override
  public abstract void replaceAll(UnaryOperator<E> operator);

  @Override
  public abstract E set(E item, int index);

  @Override
  public int size() {
    return size;
  }

  @Override
  public void sort(BiFunction<E, E, Integer> comparator) {
    if (size() > 1) {
      /* Splits the list in half. */
      List<E> that = this.pollMany(size() >> 1);

      /* Sorts sub-lists. */
      this.sort(comparator);
      that.sort(comparator);

      /* Merges sub-lists based on the fact they are well-ordered. */
      List<E> sorted = newEmptyList();

      while (!this.isEmpty() && !that.isEmpty()) {
        if (comparator.apply(this.first(), that.first()) <= 0) {
          sorted.add(this.poll());
        } else {
          sorted.add(that.poll());
        }
      }

      /* Adds any leftover items to the end of the resulting list. */
      List<E> remainder = !this.isEmpty() ? this : that;
      sorted.addAll(remainder);
      remainder.clear();
      this.addAll(sorted);
    }
  }

  @Override
  public Object[] toArray() {
    Object[] array = new Object[size()];
    int index = 0;
    for (Object item : this) {
      array[index++] = item;
    }
    return array;
  }
}
