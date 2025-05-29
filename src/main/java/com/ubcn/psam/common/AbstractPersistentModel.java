package com.ubcn.psam.common;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ubcn.psam.common.util.AssertUtils;


public abstract class AbstractPersistentModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6791752387127516150L;
//	public static final int STATE_UNKNOWN = Integer.MIN_VALUE;
//	public static final int STATE_DISUSED = 0;
//	public static final int STATE_NORMAL = 1;
//	public static final String REG_DATE = "reg_date";
//	public static final String MOD_DATE = "mod_date";
//	public static final String STATE = "state";
//	public static final String ROWNUM = "rownum__";


	private Map<String, Object> attributes = new LinkedHashMap<String, Object>();


//	protected String getEntityNamePrefix() {
//		return "tb_";
//	}
//
//	public String getEntityNameOfPersistentModel() {
//		return getEntityNamePrefix() + getClass().getSimpleName().toLowerCase();
//	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String attributeName) {
		return (T) this.attributes.get(attributeName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String attributeName, T defaultValue) {
		Object attributeValue = this.attributes.get(attributeName);

		return    attributeValue == null ? defaultValue : (T) attributeValue;
	}

	public <T> void setAttribute(String attributeName, T attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	public <T> AbstractPersistentModel addAttribute(String attributeName, T attributeValue) {
		AssertUtils.assertNotEmpty(attributeName, "attribteName");

		this.attributes.put(attributeName, attributeValue);

		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(String attributeName) {
		return (T) this.attributes.remove(attributeName);
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes.clear();
		if (attributes == null) {
			return;
		}
		this.attributes.putAll(attributes);
	}

	public AbstractPersistentModel addAttributes(Map<String, Object> attributes) {
		if (attributes == null) {
			return this;
		}
		this.attributes.putAll(attributes);

		return this;
	}

	public AbstractPersistentModel mergeAttributes(Map<String, Object> attributes) {
		if (attributes == null) {
			return this;
		}
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			if (!this.attributes.containsKey(entry.getKey())) {
				this.attributes.put(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	public AbstractPersistentModel clearAttributes() {
		this.attributes.clear();

		return this;
	}

	public Timestamp getRegDate() {
		return (Timestamp) getAttribute("reg_date");
	}

	protected void setRegDate(Timestamp regDate) {
		setAttribute("reg_date", regDate);
	}

	public Timestamp getModDate() {
		return (Timestamp) getAttribute("mod_date");
	}

	protected void setModDate(Timestamp modDate) {
		setAttribute("mod_date", modDate);
	}

	public int getState() {
		return ((Integer) getAttribute("state", Integer.valueOf(Integer.MIN_VALUE))).intValue();
	}

	public void setState(int state) {
		setAttribute("state", Integer.valueOf(state));
	}




	public AbstractPersistentModel newInstance() {
		try {
			return (AbstractPersistentModel) getClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
		} catch (Exception ex) {
			if ((ex instanceof RuntimeException)) {
				throw ((RuntimeException) ex);
			}
			throw new RuntimeException(ex);
		}
	}


}
