//addSbtPlugin("org.digimead" % "sbt-osgi-manager" % "0.3.0.1-SNAPSHOT")
//resolvers += "digimead-maven" at "http://storage.googleapis.com/maven.repository.digimorg/"
//resolvers += Resolver.url("digimead-ivy", url("http://storage.googleapis.com/ivy.repository.digimead.org/"))(Resolver.defaultIvyPatterns)
//
//resolvers ++= Seq(
//  Classpaths.typesafeReleases,
//  "oss sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/"
//)

val jctTargetPlugin = ProjectRef(file("./../../sbt-jcryptool-target-manager"), "sbt-jcryptool-target-manager")
lazy val root = (project in file(".")) dependsOn jctTargetPlugin
addSbtPlugin("org.jcryptool" % "sbt-jcryptool-target-manager" % "0.1.1-SNAPSHOT")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")