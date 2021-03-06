package ru.leadpogrommer.oop.heapsort;

/**
 * Class that contains method to sort arrays using heapsort algorithm
 */
public class HeapSorter {
    private static void heapify(int[] arr, int i, int heap_size) {
        int j = i;
        int largest = j;
        do {
            j = largest;
            int left = 2 * j + 1;
            int right = 2 * j + 2;
            if (left < heap_size && arr[left] > arr[largest]) {
                largest = left;
            }
            if (right < heap_size && arr[right] > arr[largest]) {
                largest = right;
            }
            if (largest != j) {
                swap(arr, j, largest);
            }
        } while (largest != j);
    }

    /**
     * This method sorts arr in-place using heapsort algorithm
     *
     * @param arr Array to be sorted
     */
    public static void sort(int[] arr) {
        if (arr == null) throw new IllegalArgumentException();
        for (int i = (arr.length - 2) / 2; i >= 0; i--) heapify(arr, i, arr.length);
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, i, 0);
            heapify(arr, 0, i);
        }
    }

    /**
     * Helper method to sort array elements
     *
     * @param a Input array
     * @param i First index
     * @param j Second index
     */
    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

}
