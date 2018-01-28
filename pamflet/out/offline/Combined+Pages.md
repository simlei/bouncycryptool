
# BouncyCrypTool for developers

Welcome to the BouncyCrypTool documentation for developers! 

## What does the name mean?

"BouncyCrypTool" aims to give the [Bouncy Castle](https://www.bouncycastle.org/) cryptographic library an interface (or really: multiple interfaces) that are as easy to use for end users and developers alike, and extend the [JCrypTool](cryptool.org/en/jcryptool) project as a means to provide a user interface.

 - For developers, that means an API, written in [Scala](http://www.scala-lang.org/), usable by Scala and Java applications alike

 - For end-users, it offers both a command-line interface as well as a graphical user interface. JCT is an Eclipse Rich Client Platform project. BCT provides a plug-in to integrate its user interface functionality to the already mature cryptographic environment that is JCrypTool.

# Project structure of BCT

The stretch between a pure Library and a full-featured Eclipse Rich Client Application makes this project multi-faceted.

## Facets

 - Cryptographic core: bindings to Bouncy Castle and abstractions over the cryptographic domain (bytes arrays, I/O streams and operations, identities, keys).

 - Interfaces/logic core: model of the operations w.r.t. user experience in the form of a Domain Specific Language that knows how to provide help content about domain operations and objects, knows how they can be plugged into each other and thus, basic autocompletion.

 - JCrypTool RCP Plug-in: a Rich Client Platform plug-in to JCrypTool, basically an Eclipse OSGi bundle developed with the Eclipse Plug-in development environment tools ("Eclipse PDE")
 - a standalone command line shell

## Directory structure

(// TODO: relate the subfolders in BCT and the JCT core and crypto repositories as well as the maven coordinates to the facets of the project)

## How to contribute

There are be various ways to contribute to BCT, as with any Open-Source project. Besides testing and reporting issues, to contribute code to those facets you need the necessary development tools to test your code. You won't need to set up all the development tools for those facets all at once:

 - In the simplest case w.r.t. the development process involved, you would want improve on the cryptographic routines that Bouncy Castle provides and that BCT uses, or on the documentation. This is called the "Core-only workflow".

 - Going more complex, if you want to contribute to the BCT core as well as to the JCrypTool plug-in and graphical interface, that's great -- this is the "full setup" though. You would need to set up the Eclipse Plug-in development environment for JCrypTool additionally to the BCT-required setup. We have taken care of some hard stuff with simple automations for you already. We call this the "workflow with UI and eclipse PDE".

# SBT-core-only workflow 

One of the most simple workflows in open source is to change code where you know better than the current state, test if it works out, and be done with it. This is exactly what you will find here. With it you will be able to modify: 

 - Cryptographic bindings to the BouncyCastle library and how they are parametrized
 - Change the help and documentation available to the user across different interfaces
 - Really anything that is not specific to the JCrypTool Rich Client Platform plug-in

## Setup

[Make sure to have the required tools installed.](Basic+development+tools.html) Besides Java, that would be git and SBT.

## Change something

Without going into too much detail at this stage of the project, one toy project could be to add a Digest specification to the List of Digests that BouncyCrypTool knows. For a digest, you need only know the Bouncy Castle implementor class in most cases.

How about that! We have implemented the DigestSpec for many algorithms, i.a. Whirlpool, SHA1 and MD5, but we forgot MD4! You can see that this is the case by looking at the ``DigestSpecs.java`` file in a subpath of the ``sub/crypto`` module of BouncyCrypTool: [DigestSpec.java](https://github.com/simlei/bouncycryptool/blob/develop/sub/crypto/src/main/java/org/jcryptool/bouncycastle/core/algorithms/DigestSpecs.java)

You can improve our list of specifications by simply adding this item to the specs list in the file in your local clone:

```
MD4(DigestSpecs.makeSpec("MD4", MD4Digest.class))
```

After that, verify that our automated tests picked that up correctly, by typing in your command line in the ``bouncycryptool`` folder:

    sbt test

	This runs the full test suite of bouncycryptool. You should see somewhere in there: 
	[info] DigestRegressionTest:
	[info] The MD5 implementation of the Bouncy Castle Library
	'Helloworld' --md5--> 'A165968B0A8084A041AED89BF40D581F'
	[info] - should work to prove we define the specs the right way
	[info] All digests known to this project
	'Helloworld' --Whirlpool--> '267A171AD9394F062654C8466B3260F58C0CB940D7CE8017EA74885AE00A376D4D706608F91DBDEAEAA870984FBEAB7B5D27DB104792DF468B21C3CB2F9B3BFF'
	'Helloworld' --Tiger--> '8C7942899145B653A61E496D63C8E5E0F837E12D1EDF2675'
	'Helloworld' --SHA1--> '1C3C3FA0A32ABF3473A3E88F07A377025E28C03E'
	'Helloworld' --SHA224--> '720B7DEAE2414FE44BFCC8030E770C4F8C275AA72E85A9AFFBF9D71B'
	'Helloworld' --SHA256--> '5AB92FF2E9E8E609398A36733C057E4903AC6643C646FBD9AB12D0F6234C8DAF'
	'Helloworld' --SHA384--> '0B3C94419A1795723020ECB7D75D7EC0201BD133094F8C1FCCE0F03DDC42D9793B7E4B968DD22D8736470ED1117CF07F'
	'Helloworld' --SHA512--> '98B57E17FD890C8CD2ABFAA8A180F7BEC1E3D662A7F5DCDAC9B69942865B9816DC5C747FC57EF24BA1323B8C8E3700AF6FE97497F92EB656E33D408361679AA4'
	'Helloworld' --MD2--> '581CDC10959CE3C1DF6BE7EBF66CAE08'
	'Helloworld' --MD4--> '541032988D1C6A0D1F194347AC51D692'
	'Helloworld' --MD5--> 'A165968B0A8084A041AED89BF40D581F'
	'Helloworld' --RipeMD128--> 'E31FA3410AD9F58FB00CF8CD3AB00211'
	'Helloworld' --RipeMD160--> '6A012EC2CEF81116AD28A5E668EC57DC3CDEAFDE'
	'Helloworld' --RipeMD256--> 'A388A6EE736F6D0480F96221DA17913D4CE4DC18E725F204803617381BB35E4A'
	'Helloworld' --RipeMD320--> 'FC81A3916A21CE2726FA1C1EB5D66C9C52F2562A95D12D1FC7C042BD4833A8DF2F29C08E45947D7C'
	'Helloworld' --SM3--> '64ED271D3F5A1211D823E3A52DC544294AEB72B338FE833BE1F968A2166A328D'
	'Helloworld' --GOST3411--> '1ED49EBF1F7C94079D951BEAB5AEA04FF1B2569AD047C34998C2C9482274309D'
	[info] - should be able to digest 'Helloworld' as toy aggregation test



As you can see, your change was picked up as we can see a successful test involving MD4 in the output. 

## Moving on

You can go ahead now, commit and make a pull request. Thanks!


# Workflow with UI and eclipse PDE

TODO: detail everyhting as soon as JCT PDE binaries are properly published with Maven central and it works stable aross setups.

# BouncyCrypTool Build shell - for advanced users

You need to have [all the tools](Basic+development+tools.html) installed.

## Setting up

Clone the JCrypTool repositories. We assume a root folder "JCT"; you are required to clone all three repositories next to each other.
This should work: 

```
cd path/to/JCT
git clone https://github.com/simlei/bouncycryptool.git
git clone https://github.com/jcryptool/core.git
git clone https://github.com/jcryptool/crypto.git
```

After that, it is time to fire up the BouncyCrypTool JCT build console for the first time :)

```
cd bouncycryptool
sbt startJCTConsole
```

This should take a while if you had never installed sbt before -- it does a lot of heavy lifting just for you:
 - fetch the correct SBT version, 
 - fetch the correct Scala version, 
 - automatically resolve libraries needed by the project, 
 - compile the console, and
 - finally start it up :)
 
In case everything worked, you should see, after a while, a welcome banner, and a "scala > " prompt.
For some operating systems, the prompt is not directly visible; if you suspect that is the case, just type a blank. 

If you see something like this, it works, and you can potentially say goodbye to any shell scripts, 
third-party program routines and everything, the BouncyCrypTool shell can take care of all that in the future! (With just a little bit of coding required, and it is fun coding since it is Scala :) )

```

        =========================================================
        =   Welcome to the JCrypTool project / build console!   =
        =========================================================

You are in a Scala console here! Everything is a Scala expression.
That means you can actually learn something about this language here.
It is kind of like Java, with more powerful syntax.

Your main entry point will be the object "jct".
jct.this, jct.that, jct.[...].api.command(), you get the idea.

To get started:

- Try evaluating pure Scala code (at the basic level it is like Java)
    e.g. scala> 4+4
         scala> println("Helloworld!")

- Try working with Scala and the JCrypTool model's file structure:
    e.g. scala> jct.projects.core.dir // shows the location of the core repository
    e.g. scala> (jct.projects.core.dir ** "*.java").get.take(20).foreach(println) // first 20 java files found in "core"
    e.g. scala> val helloFile = jct.projects.core.dir.getParentFile / "hello.txt"
                IO.write(helloFile, "Hello, world!")
                println(s"$helloFile reads: " + IO.read(helloFile))
                IO.delete(helloFile)

- Try building JCrypTool (like a weekly build)
    e.g. scala> jct.projects.core.api_build.buildJCT(jct.projects.core.dir / ".." / "myWeeklyBuild")
                (the '/' is perfectly valid Scala code in this context for navigating folders)

- Read more help and how to navigate here:
         scala> jct.help

At any point, press the TAB key to see auto-completions.
Online presence: https://github.com/simlei/bouncycryptool/

To quit the console, use the "exit" command.

Have fun!

scala> 
```

## Trying it out

You are now all set. You should see a "scala> " prompt waiting for you to give commands to the console. 
Unfortunately, the documentation comes to an ends for now (but not for long!)

A few instructionary tips for poking around on your own:

Try out the snippets in the welcome message first to get a feeling. 
Many important objects have got a ".help" member - type it in to get help :)
Especially APIs and their commands have those helpers; here is an incomplete list: 

