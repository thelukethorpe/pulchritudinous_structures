package pulchritudinous.structures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LinkedList<E>
    implements Iterable<E> {

  private final Node head, tail;
  private int size;

  public LinkedList() {
    this.head = new Node(null);
    this.tail = new Node(null);
    this.resetToEmptyState();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof LinkedList) {

      LinkedList that = (LinkedList) obj;
      if (this.size != that.size) {
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
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      private Node curr = head.next;

      @Override
      public boolean hasNext() {
        return curr != tail;
      }

      @Override
      public E next() {
        E item = curr.item;
        curr = curr.next;
        return item;
      }
    };
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.toArray());
  }

  private Node findByIndex(int index) {
    assert (isValidInclusiveIndex(index));
    int midpoint = (size >> 1);
    if (index <= midpoint) {
      return head.walkForwards(index + 1);
    } else {
      return tail.walkBackwards(size - index);
    }
  }

  private Node findByItem(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next) {
      if (curr.item.equals(item)) {
        return curr;
      }
    }
    return null;
  }

  private boolean isValidIndex(int index) {
    return 0 <= index && index < size;
  }

  private boolean isValidInclusiveIndex(int index) {
    return isValidIndex(index) || index == size;
  }

  private void resetToEmptyState() {
    head.setNext(tail);
    size = 0;
  }

  public void add(E item) {
    tail.insertItemJustBefore(item);
  }

  public void addAll(LinkedList<E> items) {
    for (E item : items) {
      this.add(item);
    }
  }

  public void addFirst(E item) {
    this.insertAt(item, 0);
  }

  public void addLast(E item) {
    this.add(item);
  }

  public void clear() {
    this.resetToEmptyState();
  }

  public LinkedList<E> clone() {
    LinkedList<E> clone = new LinkedList<>();
    for (E item : this) {
      clone.add(item);
    }
    return clone;
  }

  public boolean contains(E item) {
    return findByItem(item) != null;
  }

  public E first() {
    return head.next.item;
  }

  public E get(int index) {
    return isValidIndex(index) ? findByIndex(index).item : null;
  }

  public void insertAt(E item, int index) {
    if (isValidInclusiveIndex(index)) {
      Node node = findByIndex(index);
      node.insertItemJustBefore(item);
    }
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public E poll() {
    if (this.isEmpty()) {
      return null;
    }

    Node first = head.next;
    first.removeFromList();
    return first.item;
  }

  public LinkedList<E> pollMany(int n) {
    if (!isValidInclusiveIndex(n)) {
      return null;
    }

    LinkedList<E> that = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      that.add(this.poll());
    }
    return that;
  }

  public boolean remove(E item) {
    Node node = findByItem(item);
    if (node != null) {
      node.removeFromList();
      return true;
    }
    return false;
  }

  public void removeAll(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next) {
      if (curr.item.equals(item)) {
        curr.removeFromList();
      }
    }
  }

  public int size() {
    return size;
  }

  public void sort(BiFunction<E, E, Integer> comparator) {
    if (size > 1) {
      /* Splits the list in half. */
      LinkedList<E> that = this.pollMany(size >> 1);

      /* Sorts sub-lists. */
      this.sort(comparator);
      that.sort(comparator);

      /* Merges sub-lists based on the fact they are well-ordered. */
      LinkedList<E> sorted = new LinkedList<>();

      while (!this.isEmpty() && !that.isEmpty()) {
        if (comparator.apply(this.first(), that.first()) <= 0) {
          sorted.add(this.poll());
        } else {
          sorted.add(that.poll());
        }
      }

      /* Adds any leftover items to the end of the resulting list. */
      LinkedList<E> remainder = !this.isEmpty() ? this : that;
      sorted.addAll(remainder);
      remainder.clear();
      this.addAll(sorted);
    }
  }

  public Object[] toArray() {
    Object[] array = new Object[size];
    int index = 0;
    for (Object item : this) {
      array[index++] = item;
    }
    return array;
  }

  private class Node {
    private final E item;
    private Node prev, next;

    private Node(E item) {
      this.item = item;
      prev = null;
      next = null;
    }

    public void setPrev(Node node) {
      node.next = this;
      this.prev = node;
    }

    public void setNext(Node node) {
      node.prev = this;
      this.next = node;
    }

    public void insertItemJustBefore(E item) {
      if (item == null) {
        throw new NullPointerException("Cannot insert null into LinkedList.");
      }

      Node node = new Node(item);
      node.setPrev(prev);
      node.setNext(this);
      size++;
    }

    public void removeFromList() {
      assert (prev != null && next != null);
      prev.setNext(next);
      size--;
    }

    private Node walk(int steps, Function<Node, Node> walker) {
      if (steps > 0) {
        Node node = walker.apply(this);
        assert (node != null);
        return node.walk(steps - 1, walker);
      }
      return this;
    }

    public Node walkForwards(int steps) {
      return walk(steps, node -> node.next);
    }

    public Node walkBackwards(int steps) {
      return walk(steps, node -> node.prev);
    }
  }
}
