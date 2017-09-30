# Metrics

All meaningful operations are tracked through Scala Metrics, and are exposed through JMX, or via a servlet available on port 9001.
Reporting to Graphite can be enabled through application.conf, and reports to 127.0.0.1:2003 by default.
Metrics exposes all actors, queries, logs, requests, and jvm info.
