[![Build Status](https://travis-ci.org/bootique/bootique-jcache.svg)](https://travis-ci.org/bootique/bootique-jcache)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.bootique.jcache/bootique-jcache/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.bootique.jcache/bootique-jcache/)

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
            <version>0.25</version>
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
