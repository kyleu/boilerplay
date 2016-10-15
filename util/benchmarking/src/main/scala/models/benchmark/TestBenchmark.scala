package models.benchmark

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._

class TestBenchmark {
  @Benchmark
  @Measurement(iterations = 4, time = 5, timeUnit = TimeUnit.SECONDS)
  @Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
  @Threads(6)
  @Fork(1)
  def creation() = {
    val s = scala.util.Random.nextInt.toString
    s.toString
  }
}
