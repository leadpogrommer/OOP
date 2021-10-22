package ru.leadpogrommer.oop.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EmptyStackException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StackTest {
    private static Stack<Integer> stackOfInts(int c) {
        var ret = new Stack<Integer>();
        for (int i = 0; i < c; i++) ret.push(i);
        return ret;
    }


    @Test
    void push() {
        var stack = stackOfInts(3);
        assertThat(stack.getSize()).isEqualTo(3);
        assertThat(stack.toList()).containsExactly(0, 1, 2);
    }

    @Test
    void pop() {
        var stack = stackOfInts(3);
        for (int i = 2; i >= 0; i--) {
            assertThat(stack.pop()).isEqualTo(i);
            assertThat(stack.getSize()).isEqualTo(i);
        }
        assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    void pushStack() {
        var stack = stackOfInts(4);
        var appendix = stackOfInts(3);
        stack.pushStack(appendix);
        assertThat(stack.toList()).containsExactly(0, 1, 2, 3, 0, 1, 2);
        assertThat(stack.getSize()).isEqualTo(7);
        assertThat(appendix.getSize()).isEqualTo(3);
        assertThat(appendix.toList()).containsExactly(0, 1, 2);
    }

    @Test
    void pushStack_empty() {
        var stack = stackOfInts(4);
        var appendix = new Stack<Integer>();
        stack.pushStack(appendix);
        assertThat(stack.toList()).containsExactly(0, 1, 2, 3);
        assertThat(stack.getSize()).isEqualTo(4);
    }

    @Test
    void popStack() {
        var stack = stackOfInts(6);
        var ns = stack.popStack(2);
        assertThat(ns.getSize()).isEqualTo(2);
        assertThat(ns.toList()).containsExactly(4, 5);

        assertThat(stack.toList()).containsExactly(0, 1, 2, 3);
        assertThat(stack.getSize()).isEqualTo(4);

        assertThrows(IllegalArgumentException.class, () -> stack.popStack(10));
    }

    @Test
    void iterate() {
        var stack = stackOfInts(6);
        var arr = new ArrayList<Integer>();

        for (var elem : stack) {
            arr.add(elem);
        }

        assertThat(arr).containsExactly(5, 4, 3, 2, 1, 0);
    }

}