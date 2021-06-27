package core.elasticsearch

import cats.effect.IO
import com.sksamuel.elastic4s._

case class ElasticClientIO(elasticClient: ElasticClient) {
  implicit val ioExecutor: Executor[IO] =
    com.sksamuel.elastic4s.cats.effect.instances.catsEffectExecutor[IO]
  implicit val ioFunctor: Functor[IO] =
    com.sksamuel.elastic4s.cats.effect.instances.catsFunctor[IO]

  def execute[T, U](t: T)(implicit
      handler: Handler[T, U],
      manifest: Manifest[U],
      options: CommonRequestOptions
  ): IO[Response[U]] = elasticClient.execute(t)

  def show[T](t: T)(implicit handler: Handler[T, _]): String =
    elasticClient.show(t)
  def close(): Unit = elasticClient.close()
}
