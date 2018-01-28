package org.jcryptool.bouncycastle.core.operation.data;

import java.nio.charset.Charset;

import org.jcryptool.bouncycastle.core.operation.Input;

public class UTF8StringInput extends StringInput {

	public UTF8StringInput(String data) {
		super(data, Charset.forName("UTF-8"));
	}

}
