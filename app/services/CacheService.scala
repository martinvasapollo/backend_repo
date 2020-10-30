package services

import akka.actor.Actor
import javax.inject.{Inject, Singleton}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CacheUpdateActor @Inject()(c: CacheService)(implicit ec: ExecutionContext) extends Actor with Logging {
  override def receive: Receive = {
    case _ =>
      logger.info("Receive message from scheduler to update")
      c.refreshCache()
  }
}

@Singleton
class CacheService @Inject()(pictureDetailService: PictureDetailService,
                             sessionService: SessionService) extends Logging {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  var cache: Option[List[PictureDetail]] = None

  def refreshCache(): Future[Unit] = {
    sessionService.withSession { implicit session =>
      pictureDetailService.getPicturesDetails().map { e =>
        cache = Some(e.toList)
        logger.info("Update cache OK")
      }
    }
  }
}
