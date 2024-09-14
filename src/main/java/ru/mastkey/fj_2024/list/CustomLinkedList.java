package ru.mastkey.fj_2024.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CustomLinkedList<E> implements Iterable<E> {
    private final Node<E> head;
    private final Node<E> tail;
    private int size;

    public CustomLinkedList() {
        this.head = new Node<>(null, null, null);
        this.tail = new Node<>(null, null, null);
        this.size = 0;
        head.next = tail;
        tail.prev = head;
    }

    public void add(E e) {
        var prev = tail.prev;
        var node = new Node<>(prev, e, this.tail);
        prev.next = node;
        tail.prev = node;
        size++;
    }

    public int size() {
        return size;
    }

    public void remove(int index) {
        var nodeForDelete = getNodeByIndex(index);
        nodeForDelete.prev.next = nodeForDelete.next;
        nodeForDelete.next.prev = nodeForDelete.prev;
        size--;
    }

    public boolean contains(E e) {
        var temp = head.next;
            while (temp != tail) {
                if (Objects.equals(temp.item, e)) {
                    return true;
                }
                temp = temp.next;
            }
        return false;
    }

    public E get(int index) {
        return getNodeByIndex(index).item;
    }

    public void addAll(Collection<? extends E> collection) {
        for (E e : collection) {
            add(e);
        }
    }

    public void addAll(CustomLinkedList<? extends E> collection) {
        for (E e : collection) {
            add(e);
        }
    }

    private Node<E> getNodeByIndex(int index) {
        validateIndex(index);

        Node<E> temp;
        if (index > size / 2) {
            temp = tail;
            for (int i = size; i > index; i--) {
                temp = temp.prev;
            }
        } else {
            temp = head.next;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        }
        return temp;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomLinkedListIterator();
    }

    private class CustomLinkedListIterator implements Iterator<E> {
        private Node<E> current = head.next;

        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    private static class Node<E> {
        final E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Node<E> node = head.next; node != tail; node = node.next) {
            sb.append(node.item);
            if (node.next != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
