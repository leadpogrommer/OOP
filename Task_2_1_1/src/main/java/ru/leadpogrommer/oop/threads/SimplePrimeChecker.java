package ru.leadpogrommer.oop.threads;

class SimplePrimeChecker extends PrimeChecker{

    @Override
    boolean allPrimes(int[] array) {
        for(var i: array){
            if(!isPrime(i))return false;
        }
        return true;
    }
}