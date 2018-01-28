package org.jcryptool.bouncycastle.core.operation.blocksym;

import org.bouncycastle.crypto.BlockCipher;
import org.jcryptool.bouncycastle.core.util.IAllowedSizes;

public class BlockSymSpec {
	
	public Class<? extends BlockCipher> engine;
	public IAllowedSizes blocksizes;
	public IAllowedSizes keysizes;
	public String id_name;
	
	private BlockSymSpec() {
	}
	
	public BlockSymSpec(Class<? extends BlockCipher> engine, IAllowedSizes blocksizes, IAllowedSizes keysizes, String id_name) {
		super();
		this.engine = engine;
		this.blocksizes = blocksizes;
		this.keysizes = keysizes;
		this.id_name = id_name;
	}
	
}
