# ![Build Time Tracker](https://cdn.rawgit.com/passy/build-time-tracker-plugin/cc3bd9dcbda61ae7b699e4048c3f425525352d54/assets/logo.svg)

[![Build Status](https://travis-ci.org/passy/build-time-tracker-plugin.svg?branch=master)](https://travis-ci.org/passy/build-time-tracker-plugin)

How much time do you waste each day waiting for Gradle? Now you know!

Usage
-----

Apply the plugin in your `build.gradle`:

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'net.rdrei.android.buildtimetracker:gradle-plugin:0.1.+'
  }
}

apply plugin: 'build-time-tracker'

buildtimetracker {
  reporters {
    csv {
      output "buildtime/${date}.csv"
      append "true"
      header "false"
    }

    scribe {
      endpoint "https://example.com/scribe"
    }

    summary {}
  }
}
```

On an initial run, the output will look something like this:
```
$ ./gradlew clean assemble
(TBD)
```

The `csv` reporter takes the following options:

* `output`: CSV output file location relative to Gradle execution.
* `append`: (String setting) When set to `"true"` the CSV output file is
   not truncated. This is useful for collecting a series of build time
   profiles in a single CSV.
* `header`: (String setting) When set to `"false"` the CSV output does not
   include a prepended header row with column names. Is desireable in
   conjunction with `append`.

_Note_ This plugin only measures the task times that constitute a build.
Specifically, it does not measure the time in configuration at the start
of a Gradle run. This means that the time to execute a build with very fast
tasks is not accurately represented in output because it is dominated by
the time in configuration instead.


Developing
----------

This project is built and tested by [Travis](https://travis-ci.org) at
[passy/build-time-tracker-plugin](https://travis-ci.org/passy/build-time-tracker-plugin).

## Acknowledgements

Thanks to [Sindre Sorhus](https://github.com/sindresorhus) for contributing the
wonderful logo!

License
--------

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
