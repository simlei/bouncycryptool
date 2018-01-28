package org.jcryptool.bouncycastle.core.operation.data;

import java.nio.charset.Charset;

import org.jcryptool.bouncycastle.core.operation.Input;

public class StringInput implements Input {

	String data;
	Charset charset;
	
	public StringInput(String data, Charset charset) {
		super();
		this.data = data;
		this.charset = charset;
	}

	@Override
	public byte[] read() {
		return data.getBytes(charset);
	}

}
