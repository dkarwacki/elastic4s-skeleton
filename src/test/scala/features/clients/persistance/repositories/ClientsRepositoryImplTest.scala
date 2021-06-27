package features.clients.persistance.repositories

import cats.implicits.catsSyntaxOptionId
import features.clients.persistance.indexes.ClientIndex
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import utils.ElasticSearchIntegrationTest
import utils.Generators._

class ClientsRepositoryImplTest extends AnyWordSpec with Matchers {
  "A ClientRepository" should {
    "add new client and give it id" in new ClientsRepositoryFixture {
      //when
      val result = target.add(client).unsafeRunSync()

      //then
      result.id.isDefined shouldEqual true
      result.name shouldEqual client.name
      result.surname shouldEqual client.surname
      result.address shouldEqual client.address
    }

    "update client" in new ClientsRepositoryFixture {
      //given
      val newName = "NewName"
      val createdClient = target.add(client).unsafeRunSync()

      //when
      val result =
        target.update(createdClient.copy(name = newName)).unsafeRunSync()

      //then
      result.id shouldEqual createdClient.id
      result.name shouldEqual newName
    }

    "get client by id" in new ClientsRepositoryFixture {
      //given
      val createdClient = target.add(client).unsafeRunSync()
      println(createdClient)

      //when
      val result = target.findById(createdClient.id.get).unsafeRunSync()

      //then
      result shouldEqual createdClient.some
    }

    "get all clients" in new ClientsRepositoryFixture {
      //given
      val createdClient1 = target.add(clientGen.one()).unsafeRunSync()
      val createdClient2 = target.add(clientGen.one()).unsafeRunSync()

      //when
      val result = target.findAll().unsafeRunSync()

      //then
      result should contain allOf (createdClient1, createdClient2)
    }

    "get by name, surname and address" in new ClientsRepositoryFixture {
      //given
      val createdClient = target.add(clientGen.one()).unsafeRunSync()

      //when
      val result = target
        .findByNameSurnameAndAddress(
          createdClient.name,
          createdClient.surname,
          createdClient.address
        )
        .unsafeRunSync()

      //then
      result shouldEqual createdClient.some
    }
  }

}

trait ClientsRepositoryFixture extends ElasticSearchIntegrationTest {
  //index preparation
  cleanIndex(ClientIndex.Name)

  //given
  val client = clientGen.one()

  val target = new ClientsRepositoryImpl(elasticClient)
}
