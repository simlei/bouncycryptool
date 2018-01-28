package org.jcryptool.bouncycastle.core.operation.blocksym;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bouncycastle.crypto.modes.*;
import org.bouncycastle.crypto.BlockCipher;

public enum BlockSymMode {
	
	Default(null),
	CBC(CBCBlockCipher.class),
	CFB(CFBBlockCipher.class),
	//CCM(CCMBlockCipher.class),
	//GCM(GCMBlockCipher.class),
	GCFB(GCFBBlockCipher.class),
	//EAX(EAXBlockCipher.class),
	//OCB(OCBBlockCipher.class),
	OFB(OFBBlockCipher.class),
	SIC(SICBlockCipher.class);
	
	private Class<? extends BlockCipher> modeclazz;
	
	private BlockSymMode(Class<? extends BlockCipher> modeclazz) {
		this.modeclazz = modeclazz;
	}
	
	public BlockCipher applyMode(BlockCipher cipher, int blocklength_bits) {
		if(this.modeclazz == null) return cipher;
		Class<? extends BlockCipher> cl = this.modeclazz;
		
		Constructor[] cs = cl.getConstructors();
		
		for (Constructor constructor : cs) {
			if(constructor.getParameterTypes().length == 2) {
				try {
					return (BlockCipher) constructor.newInstance(new Object[]{cipher, Integer.valueOf(blocklength_bits)});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		for(Constructor constructor: cs) {
			if(constructor.getParameterTypes().length == 1) {
				try {
					return (BlockCipher) constructor.newInstance(new Object[]{cipher});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		
		//TODO: better handling
		throw new RuntimeException("No suitable mode constructor found for mode class " + cl.toString());
	}
}
