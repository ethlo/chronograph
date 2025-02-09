# Chronograph

[![Maven Central](https://img.shields.io/maven-central/v/com.ethlo.time/chronograph)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ethlo.time%22%20a%3A%22chronograph%22)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](LICENSE)

Easy to use Java stopwatch allowing measurement of elapsed time.

## Features
  * New in 4.0: Hierarchical task tracking - Start several nested tasks.
  * The same task can be timed multiple times for aggregated/cumulative data.
  * Supports numerous metrics:
     - mean
     - median
     - min/max
     - max
     - standard deviation
     - percentiles
     - percentage
     - count
     - total time
  * Human-readable durations
  * Highly tweaked code for minimal overhead.
  * Dynamic ASCII/Unicode table support for detailed result output on the console or in logs with support for colored output (ANSI console)
  * Easy to fetch the underlying data for when you need your own output format, like JSON
  * Configurable sample interval for very short running tasks
  * No dependencies

## Getting started

### Include in your project

#### Maven coordinates
```xml
<dependency>
  <groupId>com.ethlo.time</groupId>
  <artifactId>chronograph</artifactId>
  <version>4.0.0</version>
</dependency>
``` 
### Usage 

#### Simple
```java
final Chronograph chronograph = Chronograph.create();
chronograph.start("my-task");
// perform a task
chronograph.stop();
System.out.printn(chronograph.prettyPrint());
```

#### Functional style with lamda

```java
final List<Long> myList = ...;
final Chronograph chronograph = Chronograph.create();     
chronograph.time("List sort", () -> linkedList.sort(Comparator.naturalOrder()));
System.out.println(chronograph);
```

### Choice of output columns
Empty columns will be dropped automatically. Included columns can be configured.

Begin from scratch:
```java
final OutputConfig outputConfig = new OutputConfig()
  .median(true)
  .standardDeviation(true);
``` 

Begin from DEFAULT configuration:
```java
final OutputConfig outputConfig = Chronograph.configure(OutputConfig.DEFAULT
  .percentiles(75, 90, 99, 99.9, 99.99));
```

```java
// Then use it like this

System.out.println(new TableOutputFormatter(outputConfig, TableTheme.RED_HERRING).format(chronograph.getData()));
```
### Reduced sample interval
If you run very quick tasks (typically less than milliseconds) in a loop, it may be beneficial to not capture every iteration, but instead sample some of them. This can be achieved by
```java
final Chronograph chronograph = Chronograph
    .create(CaptureConfig
        .minInterval(Duration.ofNanos(10_000))); // 10 microseconds
```

### Themes

You can choose to output the results using different styles and colors. Below are a few examples.

![Themes](doc/themes.png "Themes")

#### Make your own
```java
final TableTheme myTheme = TableTheme.builder()
    .stringColor(AnsiColor.BLUE)
    .numericColor(AnsiColor.GREEN)
    .horizontalSeparator(" ")
    .verticalSpacerColor(AnsiColor.GRAY)
    .horizontalSpacerColor(AnsiColor.GRAY)
    .build();
```

## Limitations

### Time resolution
This project is utilizing `System.nanoTime()` which has some inherent issues with very quick task times. It does have a nanosecond resolution, but not a nanosecond precision. These are still usually orders of magnitude away from what you are trying to measure, so it is not a problem. If you are micro-benchmarking, consider using a framework like [JMH](https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core).

If you would like to know more:
  * [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime()](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime())
  * [https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers](https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers)

### Thread safety
The Chronograph is NOT thread safe.
