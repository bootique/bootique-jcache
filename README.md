<!--
  Licensed to ObjectStyle LLC under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ObjectStyle LLC licenses
  this file to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

[![build test deploy](https://github.com/bootique/bootique-jcache/actions/workflows/maven.yml/badge.svg)](https://github.com/bootique/bootique-jcache/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.bootique.jcache/bootique-jcache.svg?colorB=brightgreen)](https://search.maven.org/artifact/io.bootique.jcache/bootique-jcache/)

# bootique-jcache

Integration of JCache caching API with Bootique. Provides injectable CacheManager. 

*For additional help/questions about this example send a message to
[Bootique forum](https://groups.google.com/forum/#!forum/bootique-user).*
   
## Prerequisites
      
    * Java 1.8 or newer.
    * Apache Maven.
      
# Setup

## Add bootique-jcache to your build tool
**Maven**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.bootique.bom</groupId>
            <artifactId>bootique-bom</artifactId>
            <version>3.0.M2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>io.bootique.jcache</groupId>
    <artifactId>bootique-jcache</artifactId>
</dependency>
```

## Example Project

`bootique-jcache` **does not** bundle a JCache provider. You will need to explicitly include a provider of your choice 
on the classpath of your application, such as EhCache, Caffeine, Hazelcast, etc.

See usage examples:
* [bootique-ehcache-demo](https://github.com/bootique-examples/bootique-jcache-demo/tree/master/bootique-ehcache-demo) -
an example of [Ehcache](http://www.ehcache.org) usage in [Bootique](http://bootique.io) app. 
* [bootique-hazelcast-demo](https://github.com/bootique-examples/bootique-jcache-demo/tree/master/bootique-hazelcast-demo) - 
an example of [Hazelcast JCache](http://docs.hazelcast.org/docs/3.4/manual/html/jcache.html) usage in [Bootique](http://bootique.io) app.
