import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeapSorterTest {

    @Test
    void sort() {
        //We need reproducible results, so we set constant seed
        Random rnd = new Random(2281337);

        // code is tested on 10 random arrays
        for (int i = 0; i < 10; i++){
            // length between 10 and 19
            int array_length = rnd.nextInt(10) + 10;

            // array to be sorted
            int [] arr = rnd.ints(array_length).toArray();

            int[] arr_copy = Arrays.copyOf(arr, array_length);

            HeapSorter.sort(arr);
            Arrays.sort(arr_copy);

            // We test our code with reference sort algorithm
            assertArrayEquals(arr, arr_copy);
        }

    }
}