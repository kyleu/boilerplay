# SBT Tasks

* Run `scalastyle` to find style and linting violations. The plugin is configured through `./scalastyle-config.xml`.
* When you compile, your code is automatically formatted. You can manually run the formatter with `scalariformFormat`.
* To see a visualization of the project's dependencies, run `dependencyGraph`.
* Keep up-to-date by running `dependencyUpdates` and upgrading the versions in `./project/Dependencies.scala`.
* Run `stats` to get a listing of various metrics, including lines of code and project size.
* You can find the exact memory layout of a given class by running `jol:internals my.Class`.
* To get meta and run a plugin that introspects plugins, run `about-plugins`.
* Generate a visualization of your project dependencies with `projectsGraphDot`.
* Using `classDiagram my.class` will generate an SVG diagram of your class and its ancestors.
