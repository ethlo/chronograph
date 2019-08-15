# Chronograph

[![Maven Central](https://img.shields.io/maven-central/v/com.ethlo.time/chronograph.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ethlo.time%22%20a%3A%22chronograph%22)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](LICENSE)
[![Coverage Status](https://coveralls.io/repos/github/ethlo/chronograph/badge.svg?branch=master&kill_cache=2)](https://coveralls.io/github/ethlo/chronograph?branch=master)
[![Build Status](https://travis-ci.org/ethlo/chronograph.svg?branch=master)](https://travis-ci.org/ethlo/chronograph)

Easy to use Java Chronograph (stopwatch) allowing measurement of elapsed time for tasks.

## Features
* Support for showing accumulated timings for tasks
* ASCII table support for detailed result output
* No dependencies

## Getting started
```java

        final Chronograph chronograph = Chronograph.create();
        final String taskName1 = "foo"
        final String taskName2 = "bar";
        final String taskName3 = "baz baz baz baz baz baz";

        for (int i = 1; i <= 100_000; i++)
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

        logger.info(chronograph.prettyPrint());

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
