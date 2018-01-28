package org.jcryptool.bouncycastle.core.operation.digest;

import java.lang.reflect.Constructor;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.EncodableDigest;
import org.bouncycastle.util.encoders.Hex;
import org.jcryptool.bouncycastle.core.operation.BCOperation;


public class DigestOperation extends BCOperation {

	//TODO: !rushed, check digest-op-related
	
	public DigestSpec spec;
	
	private DigestOperation() {
		this(null);
	}
	
	public DigestOperation(DigestSpec spec) {
		super(spec==null?null: spec.id_name);
		this.spec = spec;
	}
	
	private Digest constructEngine() {
		if(this.spec.engine == null) throw new RuntimeException("digest engine class == null");
			
		Class<? extends Digest> cl = this.spec.engine;
		
		Constructor[] cs = cl.getConstructors();
		
		for(Constructor constructor: cs) {
			if(constructor.getParameterTypes().length == 0) {
				try {
					return (Digest) constructor.newInstance(new Object[]{});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		
		//TODO: better handling
		throw new RuntimeException("No suitable engine constructor found for engine class " + cl.toString() + ", spec id = " + this.spec.id_name);
	}

	@Override
	public byte[] executeOperation(byte[] inData) throws CryptoException {
		Digest digest = constructEngine();
		
		byte[] resBuf = new byte[digest.getDigestSize()];
	    
        byte[] m = inData;
        digest.update(m, 0, m.length);
        digest.doFinal(resBuf, 0);
        
        return resBuf;
	}

}
