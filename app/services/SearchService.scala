package services

import javax.inject.{Inject, Singleton}
import jdk.jshell.spi.ExecutionControl.UserException
import play.api.Logging

@Singleton
class SearchService @Inject()(cacheService: CacheService) extends Logging {

  def search(query: String): Seq[PictureDetail] = {
    if (cacheService.cache.isEmpty) {
      throw new UserException("Cache not loaded yet!", "", Array[StackTraceElement]())
    }
    cacheService.cache.get.filter { entry =>
      entry.id.contains(query) ||
        entry.author.contains(query) ||
        entry.camera.getOrElse("").contains(query) ||
        entry.tags.contains(query)
    }
  }
}

