package models.supervisor

import akka.util.Timeout
import graphql.GraphQLUtils._
import graphql.{GraphQLContext, GraphQLSchemaHelper}
import models.InternalMessage.{GetSystemStatus, SystemStatus}
import sangria.macros.derive.deriveObjectType
import sangria.schema._

object SocketDescriptionSchema extends GraphQLSchemaHelper("socket") {
  val channelArg = Argument("channel", OptionInputType(StringType))
  val socketIdArg = Argument("socketId", OptionInputType(uuidType))

  implicit lazy val socketDecriptionType: ObjectType[GraphQLContext, SocketDescription] = deriveObjectType()

  val queryFields = fields(unitField(name = "sockets", desc = None, t = ListType(socketDecriptionType), f = (c, _) => {
    import akka.pattern.ask

    import scala.concurrent.duration._
    implicit val timeout: Timeout = Timeout(1.second)

    ask(c.ctx.app.supervisor, GetSystemStatus).mapTo[SystemStatus].map { x =>
      x.channels.flatMap(_._2).filter(s => c.arg(channelArg).forall(_ == s.channel) && c.arg(socketIdArg).forall(_ == s.socketId))
    }
  }, channelArg, socketIdArg))
}
