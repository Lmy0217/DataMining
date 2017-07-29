/*
 *    Instance.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling an instance.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Instance implements Comparable<Instance>, Cloneable {

	/** The instance values. */
	private Map<String, Object> m_Values = null;

	/**
	 * Creates an instance.
	 */
	public Instance() {

		m_Values = new HashMap<String, Object>();
	}

	/**
	 * Gets the instance values.
	 * 
	 * @return the instance values
	 */
	public Map<String, Object> getValues() {

		return m_Values;
	}

	/**
	 * Gets the number of the instance values.
	 * 
	 * @return the number of the instance values
	 */
	public int numValues() {

		return m_Values.size();
	}

	/**
	 * Adds a value into the instance values with the given attribute and value.
	 * 
	 * @param attribute
	 *            the given attribute
	 * @param value
	 *            the value of the given attribute
	 * @return true if add success, false otherwise.
	 */
	public boolean append(String attribute, Object value) {

		m_Values.put(attribute, value);
		return true;
	}

	/**
	 * Compares two instances on the same attributes.
	 * 
	 * @param instance
	 *            the given instance
	 * @return the value 0 if two instances are difference, the value 1 if two
	 *         instances are same.
	 */
	public int compareTo(Instance instance) {

		if (instance.numValues() != numValues())
			return 0;
		if (numValues() == 0)
			return 1;
		int result = 1;
		for (Map.Entry<String, Object> entry : instance.getValues().entrySet()) {
			if (!getValues().get(entry.getKey()).equals(entry.getValue())) {
				result = 0;
				break;
			}
		}
		return result;
	}

	/**
	 * Clones this instance.
	 * 
	 * @return the copy of this instance
	 */
	public Instance clone() {

		Instance instance = new Instance();
		if (numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Values.entrySet()) {
				instance.append(entry.getKey(), entry.getValue());
			}
		}
		return instance;
	}

	/**
	 * Returns a description of this instance.
	 * 
	 * @return a description of this instance as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Instance {");
		for (Map.Entry<String, Object> entry : m_Values.entrySet()) {
			sb.append("{" + entry.getKey() + ":" + entry.getValue() + "}");
		}
		sb.append("}" + "\n");
		return sb.toString();
	}
}
