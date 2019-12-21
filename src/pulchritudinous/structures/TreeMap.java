package pulchritudinous.structures;

public class TreeMap<K extends Comparable<K>, V> {

  private final Node root;
  private int size;

  public TreeMap() {
    this.root = new LinkNode();
    this.size = 0;
  }

  private Node findNodeByKey(K key) {
    Node prev = root;
    Node curr = root.next(key);

    while (curr != null) {
      prev = curr;
      curr = curr.next(key);
    }

    return prev;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public V put(K key, V value) {
    Node node = findNodeByKey(key);
    return node.add(key, value);
  }

  public int size() {
    return 0;
  }

  private abstract class Node {
    public abstract V add(K key, V value);

    public abstract Node next(K key);

    public abstract V replace(K key, V value);
  }

  private class InternalNode extends Node {
    private final LinkNode left, right;
    private final K key;
    private V value;

    private InternalNode(K key, V value) {
      this.left = new LinkNode();
      this.right = new LinkNode();
      this.key = key;
      this.value = value;
    }

    @Override
    public V add(K key, V value) {
      Node node;
      int thisMinusThat = this.key.compareTo(key);
      if (thisMinusThat > 0) {
        node = left;
      } else if (thisMinusThat < 0) {
        node = right;
      } else {
        node = this;
      }
      return node.replace(key, value);
    }

    @Override
    public Node next(K key) {
      int thisMinusThat = this.key.compareTo(key);
      if (thisMinusThat > 0) {
        return this.left.next(key);
      } else if (thisMinusThat < 0) {
        return this.right.next(key);
      }
      return null;
    }

    @Override
    public V replace(K key, V value) {
      assert (this.key.compareTo(key) == 0);
      V prev = this.value;
      this.value = value;
      return prev;
    }
  }

  private class LinkNode extends Node {
    private InternalNode child;

    private LinkNode() {
      this.child = null;
    }

    @Override
    public V add(K key, V value) {
      assert (this.child == null);
      return this.replace(key, value);
    }

    @Override
    public Node next(K UNUSED) {
      return this.child;
    }

    @Override
    public V replace(K key, V value) {
      if (child == null) {
        child = new InternalNode(key, value);
        size++;
        return null;
      }
      return child.replace(key, value);
    }
  }
}
