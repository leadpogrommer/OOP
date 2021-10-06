package ru.leadpogrommer.oop.stack;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class Stack<T> {
    private class Node{
        public T val;
        public Node prevNode;
    }

    Stack(){
        count = 0;
        topNode = new Node();
    }

    void push(T v){
        var newNode = new Node();
        newNode.val = v;
        newNode.prevNode = topNode;
        topNode = newNode;
        count++;
    }

    T pop(){
        if(count == 0)throw new EmptyStackException();
        var ret = topNode.val;
        topNode = topNode.prevNode;
        count--;
        return ret;
    }

    Stack<T> popStack(int c){
        if(c > count)throw new IllegalArgumentException();
        var ret = new Stack<T>();
        if(c == 0)return ret;
        ret.count = c;
        count -= c;
        Node n = topNode;
        for(int i = 1; i < c; i++)n = n.prevNode;
        var oldPrevNode = n.prevNode;
        n.prevNode = ret.topNode;
        ret.topNode = topNode;
        topNode = oldPrevNode;
        return ret;
    }


    void pushStack(Stack<T> source){
        Node n;
        if(source.getCount() == 0)return;
        count += source.count;
        for(n = source.topNode; n.prevNode.prevNode != null; n = n.prevNode);
        source.count = 0;
        var sourceGuardian = n.prevNode;
        n.prevNode = topNode;
        topNode = source.topNode;

        source.topNode = sourceGuardian;


    }


    public List<T> toList(){
        var ret = new ArrayList<T>();
        var n = topNode;
        while (n.prevNode != null){
            ret.add(0, n.val);
            n = n.prevNode;
        };
        return ret;
    }


    private Node topNode;


    private int count;

    public int getCount() {
        return count;
    }
}
