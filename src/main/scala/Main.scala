import cats.effect.{ExitCode, _}
import core.elasticsearch.ElasticsearchClientResource
import features.clients.persistance.repositories.ClientsRepositoryImpl

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    ElasticsearchClientResource()
      .use { elasticClient =>
        val clientsRepository = new ClientsRepositoryImpl(elasticClient)
        clientsRepository.createIndex()
      }
      .as(ExitCode.Success)
}
