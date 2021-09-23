/**
 * Class that contains method to sort arrays using heapsort algorithm
 */
public class HeapSorter {
    private static void heapify(int[] arr, int i, int heap_size){
        while (true){
            int left = 2*i + 1;
            int right = 2*i + 2;
            int largest = i;
            if(left < heap_size && arr[left] > arr[largest]){
                largest = left;
            }
            if(right < heap_size && arr[right] > arr[largest]){
                largest = right;
            }
            if(largest != i){
                int tmp = arr[largest];
                arr[largest] = arr[i];
                arr[i] = tmp;
                i = largest;
            } else{
                break;
            }
        }
    }

    /**
     * This method sorts arr in-place using heapsort algorithm
     * @param arr Array to be sorted
     */
    public static void sort(int[] arr){
        for(int i = arr.length - 1; i >= 0; i-- ) heapify(arr, i, arr.length);
        for(int i = arr.length - 1; i > 0; i-- ){
            int tmp = arr[0];
            arr[0] = arr[i];
            arr[i] = tmp;
            heapify(arr, 0, i);
        }
    }
}
