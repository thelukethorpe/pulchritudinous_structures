package pulchritudinous.structures;

import java.util.function.Function;

public class LinkedList<E> {

  private final Node head, tail;
  private int size;

  public LinkedList() {
    this.size = 0;
    this.head = new Node(null);
    this.tail = new Node(null);
    this.head.setNext(this.tail);
  }

  private Node findByItem(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next){
      if (curr.item.equals(item)) {
        return curr;
      }
    }
    return null;
  }

  private boolean isValidIndex(int index) {
    return 0 <= index && index < size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void add(E item) {
    size++;
    tail.insertItemJustBefore(item);
  }

  public boolean contains(E item) {
    return findByItem(item) != null;
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
    for (Node curr = head.next; curr != tail; curr = curr.next){
      if (curr.item.equals(item)) {
        curr.removeFromList();
      }
    }
  }

  public E get(int index) {
    if (!isValidIndex(index)) {
      return null;
    }

    int midpoint = (size >> 1);
    if (index <= midpoint) {
      return head.walkForwards(index + 1).item;
    } else {
      return tail.walkBackwards(size - index).item;
    }
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

    private void insertItemJustBefore(E item) {
      Node node = new Node(item);

      node.setPrev(prev);
      node.setNext(this);
    }

    public void removeFromList() {
      assert(prev != null && next != null);
      prev.setNext(next);
    }

    private Node walk(int steps, Function<Node, Node> walker) {
      if (steps > 0) {
        Node node = walker.apply(this);
        assert(node != null);
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
