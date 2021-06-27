package core.elasticsearch

import cats.effect.{IO, Resource}
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
import config.Configuration
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object ElasticsearchClientResource {
  private lazy val elasticSearchConfig =
    ConfigSource.default.loadOrThrow[Configuration].elasticSearch

  def apply(): Resource[IO, ElasticClientIO] =
    Resource.make {
      IO {
        ElasticClientIO(
          ElasticClient(
            JavaClient(
              ElasticProperties(
                s"http://${elasticSearchConfig.host}:${elasticSearchConfig.port}"
              )
            )
          )
        )
      }
    }(c => IO(c.close()))
}
