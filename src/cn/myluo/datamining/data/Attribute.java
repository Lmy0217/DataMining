/*
 *    Attribute.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for handling an attribute.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Attribute {

	/** The attribute name. */
	private String m_Name;

	/** The attribute type. */
	private String n_Type;

	/** The map of attribute values. */
	private Map<String, Integer> n_Values = null;

	/**
	 * Creates an attribute.
	 */
	public Attribute() {

		n_Values = new HashMap<String, Integer>();
	}

	/**
	 * Creates an attributes with the given name and type.
	 * 
	 * @param name
	 *            the given name
	 * @param type
	 *            the given type
	 */
	public Attribute(String name, String type) {

		this();
		this.m_Name = name;
		this.n_Type = type;
	}

	/**
	 * Gets the attribute name.
	 * 
	 * @return the attribute name
	 */
	public String getName() {

		return m_Name;
	}

	/**
	 * Sets the attribute name with the given name.
	 * 
	 * @param name
	 *            the given name
	 */
	public void setName(String name) {

		this.m_Name = name;
	}

	/**
	 * Gets the attribute type.
	 * 
	 * @return the attribute type
	 */
	public String getType() {

		return n_Type;
	}

	/**
	 * Sets the attribute type with the given type.
	 * 
	 * @param type
	 *            the given type
	 */
	public void setType(String type) {

		this.n_Type = type;
	}

	/**
	 * Gets the map of attribute values.
	 * 
	 * @return the map of attribute values
	 */
	public Map<String, Integer> getValues() {

		return n_Values;
	}

	/**
	 * Gets the number of the attribute values.
	 * 
	 * @return the number of the attribute values
	 */
	public int numValues() {

		return n_Values.size();
	}

	/**
	 * Adds a value into the map of attribute values with the given value and
	 * number.
	 * 
	 * @param value
	 *            the given value
	 * @param number
	 *            the number of the given value
	 * @return true if add success, false otherwise.
	 */
	public boolean append(String value, int number) {

		Object obj = n_Values.get(value);
		if (obj == null) {
			n_Values.put(value, number);
		} else {
			n_Values.put(value, (Integer) obj + number);
		}
		return true;
	}

	/**
	 * Returns a description of this attribute.
	 * 
	 * @return a description of this attribute as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Attribute:" + getName() + " type:" + getType() + " values:{");
		for (Map.Entry<String, Integer> entry : n_Values.entrySet()) {
			sb.append("{" + entry.getKey() + ":" + entry.getValue() + "}");
		}
		sb.append("}" + "\n");
		return sb.toString();
	}
}
