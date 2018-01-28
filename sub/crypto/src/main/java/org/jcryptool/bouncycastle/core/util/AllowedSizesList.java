package org.jcryptool.bouncycastle.core.util;

import java.util.Arrays;
import java.util.List;

public class AllowedSizesList implements IAllowedSizes {

	int[] sizes;
	
	private AllowedSizesList() {
	}
	
	public AllowedSizesList(int[] sizes) {
		super();
		this.sizes = sizes;
	}

	@Override
	public int[] getAllowedBitsizes() {
		return this.sizes;
	}

	@Override
	public String toString() {
		return String.format("%s", Arrays.toString(sizes));
	}
	
}
