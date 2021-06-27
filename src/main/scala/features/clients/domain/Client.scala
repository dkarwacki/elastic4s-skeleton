package features.clients.domain
import io.circe._
import io.circe.generic.semiauto._

case class Client(
    name: String,
    surname: String,
    age: Int,
    address: String,
    id: Option[String] = None
)

object Client {
  implicit val decoder: Decoder[Client] = deriveDecoder[Client]
  implicit val encoder: Encoder[Client] = deriveEncoder[Client]
}
