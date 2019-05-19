package models.search

import java.util.UUID

import com.google.inject.Injector
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.services.Credentials
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.services.search.SearchProvider
import play.api.mvc.Call
import play.twirl.api.Html

import scala.concurrent.{ExecutionContext, Future}

class SearchHelper extends SearchProvider {
  override def intSearches(app: Application, injector: Injector, creds: Credentials)(q: String, id: Int)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start int searches */
    /* End int searches */
    Nil
  }

  override def uuidSearches(app: Application, injector: Injector, creds: Credentials)(q: String, id: UUID)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start uuid searches */
    /* End uuid searches */
    Nil
  }

  override def stringSearches(app: Application, injector: Injector, creds: Credentials)(q: String)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start string searches */
    /* End string searches */
    Nil
  }
}
