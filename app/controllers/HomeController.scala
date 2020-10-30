package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import services.{CacheService, PictureDetail, PictureService, SearchService}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, cacheService: CacheService, searchService: SearchService) extends AbstractController(cc) with Logging {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def cache() = Action { implicit request: Request[AnyContent] =>
    implicit val PictureWrites = Json.writes[PictureDetail]
    Ok(Json.stringify(Json.toJson(cacheService.cache)))
  }

  def search(query: String) = Action { implicit request: Request[AnyContent] =>
    implicit val PictureWrites = Json.writes[PictureDetail]
    Ok(Json.stringify(Json.toJson(searchService.search(query))))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

}
