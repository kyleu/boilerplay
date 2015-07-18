#!/usr/bin/env bash
cd ../
sbt clean dependencyUpdates scalariformFormat scalastyle scapegoat dist
