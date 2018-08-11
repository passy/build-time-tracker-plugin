# ![Build Time Tracker](https://cdn.rawgit.com/passy/build-time-tracker-plugin/cc3bd9dcbda61ae7b699e4048c3f425525352d54/assets/logo.svg)

[![Build Status](https://travis-ci.org/passy/build-time-tracker-plugin.svg?branch=master)](https://travis-ci.org/passy/build-time-tracker-plugin)
![Maven Version](https://img.shields.io/maven-central/v/net.rdrei.android.buildtimetracker/gradle-plugin.svg?maxAge=2592000)
[![Stories in Ready](https://img.shields.io/waffle/label/passy/build-time-tracker-plugin/ready.svg)](http://waffle.io/passy/build-time-tracker-plugin)

How much time do you spend each day waiting for Gradle? Now you know!

## Features

* Sortable bar chart summaries
* CSV output
* Daily and total summary

## Screenshot

![Screenshot](assets/screenshot.png)

## Usage

Apply the plugin in your `build.gradle`. On Gradle >2.1 you can do this
using the Plugin DSL Syntax:

```groovy
plugins {
  id "net.rdrei.android.buildtimetracker" version "0.11.0"
}
```

Otherwise, use it as `classpath` dependency:

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath "net.rdrei.android.buildtimetracker:gradle-plugin:0.11.+"
  }
}

apply plugin: "build-time-tracker"
```

Configure the plugin:

```groovy
buildtimetracker {
  reporters {
    csv {
      output "build/times.csv"
      append true
      header false
    }

    summary {
      ordered false
      threshold 50
      barstyle "unicode"
    }

    csvSummary {
      csv "build/times.csv"
    }
  }
}
```

Using the `SNAPSHOT` release:

```groovy
buildscript {
  repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
  }

  dependencies {
    classpath "net.rdrei.android.buildtimetracker:gradle-plugin:0.12.0-SNAPSHOT"
  }
}

```

## Difference to `--profile`

You may wonder why you would want to use this plugin when gradle has
a built-in [build
profiler](https://docs.gradle.org/current/userguide/tutorial_gradle_command_line.html#sec:profiling_build).
The quick version is, that if you just want to quickly check what it is that's
slowing down your build, `--profile` will be all you need. However, if you want
to continuously monitor your build and find bottlenecks that develop over time,
this plugin may be the right fit for you.  `build-time-tracker` writes a
continuous log that is monoidal and can be collected from various different
machines to run statistical analyses. Importantly, the written files contain
identifying information about the machine the build happened on so you can
compare apples with apples.

## Reporters

### CSVReporter

The `csv` reporter takes the following options:

* `output`: CSV output file location relative to Gradle execution.
* `append`: When set to `true` the CSV output file is not truncated. This is
  useful for collecting a series of build time profiles in a single CSV.
* `header`: When set to `false` the CSV output does not include a prepended
  header row with column names. Is desirable in conjunction with `append`.

A basic [R Markdown](http://rmarkdown.rstudio.com/) script, `report.Rmd` is
included for ploting and analysing build times using CSV output.

### CSVSummaryReporter

The `csvSummary` displays the accumulated total build time from a CSV file.
The reporter takes the following option:

* `csv`: Path (relative to the gradle file or absolute) to a CSV file created
  with the above reporter and the options `append = true` and `header = false`.

### SummaryReporter

The `summary` reporter gives you an overview of your tasks at the end of the
build. It has the following options:

* `threshold`: (default: 50) Minimum time in milliseconds to display a task.
* `ordered`: (default: false) Whether or not to sort the output in ascending
  order by time spent.
* `barstyle`: (default: "unicode") Supports "unicode", "ascii" and "none" for
  displaying a bar chart of the relative times spent on each task.
* `successOutput`: (default: "true") Redisplay build success or failure message
  so you don't miss it if the summary output is long.
* `shortenTaskNames`: (default: "true") Shortens long tasks names.

_Note_ This plugin only measures the task times that constitute a build.
Specifically, it does not measure the time in configuration at the start
of a Gradle run. This means that the time to execute a build with very fast
tasks is not accurately represented in output because it is dominated by
the time in configuration instead.

## Developing

This project is built and tested by [Travis](https://travis-ci.org) at
[passy/build-time-tracker-plugin](https://travis-ci.org/passy/build-time-tracker-plugin).

## Acknowledgements

Thanks to [Sindre Sorhus](https://github.com/sindresorhus) for contributing the
wonderful logo!

## License

    Copyright 2014 Pascal Hartig

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
