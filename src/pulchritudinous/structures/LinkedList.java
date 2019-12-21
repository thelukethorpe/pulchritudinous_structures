package pulchritudinous.structures;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class LinkedList<E> extends AbstractList<E> {

  private final Node head, tail;

  public LinkedList() {
    super();
    this.head = new Node(null);
    this.tail = new Node(null);
    this.resetToEmptyState();
  }

  @Override
  protected E findByIndex(int index) {
    Node node = findNodeByIndex(index);
    return node != null ? node.item : null;
  }

  @Override
  protected E findByItem(E item) {
    Node node = findNodeByItem(item);
    return node != null ? node.item : null;
  }

  private Node findNodeByIndex(int index) {
    assert (isValidInclusiveIndex(index));
    int midpoint = (size() >> 1);
    if (index <= midpoint) {
      return head.walkForwards(index + 1);
    } else {
      return tail.walkBackwards(size() - index);
    }
  }

  private Node findNodeByItem(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next) {
      if (curr.item.equals(item)) {
        return curr;
      }
    }
    return null;
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
  protected LinkedList<E> newEmptyList() {
    return new LinkedList<>();
  }

  @Override
  protected void resetToEmptyState() {
    super.resetToEmptyState();
    head.setNext(tail);
  }

  @Override
  public void insertAt(E item, int index) {
    if (isValidInclusiveIndex(index)) {
      Node node = findNodeByIndex(index);
      node.insertItemJustBefore(item);
    }
  }

  @Override
  public boolean remove(E item) {
    Node node = findNodeByItem(item);
    if (node != null) {
      node.removeFromList();
      return true;
    }
    return false;
  }

  @Override
  public void removeAll(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next) {
      if (curr.item.equals(item)) {
        curr.removeFromList();
      }
    }
  }

  @Override
  public void removeAt(int index) {
    if (isValidIndex(index)) {
      Node node = findNodeByIndex(index);
      node.removeFromList();
    }
  }

  @Override
  public void replaceAll(UnaryOperator<E> operator) {
    for (Node curr = head.next; curr != tail; curr = curr.next) {
      curr.replaceWith(operator.apply(curr.item));
    }
  }

  @Override
  public E set(E item, int index) {
    if (!isValidIndex(index)) {
      return null;
    }

    Node node = findNodeByIndex(index);
    node.replaceWith(item);
    return node.item;
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
      incrementSize();
    }

    public void removeFromList() {
      assert (prev != null && next != null);
      prev.setNext(next);
      decrementSize();
    }

    public void replaceWith(E item) {
      this.insertItemJustBefore(item);
      this.removeFromList();
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
