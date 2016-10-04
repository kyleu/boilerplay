package gatling

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SimpleSimulation extends Simulation {
  val domain = "localhost:9000"
  val pauseSeconds = 1

  val httpConf = http
    .baseURL(s"http://$domain")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val homeRequest = http("Splash Page").get("/").check(status.is(200))

  println("?")

  val scn = scenario("Basic Test").exec(homeRequest).pause(pauseSeconds)

  setUp(scn.inject(
    rampUsers(10) over 10.seconds
  ).protocols(httpConf))
}
