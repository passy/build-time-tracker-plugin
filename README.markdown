# Build Time Tracker
[![Build Status](https://travis-ci.org/passy/build-time-tracker-plugin.svg)](https://travis-ci.org/passy/build-time-tracker-plugin)

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


Developing
----------

This project is built and tested by [Travis](https://travis-ci.org) at
[passy/build-time-tracker-plugin](https://travis-ci.org/passy/build-time-tracker-plugin).

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
