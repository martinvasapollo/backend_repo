import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import scheduler.Scheduler
import services.{CacheUpdateActor}


class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[CacheUpdateActor]("scheduler-actor")
    bind(classOf[Scheduler]).asEagerSingleton()
  }
}
