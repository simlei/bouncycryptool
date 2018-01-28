package org.jcryptool.bouncycastle.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class Bytes {
	
	public static String makeString(byte[] data) {
		return new String(data, Charset.forName("UTF-8"));
	}
	
	public static byte[] makeBytes(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s.getBytes();
		}
	}
	
	public static byte[] resize(byte[] data, int newL) {
		byte[] result = new byte[newL];
		System.arraycopy(data, 0, result, 0, Math.min(newL, data.length));
		return result;
	}
	
	public static int[] filterAllowedSizes(int[] sizes, int lowest, int divisibleBy) {
		List<Integer> resultList = new LinkedList<Integer>();
		for(int size: sizes) {
			if(size >= lowest && size % divisibleBy == 0) {
				resultList.add(size);
			}
		}
		int[] result = new int[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			Integer size = resultList.get(i);
			result[i] = size;
		}
		return result;
	}
	
}
