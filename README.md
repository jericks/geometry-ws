[![Build Status](https://travis-ci.org/jericks/geometry-ws.svg?branch=master)](https://travis-ci.org/jericks/geometry-ws)

geometry-ws
===========

Micro geometry web services using the Java Topology Suite (JTS) and Micronaut.

Build
-----

```
./gradlew clean build
```

Run
---

```
./gradlew run
```

```
java -jar build/libs/geometry-ws-0.1-all.jar
```

Examples
--------

```
http://localhost:8080/buffer/wkt/wkt?geom=POINT(1 1)&d=10
```

```
http://localhost:8080/buffer/wkt/kml?geom=POINT(1 1)&d=10
```

```
http://localhost:8080/swagger/geometry-web-services-0.0.yml
```

Swagger API documentation is available.

![Swagger API](docs/images/swagger.png)

You can use it to try out all of the available web services.

![Centroid Web Service](docs/images/centroid.png)

You can visualize result in an OpenLayers or Leaflet map.

![Buffer Web Service](docs/images/buffer.png)
