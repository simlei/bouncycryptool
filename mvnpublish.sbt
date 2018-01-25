// POM settings for Sonatype
homepage := Some(url("https://github.com/simlei/bouncycryptool"))
scmInfo := Some(ScmInfo(url("https://github.com/simlei/bouncycryptool"),
"git@github.com:simlei/bouncycryptool.git"))
developers := List(Developer("Simon Leischnig",
  "simlei",
  "simonjena@gmail.com",
  url("https://github.com/simlei")))
licenses += ("MIT License", url("https://opensource.org/licenses/MIT"))
licenses += ("Eclipse Public License 2.0", url("https://opensource.org/licenses/EPL-2.0"))
publishMavenStyle := true

// Add sonatype repository settings
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)