package com.kakao.unicorn.mappers

import com.google.inject.{Inject, Singleton}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.finatra.httpclient.HttpClientException

@Singleton
class HttpClientExceptionMapper @Inject()(response: ResponseBuilder) extends ExceptionMapper[HttpClientException] {

  override def toResponse(request: Request, exception: HttpClientException): Response = {
    response.status(exception.status).body(exception.getMessage).contentType("application/json")
  }

}