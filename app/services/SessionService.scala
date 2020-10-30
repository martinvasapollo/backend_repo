package services

import javax.inject.{Inject, Singleton}
import play.api.inject.ApplicationLifecycle
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future


case class AuthResponse(auth: Boolean, token: String)


@Singleton
class SessionService @Inject()(lifecycle: ApplicationLifecycle, ws: WSClient) {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def withSession[A](f: AuthResponse => Future[A]): Future[A] = {
    implicit val personReads = Json.reads[AuthResponse]
    val data = Json.obj(
      "apiKey" -> "23567b218376f79d9415",
    )
    val request = ws.url("http://interview.agileengine.com/auth").post(data)
    request.flatMap {
      r =>
        val authResponse = r.json.validate[AuthResponse].get
        f(authResponse)
    }
  }

}
