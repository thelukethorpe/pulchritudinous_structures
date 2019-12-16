package pulchritudinous.structures;

public class LinkedList<E> {

  private int size;

  public LinkedList() {
    this.size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void add(E item) {
    this.size++;
  }
}
