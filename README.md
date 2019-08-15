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

final Chronograph chrono = Chronograph.create();
chrono.start("my-first-task");
performSomeTask();
chrono.stop();
System.out.println(chrono.prettyPrint());

``` 

Example output:
```javascript
--------------------------------------------------------------------------------
&#124; Task                  &#124; Average      &#124; Total        &#124; Invocations   &#124; %      &#124;    
--------------------------------------------------------------------------------
&#124; my-first-task         &#124;      888.84μ &#124;       9.288s &#124;        10,449 &#124;  100%  &#124;
```