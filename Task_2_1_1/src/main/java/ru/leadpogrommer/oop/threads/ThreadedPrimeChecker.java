package ru.leadpogrommer.oop.threads;

public class ThreadedPrimeChecker extends PrimeChecker {
    final private Thread[] pool;

    ThreadedPrimeChecker(int n){
        pool = new Thread[n];
    }



    @Override
    boolean allPrimes(int[] array) {
        var state = new Object(){
            int nextIndex = 0;
            boolean result = true;
        };

        for(int i = 0; i < pool.length; i++){
            pool[i] = new Thread(()->{
                while(true){
                    int ourIndex;
                    synchronized (state){
                        if(state.nextIndex == -1 || !state.result)break;
                        ourIndex = state.nextIndex++;
                        if(state.nextIndex == array.length)state.nextIndex = -1;
                    }
                    if(!isPrime(array[ourIndex]))state.result = false;

                }
            });
            pool[i].start();
        }
        for(var thread:pool){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return state.result;
    }




}
