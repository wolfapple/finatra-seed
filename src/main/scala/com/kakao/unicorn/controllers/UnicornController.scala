package com.kakao.unicorn.controllers

import com.twitter.finagle.http.{MediaType, Request}
import com.twitter.finatra.http.Controller

class UnicornController extends Controller {

  get("/health") { request: Request =>
    response.ok("ok").contentType(MediaType.PlainText)
  }

  get("/docs/:*") { request: Request =>
    response.ok.fileOrIndex(s"/docs/${request.params("*")}", "/docs/index.html")
  }

}