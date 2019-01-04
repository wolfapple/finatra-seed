package com.kakao.unicorn.modules

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}
import java.util.Locale

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.finagle.http.{Request, Response}
import com.twitter.inject.TwitterModule
import com.twitter.util.Duration
import org.slf4j.MDC

object UnicornAccessLogModule extends TwitterModule {

  override def configure() {
    bindSingleton[com.twitter.finagle.filter.LogFormatter[Request, Response]].to[JsonLogFormatter]
  }

}

class JsonLogFormatter extends com.twitter.finagle.http.filter.LogFormatter {

  val DateFormat: DateTimeFormatter =
    DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss Z")
      .withLocale(Locale.KOREA)
      .withZone(ZoneId.of("Asia/Seoul"))

  def format(request: Request, response: Response, responseTime: Duration) = {
    val remoteAddr = request.remoteAddress.getHostAddress

    val contentLength = response.length
    val contentLengthStr = if (contentLength > 0) contentLength.toString else "-"

    val uaStr = request.userAgent.getOrElse("-")

    val mapper = new ObjectMapper
    val node = mapper.createObjectNode
    node.put("time", formattedDate)
    node.put("host", remoteAddr)
    node.put("method", escape(request.method.toString))
    node.put("path", escape(request.uri))
    node.put("version", escape(request.version.toString))
    node.put("status", response.statusCode)
    node.put("content_length", contentLengthStr)
    node.put("duration", responseTime.inMillis)
    node.put("user_agent", escape(uaStr))
    node.put("trace_id", MDC.get("traceId"))
    node.toString
  }

  def formatException(request: Request, throwable: Throwable, responseTime: Duration): String =
    throw new UnsupportedOperationException(throwable.getMessage)

  def formattedDate(): String = ZonedDateTime.now.format(DateFormat)

}