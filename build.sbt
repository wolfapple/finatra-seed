name := "finatra-seed"

organization := "com.kakao"

version := "0.1"

scalaVersion := "2.12.4"

parallelExecution in ThisBuild := false

resolvers ++= Seq(
  Resolver.mavenLocal,
  "kakao-maven-proxy-releases" at "http://repo.daumkakao.io/content/repositories/daum/",
  "kakao-maven-proxy-snapshots" at "http://repo.daumkakao.io/content/repositories/daum-snapshots/",
  "dk-t9-group-releases" at "http://repo.daumkakao.io/content/repositories/dk-t9-release/",
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

lazy val versions = new {
  val finatra = "18.3.0"
  val guice = "4.0"
  val logback = "1.1.7"

  val mockito = "1.9.5"
  val scalatest = "3.0.0"
  val scalacheck = "1.13.4"
  val specs2 = "2.4.17"

  val typesafeConfig = "1.3.1"
  val scalaUri = "0.5.0"
  val sentryLogback = "1.6.0"
  val zipkin = "1.2.1"
}

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra,
//"com.twitter" %% "finagle-redis" % versions.finagle,
  "ch.qos.logback" % "logback-classic" % versions.logback,

  "com.typesafe" % "config" % versions.typesafeConfig,
  "io.lemonlabs" %% "scala-uri" % versions.scalaUri,
  "io.sentry" % "sentry-logback" % versions.sentryLogback,
  "io.zipkin.finagle" %% "zipkin-finagle-http" % versions.zipkin,

  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
  "org.specs2" %% "specs2-mock" % versions.specs2 % "test"
)

javacOptions ++= Seq("-encoding", "UTF-8")

mainClass in assembly := Some("com.kakao.unicorn.ServerMain")

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}