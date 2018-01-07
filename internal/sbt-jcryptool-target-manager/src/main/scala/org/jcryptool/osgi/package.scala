package org.jcryptool

import sbt._
import sbt.Keys._
import sbt.osgi.manager._

package object osgi {

  object dependencies {
    def bundleDependency(id: String, withSource: Boolean = true, version: String = OSGi.ANY_VERSION): ModuleID = {
      if(withSource) {
        typeP2(OSGi.ECLIPSE_PLUGIN % id % version withSources)
      } else {
        typeP2(OSGi.ECLIPSE_PLUGIN % id % version)
      }
    }
  }

}
