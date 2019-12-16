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

  private Node findByIndex(int index) {
    assert(isValidIndex(index));
    int midpoint = (size >> 1);
    if (index <= midpoint) {
      return head.walkForwards(index + 1);
    } else {
      return tail.walkBackwards(size - index);
    }
  }

  private boolean isValidIndex(int index) {
    return 0 <= index && index < size;
  }

  private boolean isValidInclusiveIndex(int index) {
    return isValidIndex(index) || index == size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void add(E item) {
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
    return isValidIndex(index) ? findByIndex(index).item : null;
  }

  public void insertAt(E item, int index) {
    if (isValidInclusiveIndex(index)) {
      Node node = findByIndex(index);
      node.insertItemJustBefore(item);
    }
  }

  public LinkedList<E> pollMany(int n) {
    if (!isValidInclusiveIndex(n)) {
      return null;
    }

    LinkedList<E> pollCollection = new LinkedList<>();
    Node node = findByIndex(n);

    pollCollection.head.setNext(head.next);
    pollCollection.tail.setPrev(node.prev);
    pollCollection.size = n;

    this.head.setNext(node);
    this.size = size - n;

    return pollCollection;
  }

  public E poll() {
    if (this.isEmpty()) {
      return null;
    }

    E first = this.first();
    head.setNext(head.next.next);
    return first;
  }

  public E first() {
    return head.next.item;
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
      if (item == null) {
        throw new NullPointerException("Cannot insert null into LinkedList.");
      }

      Node node = new Node(item);
      node.setPrev(prev);
      node.setNext(this);
      size++;
    }

    public void removeFromList() {
      assert(prev != null && next != null);
      prev.setNext(next);
      size--;
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
