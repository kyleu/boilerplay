package utils.metrics

case class MetricsConfig(
  jmxEnabled: Boolean,
  graphiteEnabled: Boolean,
  graphiteServer: String,
  graphitePort: Int,
  servletEnabled: Boolean,
  servletPort: Int
)
