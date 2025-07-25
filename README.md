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

Integration of JCache caching API with Bootique. Provides an injectable CacheManager. 

*For additional help/questions about this example send a message to
[Bootique forum](https://groups.google.com/forum/#!forum/bootique-user).*
   
# Setup

## Add bootique-jcache to your build tool
**Maven**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.bootique.bom</groupId>
            <artifactId>bootique-bom</artifactId>
            <version>3.0-RC1</version>
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
on the classpath of your application, such as EhCache, Caffeine, Hazelcast, etc. You can find the details of different 
provider integrations in the [examples](https://github.com/bootique-examples/bootique-jcache-examples).

Providers are chosen either implicitly by locating the provider class in `META-INF/services/javax.cache.spi.CachingProvider`
in the application dependencies (a similar mechanism is used to locate Bootique's own modules), or taken from configuration:

```yaml
jcache:
   provider: org.ehcache.jsr107.EhcacheCachingProvider
```