```dtd
jct.help
jct.api.help
jct.projects.core.api_build.help
jct.projects.core.api_build.buildJCT.help //this is the help for the JCT build command; [...].buildJCT() would execute it
jct.projects.bouncycryptool.projects.jctPlatformExtractor.api.help
[...]
```

The first snippet -- jct.help -- leads to more identifiers to try out, especially, the 
command APIs. When you get there, these are the "crown jewels" of the console: 

```
// builds JCrypTool to the defaullocation, takes that built product and resolves
// and publishes it to the Scala/BouncyCrypTool world. This takes quite some time.
jct.api.buildResolveAndPublish()  

// step 2 from the operation above, but does not require step 1 as it uses a snapshot off the web
val p2WebUrl = "http://simlei.github.io/jcryptool-p2/"
jct.projects.bouncycryptool.projects.jctPlatformExtractor.api.onWebHostedBuild.resolveAndPublish(p2WebUrl) 

// build your own "weekly snapshot" of jcryptool!
val myTargetDirectory = jct.projects.core.dir / ".." / "myWeeklyBuild"  // or just [...] = file("C:\Users\<...>")
jct.projects.core.api_build.buildJCT(myTargetDirectory)

``` 

Those long identifiers above are only for educational purpose. As jct.help will tell you, 
there are lots of shortcuts to the projects and APIs to not be required to type all that much.

