package pulchritudinous.structures;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreeMap<K extends Comparable<K>, V> implements Iterable<TreeMap<K, V>.Entry<K, V>> {

  private final LinkNode root;
  private int size;

  public TreeMap() {
    this.root = new LinkNode(null);
    this.resetToEmptyState();
  }

  private void assertThatTreeIsCorrectlyStructured() {
    assert (root.getRedBlackIndex() >= 0);
  }

  private V computeIf(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunc, Predicate<Node> condition) {
    Node node = findNodeByKey(key);
    return condition.test(node) ? node.remap(key, remappingFunc) : null;
  }

  private Node findNodeByKey(K key) {
    return root.searchFor(key);
  }

  private void resetToEmptyState() {
    this.root.child = null;
    this.size = 0;
  }

  private <E> List<E> toOrderedList(Function<InternalNode, E> collector) {
    List<E> list = new LinkedList<>();
    root.collectInOrder(list, collector);
    return list;
  }

  public void clear() {
    this.resetToEmptyState();
  }

  @Override
  public TreeMap<K, V> clone() {
    TreeMap<K, V> clone = new TreeMap<>();
    BiConsumer<LinkNode, LinkNode> cloner = (thisNode, thatNode)
        -> thisNode.add(thatNode.child.key, thatNode.child.value);
    clone.root.applyToAll(this.root, cloner);
    return clone;
  }

  public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunc) {
    return computeIf(key, remappingFunc, node -> true);
  }

  public V computeIfAbsent(K key, Function<? super K, ? extends V> remappingFunc) {
    return computeIf(key, (k, v) -> remappingFunc.apply(k), node -> !node.isMappedBy(key));
  }

  public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunc) {
    return computeIf(key, remappingFunc, node -> node.isMappedBy(key));
  }

  public boolean containsKey(K key) {
    return findNodeByKey(key).isMappedBy(key);
  }

  public boolean containsValue(V value) {
    return this.getValues().contains(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof TreeMap) {
      TreeMap that = (TreeMap) obj;
      return this.size() == that.size() && this.getEntries().equals(that.getEntries());
    } else {
      return false;
    }
  }

  public V get(K key) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      return node.asInternalNode().getValue();
    }
    return null;
  }

  public List<Entry<K, V>> getEntries() {
    return this.toOrderedList(InternalNode::toEntry);
  }

  public List<K> getKeys() {
    return this.toOrderedList(InternalNode::getKey);
  }

  public V getOrDefault(K key, V defaultValue) {
    V value = this.get(key);
    return value != null ? value : defaultValue;
  }

  public List<V> getValues() {
    return this.toOrderedList(InternalNode::getValue);
  }

  @Override
  public int hashCode() {
    return this.getEntries().hashCode();
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    return this.getEntries().iterator();
  }

  public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunc) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      return node.remap(key, (k, v) -> remappingFunc.apply(v, value));
    } else {
      return node.add(key, value);
    }
  }

  public V put(K key, V value) {
    Node node = findNodeByKey(key);
    return node.add(key, value);
  }

  public void putAll(TreeMap<? extends K, ? extends V> treeMap) {
    for (TreeMap<? extends K, ? extends V>.Entry<? extends K, ? extends V> entry : treeMap) {
      this.put(entry.getKey(), entry.getValue());
    }
  }

  public V putIfAbsent(K key, V value) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      return node.asInternalNode().getValue();
    } else {
      node.add(key, value);
      return null;
    }
  }

  public boolean remove(K key) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      node.asInternalNode().removeFromMap();
      return true;
    }
    return false;
  }

  public V replace(K key, V value) {
    Node node = findNodeByKey(key);
    if (node.isMappedBy(key)) {
      return node.replace(key, value);
    }
    return null;
  }

  public void replaceAll(BiFunction<? super K, ? super V, ? extends V> remappingFunc) {
    BiConsumer<LinkNode, LinkNode> replacer = (thisNode, thatNode) -> {
      K key = thisNode.child.key;
      V value = remappingFunc.apply(thatNode.child.key, thatNode.child.value);
      thisNode.replace(key, value);
    };

    this.root.applyToAll(this.root, replacer);
  }

  public int size() {
    return size;
  }

  private abstract class Node {
    public abstract V add(K key, V value);

    public abstract InternalNode asInternalNode();

    public abstract <E> void collectInOrder(List<E> list, Function<InternalNode, E> collector);

    public abstract int getRedBlackIndex();

    public abstract boolean isMappedBy(K key);

    public abstract Node next(K key);

    public V remap(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunc) {
      V value;
      if (this.isMappedBy(key)) {
        Entry<K, V> entry = this.asInternalNode().toEntry();
        value = remappingFunc.apply(entry.getKey(), entry.getValue());
        if (value == null) {
          this.asInternalNode().removeFromMap();
        }
      } else {
        value = remappingFunc.apply(key, null);
      }

      if (value != null) {
        this.add(key, value);
      }
      return value;
    }

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
    public final RedBlackBalancer balancer;
    private K key;
    private V value;
    private LinkNode parent;

    private InternalNode(K key, V value, LinkNode parent) {
      if (key == null) {
        throw new NullPointerException("Cannot put null mapping into TreeMap.");
      }

      this.left = new LinkNode(this);
      this.right = new LinkNode(this);
      this.balancer = new RedBlackBalancer(this);
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
    public <E> void collectInOrder(List<E> list, Function<InternalNode, E> collector) {
      left.collectInOrder(list, collector);
      list.add(collector.apply(this));
      right.collectInOrder(list, collector);
    }

    public K getKey() {
      return key;
    }

    @Override
    public int getRedBlackIndex() {
      assert (!this.balancer.hasParent()
          || !(this.balancer.getParent().getColour() == NodeColour.RED
          && this.balancer.getColour() == NodeColour.RED));

      int left = this.left.getRedBlackIndex();
      int right = this.right.getRedBlackIndex();

      assert (!this.left.hasChild() || this.left.child.key.compareTo(this.key) < 0);
      assert (!this.right.hasChild() || this.right.child.key.compareTo(this.key) > 0);
      assert (this.balancer.getColour().isValid());
      assert (left == right);

      return this.balancer.getColour() == NodeColour.RED ? left : right + 1;
    }

    public V getValue() {
      return value;
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
      InternalNode predecessor;
      LinkNode successor;

      if (this.right.hasChild()) {
        InternalNode node = right.searchFor(key).asInternalNode();
        this.key = node.key;
        this.value = node.value;
        predecessor = node;
        successor = predecessor.right;
      } else {
        predecessor = this;
        successor = predecessor.left;
      }

      predecessor.balancer.balanceBeforeRemoval(successor);
      predecessor.parent.linkWith(successor);
      assertThatTreeIsCorrectlyStructured();
      size--;
    }

    private Entry<K, V> toEntry() {
      return new Entry<>(key, value);
    }
  }

  private class LinkNode extends Node {
    private final InternalNode owner;
    private InternalNode child;

    private LinkNode(InternalNode owner) {
      this.owner = owner;
      this.child = null;
    }

    @Override
    public V add(K key, V value) {
      assert (!this.hasChild());
      return this.replace(key, value);
    }

    @Override
    public InternalNode asInternalNode() {
      return null;
    }

    public void applyToAll(LinkNode node, BiConsumer<LinkNode, LinkNode> consumer) {
      if (node.hasChild()) {
        consumer.accept(this, node);
        this.child.left.applyToAll(node.child.left, consumer);
        this.child.right.applyToAll(node.child.right, consumer);
      }
    }

    @Override
    public <E> void collectInOrder(List<E> list, Function<InternalNode, E> collector) {
      if (this.hasChild()) {
        child.collectInOrder(list, collector);
      }
    }

    public boolean hasChild() {
      return child != null;
    }

    @Override
    public int getRedBlackIndex() {
      if (!this.hasChild()) {
        return 0;
      } else {
        return this.child.getRedBlackIndex();
      }
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
      if (!this.hasChild()) {
        child = new InternalNode(key, value, this);
        size++;
        child.balancer.balanceAfterInsertion();
        assertThatTreeIsCorrectlyStructured();
        return null;
      }
      return child.replace(key, value);
    }

    public void setChild(InternalNode child) {
      this.child = child;
      if (this.hasChild()) {
        child.parent = this;
      }
    }

    public void linkWith(LinkNode link) {
      this.setChild(link.child);
      link.child = null;
    }
  }

  private class RedBlackBalancer {
    private final InternalNode node;
    private NodeColour colour;

    private RedBlackBalancer(InternalNode node) {
      this.node = node;
      this.colour = NodeColour.RED;
    }

    public void balanceAfterInsertion() {
      if (!this.hasParent()) {
        this.setColour(NodeColour.BLACK);
      } else if (this.getParent().getColour() == NodeColour.BLACK) {
        /* Do nothing... */
      } else if (this.hasUncle() && this.getUncle().getColour() == NodeColour.RED) {
        this.getParent().setColour(NodeColour.BLACK);
        this.getUncle().setColour(NodeColour.BLACK);
        this.getGrandparent().setColour(NodeColour.RED);
        this.getGrandparent().balanceAfterInsertion();
      } else if (this.getParent().isLeftChild() && this.isRightChild()) {
        RedBlackBalancer parent = this.getParent();
        parent.rotateLeft();
        parent.rebalanceAfterInsertion();
      } else if (this.getParent().isRightChild() && this.isLeftChild()) {
        RedBlackBalancer parent = this.getParent();
        parent.rotateRight();
        parent.rebalanceAfterInsertion();
      } else {
        this.rebalanceAfterInsertion();
      }
    }

    public void balanceBeforeRemoval(LinkNode successor) {
      RedBlackBalancer predecessor = this;
      if (predecessor.getColour() == NodeColour.BLACK) {
        boolean success = setupSuccessorForRemoval(successor);
        if (!success) {
          predecessor.rebalanceBeforeRemoval();
        }
      }
    }

    private void flipLeft() {
      NodeColour colour = this.getColour();
      RedBlackBalancer right = this.getRightChild();
      this.setColour(right.getColour());
      right.setColour(colour);
      this.rotateLeft();
    }

    private void flipRight() {
      NodeColour colour = this.getColour();
      RedBlackBalancer left = this.getLeftChild();
      this.setColour(left.getColour());
      left.setColour(colour);
      this.rotateRight();
    }

    private boolean hasLeftChild() {
      return this.node.left.hasChild();
    }

    private boolean hasParent() {
      return this.node.parent.owner != null;
    }

    private boolean hasRightChild() {
      return this.node.right.hasChild();
    }

    private boolean hasUncle() {
      InternalNode grandparent = this.getGrandparent().node;
      return grandparent.left.hasChild() && grandparent.right.hasChild();
    }

    private boolean isLeftChild() {
      return this.getParent().hasLeftChild() && this.getParent().getLeftChild() == this;
    }

    private boolean isRightChild() {
      return this.getParent().hasRightChild() && this.getParent().getRightChild() == this;
    }

    private NodeColour getColour() {
      return this.colour;
    }

    private RedBlackBalancer getLeftChild() {
      assert (this.hasLeftChild());
      return this.node.left.child.balancer;
    }

    private NodeColour getLeftColour() {
      if (this.hasLeftChild()) {
        return this.getLeftChild().getColour();
      } else {
        return NodeColour.BLACK;
      }
    }

    private RedBlackBalancer getGrandparent() {
      return this.getParent().getParent();
    }

    private RedBlackBalancer getParent() {
      assert (this.hasParent());
      return this.node.parent.owner.balancer;
    }

    private RedBlackBalancer getRightChild() {
      assert (this.hasRightChild());
      return this.node.right.child.balancer;
    }

    private NodeColour getRightColour() {
      if (this.hasRightChild()) {
        return this.getRightChild().getColour();
      } else {
        return NodeColour.BLACK;
      }
    }

    private RedBlackBalancer getSibling() {
      if (this.isLeftChild()) {
        return this.getParent().getRightChild();
      } else if (this.isRightChild()) {
        return this.getParent().getLeftChild();
      }
      return null;
    }

    private RedBlackBalancer getUncle() {
      return this.getParent().getSibling();
    }

    private void rebalanceBeforeRemoval() {
      if (this.hasParent()) {
        RedBlackBalancer sibling = this.getSibling();
        RedBlackBalancer parent = this.getParent();

        if (sibling.getColour() == NodeColour.RED) {
          if (this.isLeftChild()) {
            parent.flipLeft();
          } else {
            parent.flipRight();
          }
        }

        sibling = this.getSibling();
        parent = this.getParent();

        if (sibling.getColour() == NodeColour.BLACK && sibling.getLeftColour() == NodeColour.BLACK
            && sibling.getRightColour() == NodeColour.BLACK) {
          if (parent.getColour() == NodeColour.BLACK) {
            sibling.setColour(NodeColour.RED);
            parent.rebalanceBeforeRemoval();
          } else {
            sibling.setColour(NodeColour.RED);
            parent.setColour(NodeColour.BLACK);
          }
        } else {
          if (sibling.getColour() == NodeColour.BLACK) {
            if (this.isLeftChild() && sibling.getRightColour() == NodeColour.BLACK
                && sibling.getLeftColour() == NodeColour.RED) {
              sibling.flipRight();
            } else if (this.isRightChild() && sibling.getLeftColour() == NodeColour.BLACK
                && sibling.getRightColour() == NodeColour.RED) {
              sibling.flipLeft();
            }
          }

          sibling = this.getSibling();
          parent = this.getParent();

          sibling.setColour(parent.getColour());
          parent.setColour(NodeColour.BLACK);
          if (this.isLeftChild()) {
            sibling.getRightChild().setColour(NodeColour.BLACK);
            parent.rotateLeft();
          } else {
            sibling.getLeftChild().setColour(NodeColour.BLACK);
            parent.rotateRight();
          }
        }
      }
    }

    private void rebalanceAfterInsertion() {
      boolean isLeft = this.isLeftChild();
      boolean isRight = this.isRightChild();
      if (isLeft || isRight) {
        this.getParent().setColour(NodeColour.BLACK);
        RedBlackBalancer grandparent = this.getGrandparent();
        grandparent.setColour(NodeColour.RED);
        if (isLeft) {
          grandparent.rotateRight();
        } else {
          grandparent.rotateLeft();
        }
      }
    }

    private void reparent(RedBlackBalancer balancer) {
      balancer.node.parent.linkWith(this.node.parent);
    }

    private void rotateLeft() {
      RedBlackBalancer right = this.getRightChild();
      right.reparent(this);
      this.setRightChild(right.node.left);
      right.setLeftChild(this.node);
    }

    private void rotateRight() {
      RedBlackBalancer left = this.getLeftChild();
      left.reparent(this);
      this.setLeftChild(left.node.right);
      left.setRightChild(this.node);
    }

    private void setColour(NodeColour colour) {
      this.colour = colour;
    }

    private void setLeftChild(InternalNode node) {
      this.node.left.setChild(node);
    }

    private void setLeftChild(LinkNode link) {
      this.node.left.linkWith(link);
    }

    private void setRightChild(InternalNode node) {
      this.node.right.setChild(node);
    }

    private void setRightChild(LinkNode link) {
      this.node.right.linkWith(link);
    }

    private boolean setupSuccessorForRemoval(LinkNode link) {
      if (link.hasChild()) {
        RedBlackBalancer successor = link.child.balancer;
        assert (successor.getColour() == NodeColour.RED);
        successor.setColour(NodeColour.BLACK);
        return true;
      }
      return false;
    }
  }

  private enum NodeColour {
    RED,
    BLACK;

    public boolean isValid() {
      return this == RED || this == BLACK;
    }
  }

  public class Entry<K, V> {
    private final K key;
    private final V value;

    private Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if (obj instanceof Entry) {
        Entry that = (Entry) obj;
        return this.key.equals(that.key) && this.value.equals(that.value);
      } else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return Objects.hash(key, value);
    }
  }
}
