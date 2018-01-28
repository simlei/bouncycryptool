# SBT-core-only workflow 

One of the most simple workflows in open source is to change code where you know better than the current state, test if it works out, and be done with it. This is exactly what you will find here. With it you will be able to modify: 

 - Cryptographic bindings to the BouncyCastle library and how they are parametrized
 - Change the help and documentation available to the user across different interfaces
 - Really anything that is not specific to the JCrypTool Rich Client Platform plug-in

## Setup

[Make sure to have the required tools installed.](Basic+development+tools.html) Besides Java, that would be git and SBT.

## Change something

Without going into too much detail at this stage of the project, one toy project could be to add a Digest specification to the List of Digests that BouncyCrypTool knows. For a digest, you need only know the Bouncy Castle implementor class in most cases.

How about that! We have implemented the DigestSpec for many algorithms, i.a. Whirlpool, SHA1 and MD5, but we forgot MD4! You can see that this is the case by looking at the ``DigestSpecs.java`` file in a subpath of the ``sub/crypto`` module of BouncyCrypTool: 
[DigestSpec.java](https://github.com/simlei/bouncycryptool/blob/develop/sub/crypto/src/main/java/org/jcryptool/bouncycastle/core/algorithms/DigestSpecs.java)

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

