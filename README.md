[![Build Status](https://travis-ci.org/bootique/bootique-jcache.svg)](https://travis-ci.org/bootique/bootique-jcache)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.bootique.jcache/bootique-jcache/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.bootique.jcache/bootique-jcache/)

# bootique-jcache

Integration of JCache caching provider with Bootique. Provides injectable CacheManager. 

Does not bundle a JCache provider. You will need to include a provider of your choice on the classpath of your application,
such as EhCache, Hazelcast, etc.
