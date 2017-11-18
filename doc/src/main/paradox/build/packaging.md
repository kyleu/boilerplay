# Packaging

Boilerplay can produce a standard Play distribution by running `dist` from SBT.

Other distribution format are available. More than you'd ever need, in fact:

* `assembly` - Produces a self-contained "uberjar" capable of being run with `java -jar boilerplay.jar`.
* `jdkPackager:packageBin` - Creates an OS-specific installer, such as a `DMG` for macOS, an `exe` for windows, or a Debian package.
* `docker:publishLocal` - Build a Docker image and publishes it to your local Docker repository.
