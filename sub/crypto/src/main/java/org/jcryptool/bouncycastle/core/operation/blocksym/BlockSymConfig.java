package org.jcryptool.bouncycastle.core.operation.blocksym;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

public class BlockSymConfig {
	
	public BlockSymMode mode;
	public BlockSymPadding padding;
	public Integer blocksize_bits;
	public byte[] iv;
	
	private BlockSymConfig() {
		
	}

	public BlockSymConfig(BlockSymMode mode, BlockSymPadding padding,
			int blocksize_bits, byte[] iv) {
		super();
		this.mode = mode;
		this.padding = padding;
		this.blocksize_bits = blocksize_bits;
		
		//TODO: check that iv is of blocksize
		this.iv = new byte[blocksize_bits/8];
		System.arraycopy(iv, 0, this.iv, 0, blocksize_bits/8);
	}

}
