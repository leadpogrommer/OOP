package ru.leadpogrommer.oop.threads;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PrimeCheckerTest {
    void tesPrimeCheckerImpl(PrimeChecker checker){
        assertTrue(checker.allPrimes(Dataset.PRIMES));
        var newArr = Arrays.copyOf(Dataset.PRIMES, Dataset.PRIMES.length + 1);
        newArr[Dataset.PRIMES.length] = 4;
        assertFalse(checker.allPrimes(newArr));
    }


    @Test
    void simpleChecker(){
        tesPrimeCheckerImpl(new SimplePrimeChecker());
    }

    @Test
    void threadedChecker(){
        tesPrimeCheckerImpl(new ThreadedPrimeChecker(12));
    }

    @Test
    void streamChecker(){
        tesPrimeCheckerImpl(new StreamPrimeChecker());
    }

    @Test
    void isPrime() {
        assertTrue(PrimeChecker.isPrime(2));
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(19));

        assertFalse(PrimeChecker.isPrime(1));
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(21));

    }
}