resolvers += Classpaths.typesafeReleases
resolvers += "oss sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/"

val jctPlugin = ProjectRef(file("./../internal/sbt-jcryptool-manager"), "sbt-jcryptool-manager")
lazy val root = (project in file(".")) dependsOn jctPlugin

//addSbtPlugin("org.jcryptool" % "sbt-jcryptool-manager" % "0.1-SNAPSHOT")
//
//addSbtPlugin("org.digimead" % "sbt-osgi-manager" % "0.3.0.1-SNAPSHOT")
//resolvers += Resolver.url("digimead-ivy", url("http://storage.googleapis.com/ivy.repository.digimead.org/"))(Resolver.defaultIvyPatterns)
//
//resolvers ++= Seq(
//  Classpaths.typesafeReleases,
//  "oss sonatype" at "https://oss.sonatype.org/content/repositories/snapshots/"
//)
//
//// --- old stuff
//
//resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
//
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.2" cross CrossVersion.patch)
//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5" cross CrossVersion.patch)
//addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2" cross CrossVersion.patch)
//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.3" cross CrossVersion.patch)
//
//addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
//addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")
//
///*lazy val root = (project in file(".")) dependsOn (sbtosgimanager, releasehelper)*/
//
//lazy val sbtosgimanager = ProjectRef(file("/home/simon/gitShared/sbt-osgi-manager"), "sbt-osgi-manager")
//lazy val releasehelper = ProjectRef(file("/home/simon/gitShared/opal_hiwi/DEVELOPING_OPAL/plugins/sbt-releasehelper"), "sbt-releasehelper")
//
////addSbtPlugin("org.digimead" % "sbt-osgi-manager" % "0.3.0.1-SNAPSHOT" from s"file:///${JCTDetection.sbtosgijar}")
//addSbtPlugin("org.my-org" % "unmanaged-sbt-plugin" % "0.0.1"
//  from "file:///./lib/unmanaged-sbt-plugin-0.0.1.jar")
//
