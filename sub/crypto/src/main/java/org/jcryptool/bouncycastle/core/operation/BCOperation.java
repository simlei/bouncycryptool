package org.jcryptool.bouncycastle.core.operation;

import org.bouncycastle.crypto.CryptoException;

public abstract class BCOperation {
	
	private String name;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BCOperation(String name) {
		super();
		this.name = name;
	}

	public abstract byte[] executeOperation(byte[] inData) throws CryptoException;
	
}
