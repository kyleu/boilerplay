package services.audit

import java.time.LocalDateTime
import java.util.UUID

import akka.actor.ActorRef
import models.audit._
import models.auth.Credentials
import models.result.data.DataField
import services.supervisor.ActorSupervisor
import util.{DateUtils, Logging}
import util.FutureUtils.serviceContext
import util.tracing.TraceData

import scala.concurrent.Future

object AuditCache {
  val maxPending = 1000
  val maxDurationMs = 30000

  case class CacheEntry(t: String, pk: Seq[String], fields: Seq[DataField])
}

class AuditCache(supervisor: ActorRef, lookup: AuditLookup) extends Logging {
  private[this] val cache = new collection.mutable.HashMap[UUID, (AuditStart, Seq[AuditCache.CacheEntry], LocalDateTime)]()

  def pendingCount = cache.size
  def pending = cache.toSeq.sortBy(_._2._3)(util.DateUtils.localDateTimeOrdering)

  def onStart(creds: Credentials, id: UUID, msg: AuditStart)(implicit traceData: TraceData) = {
    supervisor.tell(ActorSupervisor.Broadcast("audit", models.ResponseMessage.AuditStartNotification(id, msg)), supervisor)

    val f = msg.models.map { model =>
      lookup.getByPk(creds, model.t, model.pk: _*).map(_.map(_.toDataFields).getOrElse {
        Seq(DataField("error", Some(s"Could not load model [${model.t}:${model.pk.mkString("/")}] for read.")))
      }).map(model -> _)
    }
    Future.sequence(f).map { results =>
      val entries = results.map(x => AuditCache.CacheEntry(x._1.t, x._1.pk, x._2))
      val v = (msg, entries, DateUtils.now)
      cache.update(id, v)
      log.info(s"Started audit with id [$id] ($pendingCount in cache).")
      "OK"
    }
  }

  private[this] def getAuditFields(o: AuditCache.CacheEntry, n: Seq[DataField]) = o.fields.flatMap(f => n.find(_.k == f.k).flatMap { nf =>
    if (f.v != nf.v) { Some(AuditField(k = f.k, o = f.v, n = nf.v)) } else { None }
  })

  def onComplete(creds: Credentials, msg: AuditComplete)(implicit traceData: TraceData) = {
    val current = cache.getOrElse(msg.id, throw new IllegalStateException(
      s"Cannot find audit with id [${msg.id}] ($pendingCount in cache)."
    ))
    supervisor.tell(ActorSupervisor.Broadcast("audit", models.ResponseMessage.AuditCompleteNotification(msg)), supervisor)

    val updateLookup = Future.sequence(current._2.map { model =>
      lookup.getByPk(creds, model.t, model.pk: _*).map(_.map(_.toDataFields).getOrElse {
        Seq(DataField("error", Some(s"Could not load model [${model.t}:${model.pk.mkString("/")}] for read.")))
      }).map(model -> _)
    })
    val insertLookup = Future.sequence(msg.inserted.map { model =>
      lookup.getByPk(creds, model.t, model.pk: _*).map(_.map(_.toDataFields).getOrElse {
        Seq(DataField("error", Some(s"Could not load model [${model.t}:${model.pk.mkString("/")}] for read.")))
      }).map(model -> _)
    })

    updateLookup.flatMap { u =>
      insertLookup.map { i =>
        val updates = u.map { c =>
          AuditRecord(auditId = msg.id, t = c._1.t, pk = c._1.pk, changes = getAuditFields(c._1, c._2))
        }.filter(_.changes.nonEmpty)

        val inserts = i.map { c =>
          AuditRecord(auditId = msg.id, t = c._1.t, pk = c._1.pk, changes = c._2.map(x => AuditField(x.k, None, x.v)))
        }

        val audit = Audit(
          id = msg.id,
          act = current._1.action,
          client = current._1.client,
          userId = creds.user.id,
          tags = current._1.tags ++ msg.tags,
          msg = msg.msg,
          records = updates ++ inserts,
          started = current._3,
          completed = util.DateUtils.now
        )
        cache.remove(audit.id)
        log.info(s"Completed audit with id [${msg.id}] ($pendingCount in cache).")
        AuditHelper.onAudit(audit)
        audit
      }
    }
  }
}
