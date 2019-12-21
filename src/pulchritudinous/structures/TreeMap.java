package pulchritudinous.structures;

public class TreeMap<K extends Comparable<K>, V> {

  private final Node root;
  private int size;

  public TreeMap() {
    this.root = new LinkNode();
    this.size = 0;
  }

  private Node findNodeByKey(K key) {
    return root.searchFor(key);
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public V put(K key, V value) {
    Node node = findNodeByKey(key);
    return node.add(key, value);
  }

  public int size() {
    return size;
  }

  public boolean remove(K key) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      node.asInternalNode().removeFromMap();
      return true;
    }
    return false;
  }

  private abstract class Node {
    public abstract V add(K key, V value);

    public abstract InternalNode asInternalNode();

    public abstract boolean isMappedBy(K key);

    public abstract Node next(K key);

    public abstract V replace(K key, V value);

    public Node searchFor(K key) {
      Node prev = this;
      Node curr = this.next(key);

      while (curr != null) {
        prev = curr;
        curr = curr.next(key);
      }

      return prev;
    }
  }

  private class InternalNode extends Node {
    private final LinkNode left, right;
    private K key;
    private V value;
    private LinkNode parent;

    private InternalNode(K key, V value, LinkNode parent) {
      if (key == null) {
        throw new NullPointerException("Cannot put null mapping into TreeMap.");
      }

      this.left = new LinkNode();
      this.right = new LinkNode();
      this.key = key;
      this.value = value;
      this.parent = parent;
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
    public InternalNode asInternalNode() {
      return this;
    }

    @Override
    public boolean isMappedBy(K key) {
      return this.key.compareTo(key) == 0;
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

    public void removeFromMap() {
      if (right.hasChild()) {
        InternalNode node = right.searchFor(key).asInternalNode();
        node.removeFromMap();
        this.key = node.key;
        this.value = node.value;
      } else {
        parent.setChild(left);
        size--;
      }
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
    public InternalNode asInternalNode() {
      return null;
    }

    public boolean hasChild() {
      return child != null;
    }

    @Override
    public boolean isMappedBy(K key) {
      return false;
    }

    @Override
    public Node next(K UNUSED) {
      return this.child;
    }

    @Override
    public V replace(K key, V value) {

      if (child == null) {
        child = new InternalNode(key, value, this);
        size++;
        return null;
      }
      return child.replace(key, value);
    }

    public void setChild(LinkNode link) {
      this.child = link.child;
      if (child != null) {
        child.parent = this;
      }
    }
  }
}
