package com.ubcn.psam.common;

import java.io.Serializable;

public abstract class AbstractModel implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7446261606993216508L;
	public static final String YES = "Y";
	public static final String NO = "N";

	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}
}
