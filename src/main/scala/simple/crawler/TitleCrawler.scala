package simple.crawler

import cats.effect.{Concurrent, Sync}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import fs2.Stream
import Domain.TitleEntity
import scala.util.Try

trait TitleCrawler[F[_]] {
  def crawlUrl(url: String): F[TitleEntity]

  def crawlUrls(urls: List[String]): F[List[TitleEntity]]
}

object TitleCrawler {
  type Title = String

  def apply[F[_]]()(implicit C: Concurrent[F], S: Sync[F]): TitleCrawler[F] = new TitleCrawler[F] {
    private val browser = new JsoupBrowser()

    override def crawlUrl(url: String): F[TitleEntity] =
      S.delay {
        val title = Try {
          browser.get(url).title
        }.toEither

        TitleEntity(url, title)
      }

    override def crawlUrls(urls: List[String]): F[List[TitleEntity]] =
      Stream
        .emits(urls)
        .covary[F]
        .parEvalMap(Config.availableCores * 2)(crawlUrl)
        .compile
        .toList
  }
}
