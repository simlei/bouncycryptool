package org.jcryptool.bouncycastle.core.operation.digest;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;

public class DigestSpec {
	
	public Class<? extends Digest> engine;
	public String id_name;
	
	private DigestSpec() {
	}
	
	public DigestSpec(Class<? extends Digest> engine, String id_name) {
		super();
		this.engine = engine;
		this.id_name = id_name;
	}
	
}
