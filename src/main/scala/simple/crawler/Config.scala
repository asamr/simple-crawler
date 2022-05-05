package simple.crawler

import com.comcast.ip4s.IpLiteralSyntax

object Config {
  val availableCores = Runtime.getRuntime.availableProcessors()
  val serverHost = ipv4"0.0.0.0"
  val serverPort = port"8080"
}
