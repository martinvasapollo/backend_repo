package services

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.inject.ApplicationLifecycle
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future


case class PictureDetail(id: String, author: String, tags: String, cropped_picture: String, full_picture: String, camera: Option[String])


@Singleton
class PictureDetailService @Inject()(lifecycle: ApplicationLifecycle, ws: WSClient, pictureService: PictureService) extends Logging {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def getPictureDetail(id: String)(implicit session: AuthResponse): Future[PictureDetail] = {
    implicit val DetailsReads = Json.reads[PictureDetail]
    val request = ws.url("http://interview.agileengine.com/images/" + id).addHttpHeaders("Authorization" -> ("Bearer " + session.token)).get()
    request.map { r =>
      r.json.validate[PictureDetail].get
    }
  }

  def getPicturesDetails()(implicit session: AuthResponse): Future[Seq[PictureDetail]] = {
    pictureService.getPicturesIds(1).flatMap { pictures =>
      val futures = pictures.map { picture =>
        getPictureDetail(picture.id)
      }
      Future.sequence(futures)
    }
  }


}