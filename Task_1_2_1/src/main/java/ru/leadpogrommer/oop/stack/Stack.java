package ru.leadpogrommer.oop.stack;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Generic stack (FIFO collection)
 *
 * @param <T> Element type
 */
public class Stack<T> {
    private Node topNode;
    private int count;

    public Stack() {
        count = 0;
    }

    /**
     * Pushes new element onto stack
     *
     * @param v Element to push
     */
    public void push(T v) {
        var newNode = new Node();
        newNode.val = v;
        newNode.prevNode = topNode;
        topNode = newNode;
        count++;
    }

    /**
     * Remove top element from the stack
     *
     * @return Top element
     * @throws EmptyStackException if stack is empty
     */
    public T pop() throws EmptyStackException {
        if (count == 0) throw new EmptyStackException();
        var ret = topNode.val;
        topNode = topNode.prevNode;
        count--;
        return ret;
    }

    /**
     * Return c top elements of the stack as new stack (these elements are removed from stack)
     *
     * @param c Number of elements to pop
     * @return Popped stack
     * @throws IllegalArgumentException if c is bigger than size of the stack
     */
    public Stack<T> popStack(int c) throws IllegalArgumentException {
        if (c > count) throw new IllegalArgumentException();
        var ret = new Stack<T>();
        if (c == 0) return ret;
        ret.count = c;
        count -= c;
        Node n = topNode;
        for (int i = 1; i < c; i++) n = n.prevNode;
        var oldPrevNode = n.prevNode;
        n.prevNode = ret.topNode;
        ret.topNode = topNode;
        topNode = oldPrevNode;
        return ret;
    }

    /**
     * Pushes source onto stack. Source is not modified
     *
     * @param source Stack to push
     */
    public void pushStack(Stack<T> source) {
        if (source.getSize() == 0) return;
        count += source.count;

        var prevTopNode = topNode;
        var sourceNode = source.topNode;
        var destinationNode = new Node();
        topNode = destinationNode;
        while (sourceNode != null) {
            destinationNode.val = sourceNode.val;
            sourceNode = sourceNode.prevNode;
            if (sourceNode != null) {
                destinationNode.prevNode = new Node();
                destinationNode = destinationNode.prevNode;
            }
        }
        destinationNode.prevNode = prevTopNode;


    }

    /**
     * Converts stack to list
     *
     * @return list of stack elements
     */
    public List<T> toList() {
        var ret = new ArrayList<T>();
        var n = topNode;
        while (n != null) {
            ret.add(0, n.val);
            n = n.prevNode;
        }
        return ret;
    }

    /**
     * @return size of the stack
     */
    public int getSize() {
        return count;
    }

    private class Node {
        public T val;
        public Node prevNode;
    }
}
