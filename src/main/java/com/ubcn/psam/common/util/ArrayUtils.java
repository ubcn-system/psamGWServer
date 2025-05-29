package com.ubcn.psam.common.util;

import java.util.Arrays;

public class ArrayUtils {

	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	@SafeVarargs
	public static <T> T[] array(T... array) {
		return array;
	}




	@SafeVarargs
	public static <T> T[] merge(T[]... arrays) {
		int index = 0;
		int newLength = 0;
		T[] newArray = null;

		for (T[] array : arrays) {
			if (array != null) {
				newLength += array.length;
			}
		}


		for (T[] array : arrays) {


			if (array != null) {

				if (newArray == null) {

					newArray = Arrays.copyOf(array, newLength);
				} else {

					System.arraycopy(array, 0, newArray, index, array.length);
				}
				index += array.length;
			}
		}
		return newArray;

	}

	public static String toHexString(byte[] array) {
		return toHexString(array, 0, array.length);
	}

	public static String toHexString(byte[] array, int offset, int length) {
		if (array == null) {
			return "";
		}
		StringBuffer buffer;

		try {
			buffer = new StringBuffer(length * 2);
			for (int i = offset; i < offset + length; i++) {
				int j = array[i] & 0xFF;

				buffer.append(hexChars[(j >>> 4)]);
				buffer.append(hexChars[(j & 0xF)]);
			}
			return buffer.toString();
		} finally {
			buffer = null;
		}
	}




	public static byte[] merge(byte[] data, byte[] keyData) {
		// TODO Auto-generated method stub
	 	byte[] newArray = new byte[data.length + keyData.length];

	 	for (int i = 0; i < newArray.length; ++i)
	 	{
	 		newArray[i] = i < data.length ? data[i] : keyData[i - data.length];
	 	}
		return newArray;
	}
}