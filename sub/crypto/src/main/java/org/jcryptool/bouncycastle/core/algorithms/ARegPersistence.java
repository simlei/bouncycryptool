package org.jcryptool.bouncycastle.core.algorithms;

import java.util.LinkedList;

import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymOperation;
import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymSpec;
import org.jcryptool.bouncycastle.core.operation.digest.DigestSpec;

public class ARegPersistence {

	private LinkedList<BlockSymSpec> specs_blocksym;
	private LinkedList<BlockSymOperation> favorites_blocksym;
	private LinkedList<DigestSpec> specs_digests;
	
	
	private ARegPersistence() {
	}
	
	public ARegPersistence(LinkedList<BlockSymSpec> specs_blocksym,
			LinkedList<BlockSymOperation> favorites_blocksym,
			LinkedList<DigestSpec> specs_digests) {
		super();
		this.specs_blocksym = specs_blocksym;
		this.favorites_blocksym = favorites_blocksym;
		this.specs_digests = specs_digests;
	}

	public AlgorithmReg makeReg() {
		return new AlgorithmReg(specs_blocksym, specs_digests, favorites_blocksym);
	}
	
	
	
}
