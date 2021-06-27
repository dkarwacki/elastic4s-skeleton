package features.clients.persistance.repositories

import cats.effect.IO
import cats.implicits._
import com.sksamuel.elastic4s.circe.hitReaderWithCirce
import com.sksamuel.elastic4s.Response
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import core.elasticsearch.ElasticClientIO
import features.clients.domain.Client
import features.clients.persistance.indexes.ClientIndex
import io.circe.syntax.EncoderOps

trait ClientsRepository {
  def createIndex(): IO[Boolean]
  def add(client: Client): IO[Client]
  def update(client: Client): IO[Client]
  def findById(clientId: String): IO[Option[Client]]
  def findByNameSurnameAndAddress(
      name: String,
      surname: String,
      address: String
  ): IO[Option[Client]]
  def findAll(): IO[List[Client]]
}

class ClientsRepositoryImpl(elasticClient: ElasticClientIO)
    extends ClientsRepository {
  import com.sksamuel.elastic4s.ElasticDsl._

  override def createIndex(): IO[Boolean] =
    elasticClient
      .execute(ClientIndex.createRequest())
      .map(_.result.acknowledged)

  override def add(client: Client): IO[Client] =
    elasticClient
      .execute {
        indexInto(ClientIndex.Name)
          .source(client.asJson.toString())
          .refreshImmediately
      }
      .map(r => client.copy(id = r.result.id.some))

  override def update(client: Client): IO[Client] =
    client.id match {
      case Some(id) =>
        elasticClient.execute {
          updateById(ClientIndex.Name, id)
            .doc(client.asJson.toString())
            .refreshImmediately
        } >> IO.pure(client)
      case None => add(client)
    }

  override def findById(clientId: String): IO[Option[Client]] =
    elasticClient
      .execute {
        get(ClientIndex.Name, clientId)
      }
      .map(response => {
        if (response.result.found)
          response.result.to[Client].copy(id = response.result.id.some).some
        else
          None
      })

  override def findAll(): IO[List[Client]] =
    elasticClient
      .execute {
        search(ClientIndex.Name)
      }
      .map(toClientWithId)

  override def findByNameSurnameAndAddress(
      name: String,
      surname: String,
      address: String
  ): IO[Option[Client]] =
    elasticClient
      .execute {
        search(ClientIndex.Name)
          .query(
            boolQuery()
              .must(
                matchQuery(ClientIndex.Fields.Name, name),
                matchQuery(ClientIndex.Fields.Surname, surname),
                matchQuery(ClientIndex.Fields.Address, address)
              )
          )
      }
      .map(toClientWithId)
      .map(_.headOption)

  private def toClientWithId(
      response: Response[SearchResponse]
  ): List[Client] = {
    response.result
      .to[Client]
      .zip(response.result.ids)
      .map { case (client, id) => client.copy(id = id.some) }
      .toList
  }
}
