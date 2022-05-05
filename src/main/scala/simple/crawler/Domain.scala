package simple.crawler

import cats.effect.Async
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

object Domain {

  final case class TitleRequest(urls: List[String])
  object TitleRequest {
    implicit val decoder: Decoder[TitleRequest] = deriveDecoder
    implicit def entityDecoder[F[_] : Async]: EntityDecoder[F, TitleRequest] = jsonOf[F, TitleRequest]
  }

  final case class TitleEntity(url: String, title: Either[Throwable, String])
  object TitleEntity {
    implicit val throwableEncoder: Encoder[Throwable] = Encoder.encodeString.contramap(_.getMessage)
    implicit val eitherEncoder: Encoder[Either[Throwable, String]] =
      Encoder.encodeEither[Throwable, String]("error", "title")
    implicit val encoder: Encoder[TitleEntity] = deriveEncoder
    implicit def entityEncoder[F[_]]: EntityEncoder[F, TitleEntity] = jsonEncoderOf[F, TitleEntity]
  }

  final case class TitleResponse(titleUrls: List[TitleEntity])
  object TitleResponse {
    implicit val encoder: Encoder[TitleResponse] = deriveEncoder[TitleResponse]
    implicit def entityEncoder[F[_]]: EntityEncoder[F, TitleResponse] = jsonEncoderOf[F, TitleResponse]
  }
}
