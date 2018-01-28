package org.jcryptool.bouncycastle.core.operation.blocksym;

public class BlockSymDefaults {
	
	public static BlockSymConfig makeDefaultCfg(BlockSymSpec spec) {
		return new BlockSymConfig(makeDefaultMode(spec), makeDefaultPadding(spec), makeDefaultBlocksize(spec), makeDefaultIV(spec, makeDefaultBlocksize(spec)));
	}

	private static byte[] makeDefaultIV(BlockSymSpec spec, int blocksize) {
		return new byte[blocksize];
	}

	private static int makeDefaultBlocksize(BlockSymSpec spec) {
		return spec.blocksizes.getAllowedBitsizes()[0];
	}

	private static BlockSymPadding makeDefaultPadding(BlockSymSpec spec) {
		return BlockSymPadding.PKCS7;
	}

	private static BlockSymMode makeDefaultMode(BlockSymSpec spec) {
		return BlockSymMode.CBC;
	}
	
}
