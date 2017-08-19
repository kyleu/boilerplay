package util.metrics

case class MetricsConfig(
  jmxEnabled: Boolean,

  tracingEnabled: Boolean,
  tracingServer: String,
  tracingPort: Int,

  graphiteEnabled: Boolean,
  graphiteServer: String,
  graphitePort: Int,

  servletEnabled: Boolean,
  servletPort: Int
)
