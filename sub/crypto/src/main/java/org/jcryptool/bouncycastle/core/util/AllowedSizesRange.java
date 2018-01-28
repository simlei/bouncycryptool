package org.jcryptool.bouncycastle.core.util;

import java.util.LinkedList;
import java.util.List;

public class AllowedSizesRange implements IAllowedSizes {

	private int start;
	private int end;
	private int divisibleby = 1;
	
	private AllowedSizesRange() {
	}
	
	public AllowedSizesRange(int start, int end, int divisibleby) {
		super();
		this.start = start;
		this.end = end;
		this.divisibleby = divisibleby;
	}

	@Override
	public int[] getAllowedBitsizes() {
		LinkedList<Integer> resultList = new LinkedList<Integer>();
		for(int i=start; i<=end; i=i+1) {
			if(i % divisibleby == 0) {
				resultList.add(i);
			}
		}
		int[] r = new int[resultList.size()];
		for (int j = 0; j < resultList.size(); j++) {
			Integer i = resultList.get(j);
			r[j] = i;
		}
		return r;
	}
	
	@Override
	public String toString() {
		if(divisibleby > 1) {
			return String.format("%d..%d..%d", start, divisibleby, end);
		} else {
			return String.format("%d..%d", start, end);
		}
	}
	
	
}
