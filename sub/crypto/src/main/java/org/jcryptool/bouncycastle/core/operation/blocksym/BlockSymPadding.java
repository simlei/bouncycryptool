package org.jcryptool.bouncycastle.core.operation.blocksym;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bouncycastle.crypto.paddings.*;
import org.bouncycastle.crypto.BlockCipher;

public enum BlockSymPadding {
	
	PKCS7(PKCS7Padding.class),
	ISO10126d2(ISO10126d2Padding.class),
	X923(X923Padding.class),
	ISO7816d4(ISO7816d4Padding.class),
	ZeroByte(ZeroBytePadding.class);
	
	private Class<? extends BlockCipherPadding> modeclazz;
	
	private BlockSymPadding(Class<? extends BlockCipherPadding> modeclazz) {
		this.modeclazz = modeclazz;
	}
	
	public BlockCipherPadding getPadding() {
		if(this.modeclazz == null) throw new RuntimeException("padding mode class == null");
		
		Class<? extends BlockCipherPadding> cl = this.modeclazz;
		
		Constructor[] cs = cl.getConstructors();
		
		for (Constructor constructor : cs) {
			if(constructor.getParameterTypes().length == 0) {
				try {
					return (BlockCipherPadding) constructor.newInstance(new Object[]{});
				} catch (Exception e) {
					// TODO this should never happen; log this...
					throw new RuntimeException(e);
				}
			}
		}
		
		//TODO: better handling
		throw new RuntimeException("No suitable mode constructor found for padding class " + cl.toString());
	}
}
