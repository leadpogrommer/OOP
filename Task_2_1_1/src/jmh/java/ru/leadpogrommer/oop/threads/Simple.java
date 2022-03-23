package ru.leadpogrommer.oop.threads;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 2)
@Fork(1)
public class Simple {


    @org.openjdk.jmh.annotations.Benchmark
    public void simple(Blackhole b){
        b.consume(new SimplePrimeChecker().allPrimes(Dataset.morePrimes(5)));
    }


    @org.openjdk.jmh.annotations.Benchmark
    public void streamed(Blackhole b){
        b.consume(new StreamPrimeChecker().allPrimes(Dataset.morePrimes(5)));
    }
}
