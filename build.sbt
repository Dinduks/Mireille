scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.14" % "test",
    "commons-io" % "commons-io" % "2.4"
)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")
