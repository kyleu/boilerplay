package models.supervisor

import akka.util.Timeout
import models.InternalMessage.{GetSystemStatus, SystemStatus}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.graphql.{GraphQLContext, SchemaHelper}
import sangria.macros.derive._
import sangria.schema.{fields, _}
import util.FutureUtils.graphQlContext

object SocketDecriptionSchema extends SchemaHelper("note") {
  val channelArg = Argument("channel", OptionInputType(StringType), description = "Optionally filters returned results to the provided channel name.")
  val socketIdArg = Argument("socketId", OptionInputType(uuidType), description = "Optionally filters returned results to the exact channel id.")

  implicit lazy val socketDecriptionType: ObjectType[GraphQLContext, SocketDescription] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "sockets",
    fieldType = ListType(socketDecriptionType),
    arguments = channelArg :: socketIdArg :: Nil,
    resolve = c => traceF(c.ctx, "search")(_ => {
      import akka.pattern.ask
      import scala.concurrent.duration._
      implicit val timeout: Timeout = Timeout(1.second)

      ask(c.ctx.app.supervisor, GetSystemStatus).mapTo[SystemStatus].map { x =>
        x.channels.flatMap(_._2).filter(s => c.arg(channelArg).forall(_ == s.channel) && c.arg(socketIdArg).forall(_ == s.socketId))
      }
    })
  ))
}
