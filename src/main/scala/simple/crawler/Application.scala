package simple.crawler

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val crawler = TitleCrawler[IO]()
    val route = Routes.crawlRoute[IO](crawler).orNotFound
    val loggedHttpApp = Logger.httpApp(true, true)(route)

    EmberServerBuilder.default[IO]
      .withHost(Config.serverHost)
      .withPort(Config.serverPort)
      .withHttpApp(loggedHttpApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
