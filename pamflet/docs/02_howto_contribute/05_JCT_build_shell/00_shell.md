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

