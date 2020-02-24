package com.github.blalasaadri;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.text.DecimalFormat;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Heavily inspired by <a href="https://www.retit.de/continuous-benchmarking-with-jmh-and-junit/">Continuous Benchmarking with JMH and JUnit</a>.
 */
class MyBenchmarkTest {

    private static DecimalFormat df = new DecimalFormat("0.000");
    private static final double REFERENCE_SCORE = 37.132;

    @Test
    public void runJmhBenchmark() throws RunnerException {
        // tag::test_option_definitions[]
        Options opt = new OptionsBuilder()
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(3)
                .forks(3)
                .include(MyBenchmark.class.getSimpleName())
                .build();
        // end::test_option_definitions[]
        Collection<RunResult> runResults = new Runner(opt).run();
        assertFalse(runResults.isEmpty());

//        assertThat(runResults)
//                .allSatisfy(runResult -> assertDeviationWithin(runResult, REFERENCE_SCORE, 0.05));
    }

//    private static void assertDeviationWithin(RunResult result, double referenceScore, double maxDeviation) {
//        double score = result.getPrimaryResult().getScore();
//        double deviation = Math.abs(score / referenceScore - 1);
//        String deviationString = df.format(deviation * 100) + "%";
//        String maxDeviationString = df.format(maxDeviation * 100) + "%";
//        String errorMessage = "Deviation " + deviationString + " exceeds maximum allowed deviation " + maxDeviationString;
//        assertThat(deviation)
//                .withFailMessage(errorMessage)
//                .isLessThan(maxDeviation);
//    }

}
