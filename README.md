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
final String taskName2 = "bar";
final String taskName3 = "baz baz baz baz baz baz";

for (int i = 0; i < 100_000; i++)
{
    chronograph.start(taskName);
    microsecondTask();
    chronograph.stop(taskName);

    chronograph.start(taskName2);
    microsecondTask();
    chronograph.stop(taskName2);

    chronograph.start(taskName3);
    microsecondTask();
    chronograph.stop(taskName3);
}

System.out.println(chronograph.prettyPrint());
``` 

Example output:
```
--------------------------------------------------------------------------------
| Task                  | Average      | Total        | Invocations   | %      |    
--------------------------------------------------------------------------------
| a long task name      |        1.18μ |      118.06m |       100,000 |  33.4% |
| bar                   |        1.18μ |      117.64m |       100,000 |  33.2% |
| baz baz baz baz baz b |        1.18μ |      118.28m |       100,000 |  33.4% |
--------------------------------------------------------------------------------
| Total: 353.98m                                                               |
--------------------------------------------------------------------------------
```

## Limitations
This project is utilizing `System.nanoTime()` which has some inherent issues with very quick task times. It does have a nanosecond resolution, but not a nanosecond precision. These are still usually orders of magnitude away from what you are trying to measure, so it is not a problem. If you are micro-benchmarking, consider using a framework like [JMH](https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core)

If you would like to know more:
* https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime()
* https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers
