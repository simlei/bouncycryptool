package scalabc.tools

object JRE {
  def threadnames = Thread.getAllStackTraces.keySet

  def codeSource(clazz: Class[_]) =
    clazz.getProtectionDomain.getCodeSource;

  def classfile(clazz: Class[_]) =
    codeSource(clazz).getLocation.getPath
}