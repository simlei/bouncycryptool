package org.jcryptool.bouncycastle.core.algorithms;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.*;
import org.jcryptool.bouncycastle.core.operation.digest.DigestSpec;

public enum DigestSpecs {
	
	Whirlpool(DigestSpecs.makeSpec("Whirlpool", WhirlpoolDigest.class)),
	Tiger(DigestSpecs.makeSpec("Tiger", TigerDigest.class)),
	SHA1(DigestSpecs.makeSpec("SHA1", SHA1Digest.class)),
	SHA224(DigestSpecs.makeSpec("SHA224", SHA224Digest.class)),
	SHA256(DigestSpecs.makeSpec("SHA256", SHA256Digest.class)),
	SHA384(DigestSpecs.makeSpec("SHA384", SHA384Digest.class)),
	SHA512(DigestSpecs.makeSpec("SHA512", SHA512Digest.class)),
	MD2(DigestSpecs.makeSpec("MD2", MD2Digest.class)),
	MD2Digest(DigestSpecs.makeSpec("MD5", MD5Digest.class)),
	MD5(DigestSpecs.makeSpec("RipeMD128", RIPEMD128Digest.class)),
	RipeMD160(DigestSpecs.makeSpec("RipeMD160", RIPEMD160Digest.class)),
	RipeMD256(DigestSpecs.makeSpec("RipeMD256", RIPEMD256Digest.class)),
	RipeMD320(DigestSpecs.makeSpec("RipeMD320", RIPEMD320Digest.class)),
	SM3(DigestSpecs.makeSpec("SM3", SM3Digest.class)),
	GOST3411(DigestSpecs.makeSpec("GOST3411", GOST3411Digest.class));
	
	public final DigestSpec spec;
	
	DigestSpecs(DigestSpec spec) {
		this.spec = spec;
	}

	private static DigestSpec makeSpec(String id_name, Class<? extends Digest> engineclass) {
		return new DigestSpec(engineclass, id_name);
	}

}


// TODO: "forgotten to add" as per tutorial - MD4(DigestSpecs.makeSpec("MD4", MD4Digest.class)),