package services

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future

case class AuthResponse(auth: Boolean, token: String)

@Singleton
class SessionService @Inject()(ws: WSClient, configuration: Configuration) {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def withSession[A](f: AuthResponse => Future[A]): Future[A] = {
    implicit val personReads = Json.reads[AuthResponse]
    val apiKey = configuration.get[String]("apikey")

    val data = Json.obj(
      "apiKey" -> apiKey,
    )
    val request = ws.url("http://interview.agileengine.com/auth").post(data)
    request.flatMap {
      r =>
        val authResponse = r.json.validate[AuthResponse].get
        f(authResponse)
    }
  }

}
