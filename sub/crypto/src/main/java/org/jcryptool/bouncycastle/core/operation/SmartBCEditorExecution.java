package org.jcryptool.bouncycastle.core.operation;

import org.jcryptool.bouncycastle.core.operation.blocksym.BlockSymOperation;
import org.jcryptool.bouncycastle.core.operation.data.UTF8EditorOutput;

public class SmartBCEditorExecution extends OperationExecution {

	public SmartBCEditorExecution(BCOperation op, Input input) {
		super(op, input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Output makeOutput(byte[] out) {
		boolean hex = determineEditorKind();
		return new UTF8EditorOutput(out, hex);
	}

	private boolean determineEditorKind() {
		boolean hex = true;
		BCOperation op = getOp();
		if(op instanceof BlockSymOperation) {
			BlockSymOperation blockSymOperation = (BlockSymOperation) op;
			hex = blockSymOperation.key.encrypt;
		}
		return hex;
	}

}
