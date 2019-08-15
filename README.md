# Chronograph

[![Maven Central](https://img.shields.io/maven-central/v/com.ethlo.time/chronograph.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ethlo.time%22%20a%3A%22chronograph%22)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](LICENSE)
[![Coverage Status](https://coveralls.io/repos/github/ethlo/chronograph/badge.svg?branch=master&kill_cache=3)](https://coveralls.io/github/ethlo/chronograph?branch=master)
[![Build Status](https://travis-ci.org/ethlo/chronograph.svg?branch=master)](https://travis-ci.org/ethlo/chronograph)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0d9d2c9bfddc400f84203aa82a55f211)](https://www.codacy.com/app/morten/chronograph?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ethlo/chronograph&amp;utm_campaign=Badge_Grade)

Easy to use Java Chronograph (stopwatch) allowing measurement of elapsed time for tasks.

## Features
* Support for showing accumulated and average timings for one or more tasks
* "ASCII table" support for detailed result output on the console or in a log file (80 characters wide by default)
* No dependencies
* High test coverage

## Getting started
```java
final Chronograph chronograph = Chronograph.create();

final String taskName1 = "foo"
final String taskName2 = "bar bar";
final String taskName3 = "baz baz baz baz baz baz";

for (int i = 0; i < 100_000; i++)
{
    chronograph.timed("foo", this::microsecondTask);
    chronograph.timed("bar", this::microsecondTask);
    chronograph.timed("baz baz baz baz baz baz", this::microsecondTask);
}

System.out.println(chronograph.prettyPrint());
``` 

Output:
```
--------------------------------------------------------------------------------
| Task                  | Average      | Total        | Invocations   | %      |    
--------------------------------------------------------------------------------
| a long task name      |       1.18 μ |    118.06 ms |       100,000 |  33.4% |
| bar ba                |       1.18 μ |    117.64 ms |       100,000 |  33.2% |
| baz baz baz baz baz b |       1.18 μ |    118.28 ms |       100,000 |  33.4% |
--------------------------------------------------------------------------------
| Total: 353.98m                                                               |
--------------------------------------------------------------------------------
```

## Example outputs
```
--------------------------------------------------------------------------------
| Task                  | Average      | Total        | Invocations   | %      |    
--------------------------------------------------------------------------------
| lookup                |      188.88μ |      37.048s |       196,143 |  79.7% |
| transform             |       32.62μ |       6.398s |       196,143 |  13.8% |
| sort                  |      306.97m |      306.97m |             1 |   0.7% |
| xml                   |       16.68m |       16.68m |             1 |   0.0% |
| serializing           |       1.677s |       1.677s |             1 |   3.6% |
| gzip-compressing      |       1.043s |       1.043s |             1 |   2.2% |
--------------------------------------------------------------------------------
| Total: 46.489s                                                               |
--------------------------------------------------------------------------------
```

## Limitations
This project is utilizing `System.nanoTime()` which has some inherent issues with very quick task times. It does have a nanosecond resolution, but not a nanosecond precision. These are still usually orders of magnitude away from what you are trying to measure, so it is not a problem. If you are micro-benchmarking, consider using a framework like [JMH](https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core)

If you would like to know more:
* https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime()
* https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers
