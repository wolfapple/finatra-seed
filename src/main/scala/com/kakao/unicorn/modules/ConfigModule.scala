package com.kakao.unicorn.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.twitter.inject.annotations.Flag
import com.typesafe.config.{Config, ConfigFactory}

object ConfigModule extends TwitterModule {

  flag("env", "development", "application environment [development:default, production]")

  @Provides
  @Singleton
  def provideConfig(@Flag("env") env: String): Config = {
    ConfigFactory.load(s"${env}")
  }

}