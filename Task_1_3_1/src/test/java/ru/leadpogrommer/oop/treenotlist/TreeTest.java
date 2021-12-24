package ru.leadpogrommer.oop.treenotlist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TreeTest {
    static Tree<Integer> getIntTree(Integer... numbers) {
        var ret = new Tree<Integer>();
        ret.addAll(Arrays.stream(numbers).toList());

        return ret;
    }


    static ArrayList<List<Integer>> arrayProvider() {
        Random rnd = new Random(2281337);
        var ret = new ArrayList<List<Integer>>();

        for (int i = 0; i < 100; i++) {
            int array_length = rnd.nextInt(10) + 10;
            var arr = rnd.ints(array_length, 0, 100).boxed().toList();
            ret.add(arr);
        }

        return ret;
    }

    @ParameterizedTest(name = "Test tree on random array #{index}")
    @MethodSource("arrayProvider")
    void randomArrays(List<Integer> arr) {
        var shuffled = new ArrayList<>(arr);
        Collections.shuffle(shuffled, new Random(2281337));

        var tree = new Tree<Integer>();
        tree.addAll(arr);

        while (true) {
            assertThat(tree.size()).isEqualTo(shuffled.size());
            assertThat(tree.isEmpty()).isEqualTo(shuffled.isEmpty());
            for (var elem : shuffled) {
                assertThat(tree.contains(elem)).isEqualTo(shuffled.contains(elem));
            }

            var sorted = new ArrayList<>(shuffled);
            Collections.sort(sorted);

            assertThat(tree.stream().toList()).containsExactlyElementsOf(sorted);

            assertThat(tree.containsAll(shuffled)).isTrue();

            assertThat(tree.toArray()).containsExactlyElementsOf(sorted);
            var test_arr = new Integer[0];
            assertThat(tree.toArray(test_arr)).containsExactlyElementsOf(sorted);

            int i = 0;
            for (var elem : tree) {
                assertThat(elem).isEqualTo(sorted.get(i));
                i++;
            }

            if (shuffled.isEmpty()) break;
            tree.remove(shuffled.get(0));
            shuffled.remove(0);
        }

    }

    @Test
    void addRemoveContains() {
        var tree = new Tree<Integer>();
        assertTrue(tree.isEmpty());
        tree.add(1);
        assertFalse(tree.isEmpty());
        assertTrue(tree.contains(1));
        assertFalse(tree.contains(2));
        tree.add(2);
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        tree.remove(1);
        assertFalse(tree.contains(1));
        assertTrue(tree.contains(2));
        tree.clear();
        assertFalse(tree.contains(1));
        assertFalse(tree.contains(2));

    }


    @Test
    void containsAll() {
        var tree = getIntTree(1, 4, 4, 2, 4, 8);
        assertTrue(tree.containsAll(Arrays.asList(1, 8)));
        assertFalse(tree.containsAll(Arrays.asList(1, 8, 10)));
        assertFalse(tree.remove(10));
    }

    @Test
    void exceptions() {
        var tree = getIntTree(2, 1, 3);
        assertThrows(NullPointerException.class, () -> tree.add(null));
        assertThrows(NullPointerException.class, () -> tree.remove(null));
        assertThrows(NullPointerException.class, () -> tree.contains(null));
        assertThrows(ClassCastException.class, () -> tree.remove("kcuf"));

        assertThrows(ConcurrentModificationException.class, () -> {
            for (var elem : tree) {
                tree.add(228);
            }
        });

    }

    @Test
    void stream(){
        var tree = new Tree<String>();
        tree.addAll(List.of("aaa", "bbbb", "sadnd", "aboba", "bs", "brainfuck", "nope"));
        var res = tree.stream().filter((s)->s.contains("b")).toList();
        assertThat(res).containsExactlyInAnyOrder("bbbb", "aboba", "bs", "brainfuck");
    }


}