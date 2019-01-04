package com.kakao.unicorn.modules

import com.google.inject.{Inject, Provides, Singleton}
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.inject.TwitterModule
import com.typesafe.config.Config
import zipkin.finagle.http.HttpZipkinTracer

object ZipkinModule extends TwitterModule {

  @Provides
  @Singleton
  def providerTracer(@Inject config: Config): HttpZipkinTracer = {
    val conf = HttpZipkinTracer.Config
      .builder()
      .initialSampleRate(config.getNumber("zipkin.sample_rate").floatValue())
      .host(s"${config.getString("zipkin.host")}:${config.getInt("zipkin.port")}")
      .hostHeader(config.getString("zipkin.host"))
      .build()
    HttpZipkinTracer.create(conf, NullStatsReceiver)
  }

}