/*
 *    DataSet.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves the data set from an TXT file.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class DataSet {

	/** The data set name. */
	private String m_Name;

	/** The list of attributes. */
	private List<Attribute> m_Attributes;

	/** The list of instances. */
	private List<Instance> m_Instances;

	/**
	 * Creates a data set.
	 */
	public DataSet() {

		m_Attributes = new ArrayList<Attribute>();
		m_Instances = new ArrayList<Instance>();
	}

	/**
	 * Creates a data set with the given name.
	 * 
	 * @param name the given name
	 */
	public DataSet(String name) {

		this();
		this.m_Name = name;
	}

	/**
	 * Gets the data set name.
	 * 
	 * @return the data set name
	 */
	public String getName() {

		return m_Name;
	}

	/**
	 * Sets the data set name with the given name.
	 * 
	 * @param name
	 *            the given name
	 */
	public void setName(String name) {

		this.m_Name = name;
	}

	/**
	 * Gets the list of attributes.
	 * 
	 * @return the list of attributes
	 */
	public List<Attribute> getAttributes() {

		return m_Attributes;
	}

	/**
	 * Gets the number of attributes.
	 * 
	 * @return the number of attributes
	 */
	public int numAttributes() {

		return m_Attributes.size();
	}

	/**
	 * Gets the list of instances.
	 * 
	 * @return the list of instances
	 */
	public List<Instance> getInstances() {

		return m_Instances;
	}

	/**
	 * Gets the number of instances.
	 * 
	 * @return the number of instances
	 */
	public int numInstances() {

		return m_Instances.size();
	}

	/**
	 * Adds the given attribute into the list of attributes.
	 * 
	 * @param attribute
	 *            the given attribute
	 * @return true if add success, false otherwise.
	 */
	public boolean appendAttribute(Attribute attribute) {

		if (attribute == null)
			return false;

		m_Attributes.add(attribute);
		return true;
	}

	/**
	 * Adds the given instance into the list of instances.
	 * 
	 * @param instance
	 *            the given instance
	 * @return true if add success, false otherwise.
	 */
	public boolean appendInstance(Instance instance) {

		if (instance == null)
			return false;

		m_Instances.add(instance);
		return true;
	}

	/**
	 * Gets an abstract of this data set.
	 * 
	 * @return a abstract of this data set as a string
	 */
	public String getAbstract() {

		StringBuilder sb = new StringBuilder();
		sb.append("数据集:       " + getName() + "\n");
		sb.append("数据:         " + numInstances() + "\n");
		sb.append("属性:         " + numAttributes() + "\n");
		for (Attribute a : getAttributes()) {
			sb.append("              " + a.getName() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Returns a description of this data set.
	 * 
	 * @return a description of this data set as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("DataSet:" + getName() + "\n");
		for (Attribute a : getAttributes()) {
			sb.append(a.toString());
		}
		for (Instance i : getInstances()) {
			sb.append(i.toString());
		}
		return sb.toString();
	}
}
