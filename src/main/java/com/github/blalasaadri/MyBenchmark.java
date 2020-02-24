/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.blalasaadri;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.blalasaadri.MyBenchmark.ListVariant.FIVE_NAMES;

@State(Scope.Benchmark)
@Measurement(
        iterations = 5,
        time = 10,
        timeUnit = TimeUnit.SECONDS
)
public class MyBenchmark {

    @Param({"FIVE_NAMES", "AUTO_GENERATED_NAMES"})
    public ListVariant listVariant;

    private String[] names;

    @Setup(Level.Iteration)
    public void setup() {
        switch (listVariant) {
            case FIVE_NAMES:
                // The original names
                names = new String[]{"Java", "Node", "JavaScript", "Rust", "Go"};
                break;
            case AUTO_GENERATED_NAMES:
                // tag::autogenerate_names[]
                // 1000 new names
                final String[] firstNames = {"Alice", "Bob", "Charles", "Dora", "Emanuel", "Fabienne", "George", "Hannelore", "Igor", "Janice"};
                final String[] middleNames = {"Kim", "Landry", "Maria", "Nikita", "Oakley", "Perry", "Quin", "Robin", "Skyler", "Taylen"};
                final String[] surnames = {"Underhill", "Vaccanti", "Wilson", "Xanders", "Yallopp", "Zabawa", "Anderson", "Bell", "Carter", "Diaz"};
                names = Arrays.stream(firstNames)
                        .flatMap(firstName -> Arrays.stream(middleNames).map(middleName -> firstName + " " + middleName))
                        .flatMap(firstAndMiddleName -> Arrays.stream(surnames).map(surname -> firstAndMiddleName + " " + surname))
                        .toArray(String[]::new);
                // end::autogenerate_names[]
                break;
        }
    }

    @Benchmark
    public void mapAndPrintWithStreams(Blackhole blackhole) {
        // tag::streams[]
        List<String> collect = IntStream.range(0, names.length)
                .mapToObj(index -> index + ": " + names[index])
                .collect(Collectors.toList());

        collect.forEach(blackhole::consume);
        // end::streams[]
    }

    @Benchmark
    public void mapAndPrintWithStreamsInOneGo(Blackhole blackhole) {
        // tag::streams_in_one_go[]
        IntStream.range(0, names.length)
                .mapToObj(index -> index + ": " + names[index])
                .forEach(blackhole::consume);
        // end::streams_in_one_go[]
    }

    @Benchmark
    public void printInEnhancedForLoop(Blackhole blackhole) {
        // tag::enhanced_for[]
        int i = 0;
        for (String name : names) {
            blackhole.consume(i++ + ": " + name);
        }
        // end::enhanced_for[]
    }

    @Benchmark
    public void printInOldSchoolForLoop(Blackhole blackhole) {
        // tag::old_school_for[]
        for (int i = 0; i < names.length; i++) {
            blackhole.consume(i + ": " + names[i]);
        }
        // end::old_school_for[]
    }

    public enum ListVariant {
        FIVE_NAMES, AUTO_GENERATED_NAMES
    }
}
