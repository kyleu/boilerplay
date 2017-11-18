# Adding Models

Boilerplay is designed to be extended. For a model that mirrors a database table, you can implement the following:

* Start with the model itself, create a case class to represent your table, extending `DataFieldModel`. 
* To save compilation time, add circe codecs to your companion object:
```
  implicit val jsonEncoder: Encoder[Note] = deriveEncoder
  implicit val jsonDecoder: Decoder[Note] = deriveDecoder
```
* Create a query class by extending `BaseQueries[YourModel]`. Dig through BaseQueries to unerstand the queries you can expose.
* Build your service class by extending `ModelServiceHelper[YourModel]`, and start creating the methods that operate on your models.
* Add a controller file, extending `BaseController`, and routes for your actions. See the other subclasses to understand how to create actions effectively.
* If desired, create a GraphQL schema, optionally extending `SchemaHelper`, and add the object's fetchers, relations, and fields to the root `Schema` object.
