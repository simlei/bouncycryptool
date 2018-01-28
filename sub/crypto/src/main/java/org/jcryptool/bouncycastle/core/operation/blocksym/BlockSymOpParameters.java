package org.jcryptool.bouncycastle.core.operation.blocksym;

public class BlockSymOpParameters {
	
	public byte[] data;
	public Boolean encrypt;

	private BlockSymOpParameters() {
	}
	
	public BlockSymOpParameters(byte[] key, Boolean encrypt) {
		super();
		this.data = key;
		this.encrypt = encrypt;
	}
	
}
