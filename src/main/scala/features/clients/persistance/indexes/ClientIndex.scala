package features.clients.persistance.indexes
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.fields.{IntegerField, KeywordField, TextField}

object ClientIndex {
  val Name = "clients"
  object Fields {
    val Id = "id"
    val Name = "name"
    val Surname = "surname"
    val Age = "age"
    val Address = "address"
  }

  def createRequest() = {
    createIndex(Name).mapping(
      properties(
        KeywordField(Fields.Name),
        KeywordField(Fields.Surname),
        IntegerField(Fields.Age),
        TextField(Fields.Address)
      )
    )
  }
}
