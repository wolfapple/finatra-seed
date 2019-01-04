package com.kakao.unicorn.modules

import com.kakao.unicorn.ServerMain
import com.twitter.conversions.time._
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.finatra.httpclient.HttpClient
import com.twitter.finatra.httpclient.modules.HttpClientModule
import com.twitter.finatra.json.FinatraObjectMapper
import com.twitter.util.Duration
import zipkin.finagle.http.HttpZipkinTracer

object UnicornHttpClientModule {
  def build(label: String
            , host: String
            , port: Int = 80
            , ssl: Boolean
            , mapper: FinatraObjectMapper
            , tracer: HttpZipkinTracer
            , requestTimeout: Duration = 3.seconds) = {

    val httpClientModule = new HttpClientModule {
      override def dest = s"$host:$port"

      override def defaultHeaders: Map[String, String] = Map("Host" -> host)

      def buildHttpClient(mapper: FinatraObjectMapper,
                          httpService: Service[Request, Response]): HttpClient = {
        new HttpClient(
          hostname = hostname,
          httpService = httpService,
          retryPolicy = retryPolicy,
          defaultHeaders = defaultHeaders,
          mapper = mapper
        )
      }
    }

    val httpClient = ssl match {
      case false => Http.client
        .withLabel(label)
        .withRequestTimeout(requestTimeout)
        .withTracer(tracer)
        .withSessionQualifier.noFailFast
        .newClient(s"$host:$port")
        .toService
      case true => Http.client
        .withLabel(label)
        .withRequestTimeout(requestTimeout)
        .withTracer(tracer)
        .withSessionQualifier.noFailFast
        .withTls(host)
        .newClient(s"$host:$port")
        .toService
    }

    httpClientModule.buildHttpClient(mapper, httpClient)
  }
}