package org.jcryptool.bouncycastle.core.algorithms;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.*;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymSpec;
import org.jcryptool.bouncycastle.core.util.AllowedSizesList;
import org.jcryptool.bouncycastle.core.util.AllowedSizesRange;
import org.jcryptool.bouncycastle.core.util.IAllowedSizes;

public enum BsSpecs {
	
	AES(BsSpecs.makeBSS("AES", AESEngine.class, BsSpecs.asl(new int[]{128, 192, 256}), BsSpecs.asrs(128))),
	DES(BsSpecs.makeBSS("DES", DESEngine.class, BsSpecs.asrs(64), BsSpecs.asrs(64))),
	DESede(BsSpecs.makeBSS("DESede", DESedeEngine.class, BsSpecs.asl(new int[]{128, 192}), BsSpecs.asrs(64))),
	Rijndael(BsSpecs.makeBSS("Rijndael", RijndaelEngine.class, BsSpecs.asl(new int[]{128,160,192,224,256}), BsSpecs.asl(new int[]{128, 160, 192, 224, 256}))),
	Blowfish(BsSpecs.makeBSS("Blowfish", BlowfishEngine.class, BsSpecs.asr(0, 448), BsSpecs.asrs(64))),
	Threefish(BsSpecs.makeBSS("Threefish", ThreefishEngine.class, BsSpecs.asl(new int[]{256, 512, 1024}), BsSpecs.asl(new int[]{256, 512, 1024}))),
	Twofish(BsSpecs.makeBSS("Twofish", TwofishEngine.class, BsSpecs.asl(new int[]{128, 192, 256}), BsSpecs.asrs(128))),
	Camellia(BsSpecs.makeBSS("Camellia", CamelliaEngine.class, BsSpecs.asl(new int[]{128, 192, 256}), BsSpecs.asrs(128))),
	Shacal2(BsSpecs.makeBSS("Shacal2", Shacal2Engine.class, BsSpecs.asrs(512), BsSpecs.asrs(256))),
	Serpent(BsSpecs.makeBSS("Serpent", SerpentEngine.class, BsSpecs.asl(new int[]{128, 192, 256}), BsSpecs.asrs(128))),
	IDEA(BsSpecs.makeBSS("IDEA", IDEAEngine.class, BsSpecs.asrs(128), BsSpecs.asrs(64))),
	TEA(BsSpecs.makeBSS("TEA", TEAEngine.class, BsSpecs.asrs(128), BsSpecs.asrs(64))),
	XTEA(BsSpecs.makeBSS("XTEA", XTEAEngine.class, BsSpecs.asrs(128), BsSpecs.asrs(64))),
	SEED(BsSpecs.makeBSS("SEED", SEEDEngine.class, BsSpecs.asrs(128), BsSpecs.asrs(128))),
	Noekeon(BsSpecs.makeBSS("Noekeon", NoekeonEngine.class, BsSpecs.asrs(128), BsSpecs.asrs(128))),
	RC2(BsSpecs.makeBSS("RC2", RC2Engine.class, BsSpecs.asr(0, 1024), BsSpecs.asrs(64))),
	RC532Engine(BsSpecs.makeBSS("RC5/32", RC532Engine.class, BsSpecs.asr(0, 128), BsSpecs.asrs(64))),
	/* RC564Engine(BsSpecs.makeBSS("RC5/64", RC564Engine.class, BsSpecs.asr(0, 128), BsSpecs.asrs(128))), */
	RC6Engine(BsSpecs.makeBSS("RC6", RC6Engine.class, BsSpecs.asr(0, 256), BsSpecs.asrs(128))),
	CAST5(BsSpecs.makeBSS("CAST5", CAST5Engine.class, BsSpecs.asr(0, 128), BsSpecs.asrs(64))),
	CAST6(BsSpecs.makeBSS("CAST6", CAST6Engine.class, BsSpecs.asr(0, 128*2), BsSpecs.asrs(64*2))),
	GOST28147(BsSpecs.makeBSS("GOST28147", GOST28147Engine.class, BsSpecs.asrs(256), BsSpecs.asrs(64)));
	
	
	public final BlockSymSpec spec;
	
	BsSpecs(BlockSymSpec spec) {
		this.spec = spec;
	}




	private static BlockSymSpec makeBSS(String id_name, Class<? extends BlockCipher> engineclass,
			IAllowedSizes allowedKSizes, IAllowedSizes allowedBSizes) {
		return new BlockSymSpec(engineclass, allowedBSizes, allowedKSizes, id_name);
	}

	private static AllowedSizesRange asrs(int i) {
		return BsSpecs.asr(i,i);
	}
	private static AllowedSizesRange asr(int i, int j) {
		return new AllowedSizesRange(i, j, 1);
	}
	
	/**
	 * ifyouknowhatimean ^_~
	 * 
	 * @param is
	 * @return
	 */
	private static IAllowedSizes asl(int[] is) {
		return new AllowedSizesList(is);
	}
	
	
	
	
	
}