By the way, the web address [http://simlei.github.io/jcryptool-p2/](http://simlei.github.io/jcryptool-p2/)
from above can actually be visited. It contains a snapshot of the JCT p2 update page. 
This provisory github address will be moved to a JCT-internal location sooner or later, though.

There is so much more to talk about, how this stuff works, the design descisions behind it, shortcomings, strengths etc.

I like to show an exemplary detail of the implementation for one of the strengths of scala with complex projects: 
The help system in this console is implemented in [just one file](https://github.com/simlei/bouncycryptool/blob/master/internal/sbt-jcryptool-manager/src/main/scala/org/jcryptool/consolehelp/ExternalHelp.scala)! 
One can see, that the help definitions are all "implicit". What this means is, that they are provided as kind of a 
sea of context objects which are picked up dynamically, rather than polluting the codebase. It is also a very extensible system; to provide a ".help" 
member to any of the objects in the console, one needs only to add another entry in the ExternalHelp.scala file. We see such extensibility
as one of the reasons to go for Scala in the integration of the bouncycryptool library.

## If you start again later...

Just open your terminal, enter ``cd path/to/JCT`` from earlier (__"Setting up"__) and enter ``sbt startJCTConsole``. 
The console should spin up, just much faster this time.


# Basic development tools

Because all workflows use a common denominator of tools, this section lists them and how to install them. Choose a workflow from one of the previous chapters and use these pages as a reference on where to find and how to install the required tools.

# Required tools

## Java 8

[Java Development Kit version 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Git

Git is for fetching the code from github. On most Linux-based systems, it is already installed. [For Windows users, this works quite well](https://git-scm.com/download/win), but really any distribution which works in the standard "cmd" is good. Personally, I have not made the best experience with the GitHub distribution however.

## SBT - Scala Build Tool

- [SBT for Windows](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Windows.html) 
- [SBT for Mac](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Mac.html) 
- [SBT for Linux](http://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Linux.html)

# Optional tools: 

## Maven

[Maven](https://maven.apache.org/download.cgi) is for building JCrypTool and, especially, its target platform so it can be used by SBT to build and test the UI-related parts of the BCT project.

For windows users:
    1) Download binary zip file extract at an installation location (e. g. <USER_HOME>/installations/)
    2) [Add the "bin" subfolder to your environment variables ("Maven in PATH").](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
       (Open Windows System Control (Systemsteuerung) and enter "PATH" in the search field in the upper right of the window)
