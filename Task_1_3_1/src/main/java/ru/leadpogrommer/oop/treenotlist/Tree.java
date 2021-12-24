package ru.leadpogrommer.oop.treenotlist;

import java.util.*;


public class Tree<T extends Comparable<T>> implements Collection<T> {
    private int size = 0;
    private Node root;
    private int modificationCount;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        // contains can throw ClassCastException
        var value = (T) o;

        return findNode(value) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator();
    }

    @Override
    public Object[] toArray() {
        var arr = new Object[size];
        int i = 0;
        for (var element : this) {
            arr[i++] = element;
        }
        return arr;
    }

    @Override
    public <T1> T1[] toArray(T1[] arr) {
        if (arr.length < size()) {
            arr = Arrays.copyOf(arr, size());
        }
        int i = 0;
        for (var element : this) {
            arr[i++] = (T1) element;
        }
        return arr;
    }

    @Override
    public boolean add(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node parent = null;
        Node current = root;
        while (current != null) {
            parent = current;
            if (value.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        var newNode = new Node(value);
        if (parent == null) {
            root = newNode;
        } else if (value.compareTo(parent.value) < 0) {
            newNode.parent = parent;
            parent.left = newNode;
        } else {
            newNode.parent = parent;
            parent.right = newNode;
        }

        size++;
        modificationCount++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }

        // remove can throw ClassCastException
        var value = (T) o;

        var node = findNode(value);
        if (node == null) {
            return false;
        }

        if (node.left == null) {
            shiftSubtree(node, node.right);
        } else if (node.right == null) {
            shiftSubtree(node, node.left);
        } else {
            var succ = successor(node);
            if (succ.parent != node) {
                shiftSubtree(succ, succ.right);
                succ.right = node.right;
                succ.right.parent = succ;
            }
            shiftSubtree(node, succ);
            succ.left = node.left;
            succ.left.parent = succ;
        }
        size--;
        modificationCount++;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        var ret = true;
        for (var e : collection) {
            ret &= contains(e);
        }
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        var ret = false;
        for (var e : collection) {
            ret |= add(e);
        }
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        var ret = false;
        for (var e : collection) {
            ret |= remove(e);
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        modificationCount++;
    }

    private Node findNode(T value) {
        var current = root;
        while (current != null && !value.equals(current.value)) {
            if (value.compareTo(current.value) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return current;
    }

    private void shiftSubtree(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node successor(Node x) {
        x = x.right;
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    class Node {
        public Node left;
        T value;
        Node right;
        Node parent;

        Node(T value) {
            this.value = value;
        }
    }

    class TreeIterator implements Iterator<T> {
        private final int expectedModificationCount = modificationCount;
        private Node next;

        TreeIterator() {
            next = root;
            if (next == null) {
                return;
            }
            while (next.left != null) {
                next = next.left;
            }
        }

        private void checkModification() {
            if (modificationCount != expectedModificationCount) throw new ConcurrentModificationException();
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return next != null;
        }

        @Override
        public T next() {
            checkModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node ret = next;

            if (next.right != null) {
                next = next.right;
                while (next.left != null) {
                    next = next.left;
                }
                return ret.value;
            }

            while (true) {
                if (next.parent == null) {
                    next = null;
                    return ret.value;
                }
                if (next.parent.left == next) {
                    next = next.parent;
                    return ret.value;
                }
                next = next.parent;
            }
        }
    }
}
