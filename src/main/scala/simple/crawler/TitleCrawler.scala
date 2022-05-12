package simple.crawler

import cats.effect.{Concurrent, Sync}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import Domain.TitleEntity
import cats.Parallel
import cats.implicits.catsSyntaxParallelTraverse1

import scala.util.Try

trait TitleCrawler[F[_]] {
  def crawlUrl(url: String): F[TitleEntity]

  def crawlUrls(urls: List[String]): F[List[TitleEntity]]
}

object TitleCrawler {
  type Title = String

  def apply[F[_]]()(implicit C: Concurrent[F], S: Sync[F], P: Parallel[F]): TitleCrawler[F] = new TitleCrawler[F] { // TODO: Resource?
    private val browser = JsoupBrowser.typed()

    override def crawlUrl(url: String): F[TitleEntity] = {
      S.blocking {
        val title = Try {
          browser.get(url).title
        }.toEither

        TitleEntity(url, title)
      }
    }

    override def crawlUrls(urls: List[String]): F[List[TitleEntity]] = urls.parTraverse(crawlUrl)
  }
}
