package simple.crawler

import cats.effect.Async
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import Domain.{TitleRequest, TitleResponse}
import cats.implicits._

object Routes {

  def crawlRoute[F[_]: Async](crawler: TitleCrawler[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case req@POST -> Root / "crawler" =>
        req
          .as[TitleRequest]
          .flatMap(urls => crawler.crawlUrls(urls.urls))
          .flatMap(entities => Ok(TitleResponse(entities)))
    }
  }
}
