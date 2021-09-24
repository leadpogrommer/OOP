package ru.leadpogrommer.oop.heapsort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeapSorterTest {
    static ArrayList<int[]> arrayProvider() {
        Random rnd = new Random(2281337);
        var ret = new ArrayList<int[]>();

        for (int i = 0; i < 100; i++) {
            int array_length = rnd.nextInt(10) + 10;
            int[] arr = rnd.ints(array_length).toArray();
            ret.add(arr);
        }

        return ret;
    }

    @ParameterizedTest(name = "Test sort on random array #{index}")
    @MethodSource("arrayProvider")
    void sort_RandomArrays(int[] arr) {
        int[] arr_copy = Arrays.copyOf(arr, arr.length);

        HeapSorter.sort(arr);
        Arrays.sort(arr_copy);

        // We test our code with reference sort algorithm
        assertArrayEquals(arr_copy, arr);
    }

    @Test
    @DisplayName("Test sort on null value")
    void sort_Null() {
        assertThrows(IllegalArgumentException.class, () -> {
            HeapSorter.sort(null);
        });
    }

    @Test
    @DisplayName("Test sort on empty array")
    void sort_Empty() {
        var arr = new int[]{};
        HeapSorter.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }
}