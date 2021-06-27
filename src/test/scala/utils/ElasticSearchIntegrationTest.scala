package utils

import cats.effect.unsafe.IORuntime
import com.sksamuel.elastic4s.{ElasticClient, ElasticDsl, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
import config.Configuration
import core.elasticsearch.ElasticClientIO
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.util.Try

trait ElasticSearchIntegrationTest extends ElasticDsl {
  protected implicit val ioRuntime = IORuntime.global

  private lazy val elasticSearchTestConfig = ConfigSource
    .resources("application-test.conf")
    .loadOrThrow[Configuration]
    .elasticSearch

  protected val elasticClient: ElasticClientIO = ElasticClientIO(
    ElasticClient(
      JavaClient(
        ElasticProperties(
          s"http://${elasticSearchTestConfig.host}:${elasticSearchTestConfig.port}"
        )
      )
    )
  )

  protected def deleteIdx(indexName: String): Unit =
    Try {
      elasticClient
        .execute {
          deleteIndex(indexName)
        }
        .unsafeRunSync()
    }

  protected def createIdx(name: String): Unit =
    Try {
      elasticClient
        .execute {
          createIndex(name)
        }
        .unsafeRunSync()
    }

  protected def cleanIndex(indexName: String): Unit = {
    deleteIdx(indexName)
    createIdx(indexName)
  }
}
