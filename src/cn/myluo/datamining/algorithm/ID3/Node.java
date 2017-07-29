/*
 *    Node.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.ID3;

import java.util.HashMap;
import java.util.Map;

/**
 * The tree node of ID3 algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Node {

	/** the index of previous node. */
	private int m_PreNodeIndex = -1;

	/** the attribute value of previous node to this node. */
	private String m_PreValue;

	/** the index of attribute in this node. */
	private int m_AttributeIndex;

	/** the map of posterior node index. */
	private Map<Object, Integer> m_PostNodeIndex;

	/**
	 * Creates a node.
	 */
	Node() {

		m_PostNodeIndex = new HashMap<Object, Integer>();
	}

	/**
	 * Creates a node with the given index of previous node and attribute value
	 * of previous node to this node.
	 * 
	 * @param preNodeIndex
	 *            the given index of previous node
	 * @param preValue
	 *            the given attribute value of previous node to this node
	 */
	Node(int preNodeIndex, String preValue) {

		this();
		this.m_PreNodeIndex = preNodeIndex;
		this.m_PreValue = preValue;
	}

	/**
	 * Gets the index of previous node.
	 * 
	 * @return the index of previous node
	 */
	int getPreNodeIndex() {

		return m_PreNodeIndex;
	}

	/**
	 * Gets the attribute value of previous node to this node.
	 * 
	 * @return the attribute value of previous node to this node
	 */
	String getPreValue() {

		return m_PreValue;
	}

	/**
	 * Sets the index of attribute in this node with the given index of
	 * attribute.
	 * 
	 * @param attributeIndex
	 *            the given index of attribute
	 */
	void setAttributeIndex(int attributeIndex) {

		this.m_AttributeIndex = attributeIndex;
	}

	/**
	 * Gets the index of attribute in this node.
	 * 
	 * @return the index of attribute in this node
	 */
	int getAttributeIndex() {

		return m_AttributeIndex;
	}

	/**
	 * Adds a posterior node index into the map of posterior node index with the
	 * given value and index.
	 * 
	 * @param value
	 *            the given value
	 * @param index
	 *            the given index
	 */
	void appendPostNodeIndex(Object value, int index) {

		m_PostNodeIndex.put(value, index);
	}

	/**
	 * Gets the map of posterior node index.
	 * 
	 * @return the map of posterior node index
	 */
	Map<Object, Integer> getPostNodeIndex() {

		return m_PostNodeIndex;
	}

	/**
	 * Gets the number of the map of posterior node index.
	 * 
	 * @return the number of the map of posterior node index
	 */
	int numPostNodeIndex() {

		return m_PostNodeIndex.size();
	}

	/**
	 * Returns a description of this node.
	 * 
	 * @return a description of this node as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Node {" + m_PreNodeIndex + "," + m_PreValue + ","
				+ m_AttributeIndex + ",{");
		if (numPostNodeIndex() > 0)
			for (Map.Entry<Object, Integer> entry : m_PostNodeIndex.entrySet())
				sb.append("{" + entry.getKey() + ":" + entry.getValue() + "}");
		sb.append("}}");
		return sb.toString();
	}
}
