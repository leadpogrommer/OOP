package ru.leadpogrommer.oop.threads;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 2)
@Fork(1)
@State(Scope.Benchmark)
public class Threaded {
    @Param({"1","2","3","4","5","6","7","8","9","10","11","12",})
    public int cores;


    @org.openjdk.jmh.annotations.Benchmark
    public void benchmark(Blackhole b){
        b.consume(new ThreadedPrimeChecker(cores).allPrimes(Dataset.morePrimes(5)));
    }

}
