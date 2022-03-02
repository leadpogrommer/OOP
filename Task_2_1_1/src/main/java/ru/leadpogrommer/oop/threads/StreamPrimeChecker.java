package ru.leadpogrommer.oop.threads;

import java.util.Arrays;

public class StreamPrimeChecker extends PrimeChecker{
    @Override
    boolean allPrimes(int[] array) {
        return Arrays.stream(array).parallel().allMatch(PrimeChecker::isPrime);
    }
}
