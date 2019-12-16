package pulchritudinous.structures;

public class LinkedList<E> {

  private final Node head, tail;
  private int size;

  public LinkedList() {
    this.size = 0;
    this.head = new Node(null);
    this.tail = new Node(null);
    this.head.setNext(this.tail);
  }

  private void insertJustBefore(E item, Node next) {
    Node prev = next.prev;
    Node node = new Node(item);

    node.setPrev(prev);
    node.setNext(next);
  }

  private Node findByItem(E item) {
    for (Node curr = head.next; curr != tail; curr = curr.next){
      if (curr.item.equals(item)) {
        return curr;
      }
    }
    return null;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void add(E item) {
    size++;
    insertJustBefore(item, tail);
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

    public void removeFromList() {
      assert(prev != null && next != null);
      prev.setNext(next);
    }
  }
}
