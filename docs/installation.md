# Installation

Installing the node-related plugins can be done in multiple ways. The easiest is to use the `plugins`-closure 
in your `build.gradle` file:

```gradle
plugins {
  id "com.github.node-gradle.node" version "3.3.0"
}
```

You can also install the plugins by using the traditional Gradle way:

```gradle
buildscript {
  repositories {
    gradlePluginPortal()
  }

  dependencies {
    classpath "com.github.node-gradle:gradle-node-plugin:3.3.0"
  }
}

apply plugin: 'com.github.node-gradle.node'
```
