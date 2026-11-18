# Chronograph

[![Maven Central](https://img.shields.io/maven-central/v/com.ethlo.time/chronograph)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.ethlo.time%22%20a%3A%22chronograph%22)
[![javadoc](https://javadoc.io/badge2/com.ethlo.time/chronograph/javadoc.svg)](https://javadoc.io/doc/com.ethlo.time/chronograph/latest/com.ethlo.chronograph/com/ethlo/chronograph/Chronograph.html)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/0d9d2c9bfddc400f84203aa82a55f211)](https://app.codacy.com/gh/ethlo/chronograph/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![License](https://img.shields.io/github/license/ethlo/chronograph)](LICENSE)

Easy to use Java stopwatch allowing measurement of elapsed time.

## Features
  * New in 4.0: Hierarchical task tracking - Start several nested tasks.
  * The same task can be timed multiple times for aggregated/cumulative data.
  * Supports numerous metrics:
     - average
     - median
     - min/max
     - standard deviation
     - percentiles
     - percentage
     - count
     - total time
  * Human-readable durations
  * [Well documented](https://javadoc.io/doc/com.ethlo.time/chronograph/latest/com.ethlo.chronograph/com/ethlo/chronograph/Chronograph.html)
  * Highly tweaked code for minimal overhead.
  * Dynamic ASCII/Unicode table support for detailed result output on the console or in logs with support for colored output (ANSI console)
  * Easy to fetch the underlying data for when you need your own output format, like JSON
  * Configurable sample interval for very short running tasks
  * No dependencies

## Sample usage

#### hierarchicalTasks <sup>[source](src/test/java/samples/usage/UsageSamples.java#L36-L63)</sup>

Tasks can be nested. Time spent in a task that is not covered by its subtasks is reported as
`<overhead>`.

```java
final Chronograph c = Chronograph.create();
c.time("Request", () ->
{
    for (int i = 0; i < 13; i++)
    {
        c.time("Fetch", () -> busy(12));
    }

    // Overhead
    busy(300);

    c.time("De-serialize", () ->
    {
        // Overhead
        busy(14);
        c.time("JSON de-serialize", () -> busy(44));
    });
});

System.out.println(c);
```

#### startAndStop <sup>[source](src/test/java/samples/usage/UsageSamples.java#L65-L78)</sup>

Start and stop a task explicitly. Printing the chronograph renders the results as a table.

```java
final Chronograph chronograph = Chronograph.create();
chronograph.start("my-task");
// perform a task
chronograph.stop();
System.out.println(chronograph);

assertEquals(1, chronograph.getTask("my-task").getInvocations());
```

#### timeLambda <sup>[source](src/test/java/samples/usage/UsageSamples.java#L80-L94)</sup>

Time a lambda. The overloads of `time` cover the functional interfaces in `java.util.function`, so
a return value is passed through.

```java
final List<Long> myList = new ArrayList<>(Arrays.asList(5L, 3L, 9L, 1L));
final Chronograph chronograph = Chronograph.create();
chronograph.time("List sort", () -> myList.sort(Comparator.naturalOrder()));
final Long max = chronograph.time("Find max", () -> myList.get(myList.size() - 1));
System.out.println(chronograph);

assertEquals(9L, max);
```



Sample output in tables:
![Table output](doc/default2.png "Table output")

Or in JSON format if you need it for later analysis:

```json
{
  "tasks" : [ {
    "name" : "Request",
    "subtasks" : [ {
      "name" : "Fetch",
      "total_time" : "0.15757311",
      "subtasks_time" : "0",
      "self_time" : "0.15757311",
      "invocation_count" : 13,
      "statistics" : {
        "average" : "0.012121008",
        "median" : "0.012110195",
        "min" : "0.01208667",
        "max" : "0.012215087",
        "standard_deviation" : "0.000038184",
        "percentiles" : {
          "75.0" : "0.012130908",
          "90.0" : "0.012183143",
          "95.0" : "0.012215087",
          "99.0" : "0.012215087",
          "99.9" : "0.012215087"
        }
      }
    }, {
      "name" : "De-serialize",
      "subtasks" : [ {
        "name" : "JSON de-serialize",
        "total_time" : "0.044214251",
        "subtasks_time" : "0",
        "self_time" : "0.044214251",
        "invocation_count" : 1
      }, {
        "name" : "<overhead>",
        "total_time" : "0.015809396",
        "subtasks_time" : "0",
        "self_time" : "0.015809396",
        "invocation_count" : 1
      } ],
      "total_time" : "0.060023647",
      "subtasks_time" : "0.060023647",
      "self_time" : "0",
      "invocation_count" : 1
    }, {
      "name" : "<overhead>",
      "total_time" : "0.301883674",
      "subtasks_time" : "0",
      "self_time" : "0.301883674",
      "invocation_count" : 1
    } ],
    "total_time" : "0.519480431",
    "subtasks_time" : "0.519480431",
    "self_time" : "0",
    "invocation_count" : 1
  }, {
    "name" : "Response",
    "subtasks" : [ {
      "name" : "Logging",
      "total_time" : "0.011222603",
      "subtasks_time" : "0",
      "self_time" : "0.011222603",
      "invocation_count" : 1
    }, {
      "name" : "Serialize",
      "subtasks" : [ {
        "name" : "JSON serialize",
        "total_time" : "0.027233243",
        "subtasks_time" : "0",
        "self_time" : "0.027233243",
        "invocation_count" : 1
      }, {
        "name" : "<overhead>",
        "total_time" : "0.014704076",
        "subtasks_time" : "0",
        "self_time" : "0.014704076",
        "invocation_count" : 1
      } ],
      "total_time" : "0.041937319",
      "subtasks_time" : "0.041937319",
      "self_time" : "0",
      "invocation_count" : 1
    } ],
    "total_time" : "0.053987607",
    "subtasks_time" : "0.053159922",
    "self_time" : "0.000827685",
    "invocation_count" : 1
  } ]
}
```

## Getting started

### Include in your project

#### Maven coordinates
```xml
<dependency>
  <groupId>com.ethlo.time</groupId>
  <artifactId>chronograph</artifactId>
  <version>5.0.1</version>
</dependency>
```

### Choice of output columns
Empty columns will be dropped automatically. Included columns can be configured.

#### outputConfigFromScratch <sup>[source](src/test/java/samples/output/OutputSamples.java#L35-L46)</sup>

Begin from scratch and enable only the columns you want.

```java
final OutputConfig outputConfig = new OutputConfig()
        .median(true)
        .standardDeviation(true);

assertTrue(outputConfig.median());
```

#### outputConfigFromDefault <sup>[source](src/test/java/samples/output/OutputSamples.java#L48-L59)</sup>

Begin from one of the predefined configurations, `DEFAULT`, `COMPACT` or `EXTENDED`, and adjust it. Each
call returns a new configuration.

```java
final OutputConfig outputConfig = OutputConfig.DEFAULT
        .percentiles(75, 90, 99, 99.9, 99.99);

assertEquals(5, outputConfig.percentiles().length);
```

#### formatAsTable <sup>[source](src/test/java/samples/output/OutputSamples.java#L61-L76)</sup>

Render the results as a table with a theme from `TableThemes` and your output configuration.

```java
final Chronograph chronograph = Chronograph.create();
chronograph.time("my-task", () -> Math.sqrt(42));

final OutputConfig outputConfig = OutputConfig.EXTENDED;
final String table = new TableOutputFormatter(TableThemes.RED_HERRING, outputConfig)
        .format(chronograph.getTaskData());
System.out.println(table);

assertTrue(table.contains("my-task"));
```


### Reduced sample interval
If you run very quick tasks (typically less than milliseconds) in a loop, it may be beneficial to not capture every iteration, but instead sample some of them.

#### minInterval <sup>[source](src/test/java/samples/sampling/SamplingSamples.java#L34-L51)</sup>

Only capture an invocation when at least `minInterval` has passed since the last captured one. All
invocations are still counted.

```java
final Chronograph chronograph = Chronograph
        .create(CaptureConfig
                .minInterval(Duration.ofNanos(10_000))); // 10 microseconds

for (int i = 0; i < 100_000; i++)
{
    chronograph.time("quick-task", () -> Math.sqrt(42));
}

assertEquals(100_000, chronograph.getTask("quick-task").getInvocations());
```


### Themes

You can choose to output the results using different styles and colors. Below are a few examples.

![Themes](doc/limited.png "Theme sample")
![Themes](doc/default1.png "Theme sample")
![Themes](doc/default2.png "Theme sample")
![Themes](doc/default3.png "Theme sample")

#### customTheme <sup>[source](src/test/java/samples/themes/ThemeSamples.java#L36-L55)</sup>

Build a theme from scratch.

```java
final TableTheme myTheme = TableTheme.builder()
        .stringColor(AnsiColor.BLUE)
        .numericColor(AnsiColor.GREEN)
        .horizontalSeparator(" ")
        .verticalSpacerColor(AnsiColor.GRAY)
        .horizontalSpacerColor(AnsiColor.GRAY)
        .build();

final Chronograph chronograph = Chronograph.create();
chronograph.time("my-task", () -> Math.sqrt(42));
System.out.println(new TableOutputFormatter(myTheme, OutputConfig.DEFAULT).format(chronograph.getTaskData()));

assertEquals(AnsiColor.BLUE, myTheme.getStringColor());
```

#### adjustTheme <sup>[source](src/test/java/samples/themes/ThemeSamples.java#L57-L68)</sup>

Or start from a predefined theme and change what you need.

```java
final TableTheme myTheme = TableThemes.ROUNDED.begin()
        .stringColor(AnsiColor.YELLOW)
        .build();

assertEquals(AnsiColor.YELLOW, myTheme.getStringColor());
```


## Limitations

### Time resolution
This project is utilizing `System.nanoTime()` which has some inherent issues with very quick task times. It does have a nanosecond resolution, but not a nanosecond precision. These are still usually orders of magnitude away from what you are trying to measure, so it is not a problem. If you are micro-benchmarking, consider using a framework like [JMH](https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core).

If you would like to know more:
  * [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime()](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#nanoTime())
  * [https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers](https://shipilev.net/blog/2014/nanotrusting-nanotime/#_timers)

### Thread safety
The Chronograph is NOT thread safe.
