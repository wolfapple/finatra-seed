package com.kakao.unicorn

import java.net.URLEncoder

trait Unicorn {

  val service = "unicorn"
  val ics = "WGS84"
  val ocs = "WGS84"
  val appkey = "bed13e8d5cb7929de30e5c870cd7374f"

  private def queryString(params: Seq[(String, Any)]): String = {
    params.filter(_._2 != None).map(param => param match {
      case (k, v) => s"${URLEncoder.encode(k, "UTF-8")}=${URLEncoder.encode(v.toString, "UTF-8")}"
      case _ => ""
    }).mkString("&")
  }

  def toBody(params: (String, Any)*): String = {
    queryString(params)
  }

  def toQuery(params: (String, Any)*): String = {
    queryString(params)
  }

  def toQuery(uri: String, params: (String, Any)*): String = {
    uri match {
      case v: String => s"${URLEncoder.encode(uri, "UTF-8")}?${queryString(params)}"
      case _ => queryString(params)
    }
  }

}