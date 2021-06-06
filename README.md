Backend: Design your own Beer
=============================

DyoB-Backend is a RESTful service to display products, manage orders and
payments. It's part of our fourth-semester-project and the implementation of
third semester's KreativBier.

Besides, the service also provides a basic inventory management which is served
by the service directly.


Setup
-----

Your system needs to provide the following tools:

1. _JDK 11_ and
2. a compatible _Apache Maven_ installation.


Development
-----------

The project uses Apache Maven to manage dependencies to libraries and
frameworks, as well as providing vital functions to build and deploy the project
itself. Use

```shell
mvn clean package
```

to download required dependencies, compile, test and build the project. The
following will do almost the same steps but will also start the application for
development purposes:

```shell
# With default CORS policy
mvn clean spring-boot:run

# With wildcard origin CORS policy
mvn clean spring-boot:run -P development
```

Alternatively, start the `Application` class with the IDE of your choice. To
allow requests from any origin you can either add the environment variable
`spring_profiles_active=cors` or pass `-Dspring.profiles.active=cors` in your
run configuration.


Usage
-----

Once the service is running, it exposes several resources on the configured
port (default 8080).

The product ressource provides functionalities for consuming products from the
configured catalog in a RESTful approach. This means, no session is required but
resources should be processed by another service.

Meanwhile, the inventory component is a more administrative one and requires
authentication to be accessed. Its endpoint `/stock` produces HTML and consumes
FormData, thus it's much likely be consumed with a browser. Visit
http://localhost:8080/stock and use the credentials `manager:secret` to manage
the product inventory.


API Guide
---------

The project provides a sophisticated workflow to generate documentation full of
useful information and examples.

To do so, several tools are utilized to transform test data into snippets and
examples. The base documentation is written in AsciiDoc
(see `src/main/asciidoc/index.adoc`) and includes generated snippet references
which will be substituted during packaging.

If you haven't already, run `nvm package` to generate snippets and render the
documentation into HTML. The result will be placed to
`target/generated-docs/index.html` and can be viewed in any browser.
Alternatively, visit https://v1ncnet.github.io/swt-backend/ to get an
always-up-to-date version of `origin/main`.


Deployment
----------

Since this service doesn't need extra software, a simple `mvn package` is enough
to perform necessary steps to build an executable JAR artifact.

To run, use `java -jar target/swt-0.2.0-SNAPSHOT.jar` to start the application
on `localhost` and port 8080.


License
-------

Apache License 2.0 - Built with :heart: in Dresden
