
package services

import javax.inject._
import play.api._
import play.api.inject.ApplicationLifecycle
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent.Future


case class Picture(id: String, cropped_picture: String)

@Singleton
class PictureService @Inject()(lifecycle: ApplicationLifecycle, ws: WSClient) extends Logging {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  var cache: Option[List[PictureDetail]] = None

  def getPicturesIds(page: Int)(implicit session: AuthResponse): Future[Seq[Picture]] = {
    implicit val PictureReads = Json.reads[Picture]
    val request = ws.url("http://interview.agileengine.com/images?page=" + page).addHttpHeaders("Authorization" -> ("Bearer " + session.token)).get()
    request.flatMap { r =>
      val hasMore = (r.json \ "hasMore").validate[Boolean].get
      val data = (r.json \ "pictures").validate[Seq[Picture]].get
      if (!hasMore) {
        Future(data)
      } else {
        logger.info("get page " + page)
        getPicturesIds(page + 1).map(e => data ++ e)
      }
    }
  }

}



