# Project structure of BCT

The stretch between a pure Library and a full-featured Eclipse Rich Client Application makes this project multi-faceted.

## Facets

 - Cryptographic core: bindings to Bouncy Castle and abstractions over the cryptographic domain (bytes arrays, I/O streams and operations, identities, keys).

 - Interfaces/logic core: model of the operations w.r.t. user experience in the form of a Domain Specific Language that knows how to provide help content about domain operations and objects, knows how they can be plugged into each other and thus, basic autocompletion.

 - JCrypTool RCP Plug-in: a Rich Client Platform plug-in to JCrypTool, basically an Eclipse OSGi bundle developed with the Eclipse Plug-in development environment tools ("Eclipse PDE")
 - a standalone command line shell
