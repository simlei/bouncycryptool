package org.jcryptool.bouncycastle.core.operation;

import java.util.Date;

import org.bouncycastle.crypto.CryptoException;

public abstract class OperationExecution {

	private BCOperation op;
	private Input input;
	
	private Date executionDate;
	
	public OperationExecution(BCOperation op, Input input) {
		super();
		this.op = op;
		this.input = input;
	}
	
	public Output execute(Input input) throws CryptoException {
		byte[] in = input.read();
		byte[] out = op.executeOperation(in);
		
		this.executionDate = new Date();
		return makeOutput(out);
	}
	
	public abstract Output makeOutput(byte[] out);

	public byte[] executeComplete() throws CryptoException {
		Output out = execute(input);
		return out.readComplete();
	}
	
	public boolean isExecuted() {
		return this.executionDate != null;
	}
	
	public Date getExecutionDate() {
		return executionDate;
	}
	
	public BCOperation getOp() {
		return op;
	}
	
}
