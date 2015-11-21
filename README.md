GraphAware Neo4j Warmup
=======================

[![Build Status](https://travis-ci.org/graphaware/neo4j-warmup.png)](https://travis-ci.org/graphaware/neo4j-warmup) | <a href="http://graphaware.com/downloads/" target="_blank">Downloads</a> | Latest Release: 2.3.1.36.5

GraphAware WarmUp is a simple piece of code that warms up Neo4j caches using a single API call by accessing the entire
graph using all available processors.

Getting the Software
--------------------

### Server Mode

When using Neo4j in the <a href="http://docs.neo4j.org/chunked/stable/server-installation.html" target="_blank">standalone server</a> mode,
you will need the <a href="https://github.com/graphaware/neo4j-framework" target="_blank">GraphAware Neo4j Framework</a> and GraphAware Neo4j WarmUp .jar files (both of which you can <a href="http://graphaware.com/downloads/" target="_blank">download here</a>) dropped
into the `plugins` directory of your Neo4j installation. After Neo4j restart, you will be able to use the REST API.

### Embedded Mode / Java Development

Java developers that use Neo4j in <a href="http://docs.neo4j.org/chunked/stable/tutorials-java-embedded.html" target="_blank">embedded mode</a>
and those developing Neo4j <a href="http://docs.neo4j.org/chunked/stable/server-plugins.html" target="_blank">server plugins</a>,
<a href="http://docs.neo4j.org/chunked/stable/server-unmanaged-extensions.html" target="_blank">unmanaged extensions</a>,
GraphAware Runtime Modules, or Spring MVC Controllers can include use WarmUp as a dependency for their Java project.

#### Releases

Releases are synced to <a href="http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22timetree%22" target="_blank">Maven Central repository</a>. When using Maven for dependency management, include the following dependency in your pom.xml.

    <dependencies>
        ...
        <dependency>
            <groupId>com.graphaware.neo4j</groupId>
            <artifactId>warmup</artifactId>
            <version>2.3.1.36.5</version>
        </dependency>
        ...
    </dependencies>

#### Note on Versioning Scheme

The version number has two parts. The first four numbers indicate compatibility with Neo4j GraphAware Framework.
 The last number is the version of the WarmUp library. For example, version 2.1.3.15.1 is version 1 of WarmUp
 compatible with GraphAware Neo4j Framework 2.1.3.15.

Using GraphAware WarmUp
-----------------------

In server mode, issue a GET request to `http://your-server-address:7474/graphaware/warmup/` to warm up the caches.
In Java, use the `warmUp` method on the `WarmUp` class.

License
-------

Copyright (c) 2014 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
