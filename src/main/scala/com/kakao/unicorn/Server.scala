package com.kakao.unicorn

import com.google.inject.Module
import com.kakao.unicorn.controllers.UnicornController
import com.kakao.unicorn.mappers.HttpClientExceptionMapper
import com.kakao.unicorn.modules.{ConfigModule, UnicornAccessLogModule, ZipkinModule}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import zipkin.finagle.http.HttpZipkinTracer

object ServerMain extends Server {
  // SIGTERM graceful shutdown
  sys.addShutdownHook(Thread.sleep(10000))
}

class Server extends HttpServer {
  override def configureHttpServer(server: Http.Server): Http.Server = {
    // TODO update label
    server.withLabel("finatra-seed").withTracer(injector.instance[HttpZipkinTracer])
  }

  override def modules = Seq(ConfigModule, ZipkinModule)

  override def accessLogModule: Module = UnicornAccessLogModule

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[UnicornController]
      .exceptionMapper[HttpClientExceptionMapper]
  }
}