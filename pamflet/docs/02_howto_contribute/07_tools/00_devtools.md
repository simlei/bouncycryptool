# Basic development tools

Because all workflows use a common denominator of tools, this section lists them and how to install them. Choose a workflow from one of the previous chapters and use these pages as a reference on where to find and how to install the required tools.

# Required tools

## Java 8

[Java Development Kit version 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Git

Git is for fetching the code from github. On most Linux-based systems, it is already installed. For Windows users, this works quite well](https://git-scm.com/download/win), but really any distribution which works in the standard "cmd" is good. Personally, I have not made the best experience with the GitHub distribution however.

## SBT - Scala Build Tool

- [SBT for Windows](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Windows.html) 
- [SBT for Mac](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Mac.html) 
- [SBT for Linux](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Linux.html)

Optional tools: 

## Maven

[Maven](https://maven.apache.org/download.cgi) is for building JCrypTool and, especially, its target platform so it can be used by SBT to build and test the UI-related parts of the BCT project.

For windows users:
    1) Download binary zip file extract at an installation location (e. g. <USER_HOME>/installations/)
    2) [Add the "bin" subfolder to your environment variables ("Maven in PATH").](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
       (Open Windows System Control (Systemsteuerung) and enter "PATH" in the search field in the upper right of the window)
