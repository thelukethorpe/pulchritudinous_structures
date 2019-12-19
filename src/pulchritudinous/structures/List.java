package pulchritudinous.structures;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

interface List<E> extends Iterable<E> {

  void add(E item);

  void addAll(List<E> items);

  void addFirst(E item);

  void addLast(E item);

  void clear();

  List<E> clone();

  boolean contains(E item);

  E first();

  E get(int index);

  int indexOf(E item);

  void insertAt(E item, int index);

  boolean isEmpty();

  E last();

  E poll();

  List<E> pollMany(int n);

  boolean remove(E item);

  void removeAll(E item);

  void removeAt(int index);

  void replaceAll(UnaryOperator<E> operator);

  E set(E item, int index);

  int size();

  void sort(BiFunction<E, E, Integer> comparator);

  Object[] toArray();
}
