/*
 *    Train.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.ID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.myluo.datamining.data.Attribute;
import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.data.Instance;

/**
 * Class for train with ID3 algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Train {

	/** the train set. */
	private DataSet m_Trainset;

	/** the decision tree. */
	private List<Node> m_Tree;

	/**
	 * Creates a Train class.
	 */
	private Train() {

		m_Tree = new ArrayList<Node>();
		// Adds root node
		m_Tree.add(new Node());
	}

	/**
	 * Creates a Train class with the given train set.
	 * 
	 * @param trainset
	 *            the given train set
	 */
	public Train(DataSet trainset) {

		this();
		this.m_Trainset = trainset;
		// Training at root.
		train(0);
	}

	/**
	 * Gets the decision tree.
	 * 
	 * @return the decision tree
	 */
	public List<Node> getTree() {

		return m_Tree;
	}

	/**
	 * Training decision tree with ID3 algorithm at the given index.
	 * 
	 * @param index
	 *            the given index
	 */
	private void train(int index) {

		// checks if all instances in this node belongs one class.
		if (entropy(index, null) == 0.0) {
			// creates leaf node.
			m_Tree.get(index).setAttributeIndex(m_Trainset.numAttributes() - 1);
			m_Tree.get(index).appendPostNodeIndex(getClass(index), index);
		} else {
			// unused attributes.
			List<Integer> attributeIndexs = getAttributeIndexs(index);
			if (attributeIndexs.size() == 0)
				return;
			// the max gain.
			double max = -1;
			int attributeIndex = -1;
			// find the max gain.
			for (int i = 0; i < attributeIndexs.size(); i++) {
				double g = gain(index, attributeIndexs.get(i));
				if (g > max) {
					max = g;
					attributeIndex = attributeIndexs.get(i);
				}
			}
			// sets the index of attribute in this node with the max gain.
			m_Tree.get(index).setAttributeIndex(attributeIndex);
			// creates child node.
			for (Map.Entry<String, Integer> entry : m_Trainset.getAttributes()
					.get(attributeIndex).getValues().entrySet()) {
				m_Tree.add(new Node(index, entry.getKey()));
				m_Tree.get(index).appendPostNodeIndex(entry.getKey(),
						m_Tree.size() - 1);
				// recursion call this function at child node
				train(m_Tree.size() - 1);
			}
		}
	}

	/**
	 * Gets instances class if the instances at the given index of the decision
	 * tree belongs one class.
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @return the instances class
	 */
	private Object getClass(int index) {

		// backtracking to root to gets the used attributes.
		Map<String, String> values = new HashMap<String, String>();
		while (m_Tree.get(index).getPreNodeIndex() != -1) {
			String value = m_Tree.get(index).getPreValue();
			index = m_Tree.get(index).getPreNodeIndex();
			String attribute = m_Trainset.getAttributes()
					.get(m_Tree.get(index).getAttributeIndex()).getName();
			values.put(attribute, value);
		}
		Object c = null;
		for (Instance instance : m_Trainset.getInstances()) {
			c = instance.getValues().get(
					m_Trainset.getAttributes()
							.get(m_Trainset.numAttributes() - 1).getName());
			boolean flag = true;
			if (values.size() > 0) {
				for (Map.Entry<String, String> entry : values.entrySet()) {
					if (!instance.getValues().get(entry.getKey())
							.equals(entry.getValue())) {
						flag = false;
						break;
					}
				}
			}
			if (flag)
				break;
		}
		return c;
	}

	/**
	 * Gets the unused attributes' index at the given index of the decision tree
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @return the unused attributes' index at the given index of the decision
	 *         tree
	 */
	private List<Integer> getAttributeIndexs(int index) {

		// backtracking to root to gets the unused attributes' index.
		List<Integer> attributeIndexs = new ArrayList<Integer>();
		for (int i = 0; i < m_Trainset.numAttributes() - 1; i++) {
			attributeIndexs.add(i);
		}
		while (m_Tree.get(index).getPreNodeIndex() != -1) {
			index = m_Tree.get(index).getPreNodeIndex();
			attributeIndexs.remove((Object) m_Tree.get(index)
					.getAttributeIndex());
		}
		return attributeIndexs;
	}

	/**
	 * Computes the count of the instances at the given index of the decision
	 * tree and other condition
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @param other
	 *            the given other condition
	 * @return the count of the instances at the given index of the decision
	 *         tree and other condition
	 */
	private int count(int index, Map<String, String> other) {

		// backtracking to root to gets the used attributes' index.
		Map<String, String> values = new HashMap<String, String>();
		if (other != null)
			values.putAll(other);
		while (m_Tree.get(index).getPreNodeIndex() != -1) {
			String value = m_Tree.get(index).getPreValue();
			index = m_Tree.get(index).getPreNodeIndex();
			String attribute = m_Trainset.getAttributes()
					.get(m_Tree.get(index).getAttributeIndex()).getName();
			values.put(attribute, value);
		}
		if (values.size() == 0)
			return m_Trainset.numInstances();
		// computes the count of the instances.
		int count = 0;
		for (Instance instance : m_Trainset.getInstances()) {
			boolean flag = true;
			for (Map.Entry<String, String> entry : values.entrySet()) {
				if (!instance.getValues().get(entry.getKey())
						.equals(entry.getValue())) {
					flag = false;
					break;
				}
			}
			if (flag)
				count++;
		}
		return count;
	}

	/**
	 * Computes the entropy at the given index of the decision tree and other
	 * condition
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @param other
	 *            the given other condition
	 * @return the entropy at the given index of the decision tree and other
	 *         condition
	 */
	private double entropy(int index, Map<String, String> other) {

		Map<String, String> values = new HashMap<String, String>();
		if (other != null)
			values.putAll(other);
		int c = count(index, values);
		double e = 0;
		Attribute f = m_Trainset.getAttributes().get(
				m_Trainset.numAttributes() - 1);
		for (Map.Entry<String, Integer> entry : f.getValues().entrySet()) {
			values.clear();
			if (other != null)
				values.putAll(other);
			values.put(f.getName(), entry.getKey());
			int t = count(index, values);
			if (t != 0) {
				e -= (double) t / (double) c
						* Math.log((double) t / (double) c) / Math.log(2.0);
			}
		}
		return e;
	}

	/**
	 * Computes the gain at the given index of the decision tree and index of
	 * attribute.
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @param attributeIndex
	 *            the given index of attribute
	 * @return the gain at the given index of the decision tree and index of
	 *         attribute
	 */
	private double gain(int index, int attributeIndex) {

		double e = entropy(index, null);
		int c = count(index, null);
		double ea = 0;
		Map<String, String> values = new HashMap<String, String>();
		Attribute a = m_Trainset.getAttributes().get(attributeIndex);
		for (Map.Entry<String, Integer> entry : a.getValues().entrySet()) {
			values.clear();
			values.put(a.getName(), entry.getKey());
			int t = count(index, values);
			double ei = entropy(index, values);
			ea += (double) t / (double) c * ei;
		}
		return e - ea;
	}

	/**
	 * Shows the decision tree at the given index of the decision tree.
	 * 
	 * @param index
	 *            the given index of the decision tree
	 * @return the decision tree at the given index of the decision tree as a
	 *         string
	 */
	private String showTree(int index) {

		StringBuilder sb = new StringBuilder();
		int deep = 0;
		int in = index;
		while (m_Tree.get(in).getPreNodeIndex() != -1) {
			deep++;
			in = m_Tree.get(in).getPreNodeIndex();
		}
		if (m_Tree.get(index).getPreNodeIndex() != -1) {
			for (int i = 1; i < deep; i++)
				sb.append("|  ");
			sb.append(m_Trainset
					.getAttributes()
					.get(m_Tree.get(m_Tree.get(index).getPreNodeIndex())
							.getAttributeIndex()).getName()
					+ " = " + m_Tree.get(index).getPreValue() + "\n");
		}
		List<Map.Entry<Object, Integer>> infoIds = new ArrayList<Map.Entry<Object, Integer>>(
				m_Tree.get(index).getPostNodeIndex().entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<Object, Integer>>() {
			public int compare(Map.Entry<Object, Integer> o1,
					Map.Entry<Object, Integer> o2) {
				return m_Tree.get(o1.getValue()).numPostNodeIndex()
						- m_Tree.get(o2.getValue()).numPostNodeIndex();
			}
		});
		for (int i = 0; i < infoIds.size(); i++) {
			if (infoIds.get(i).getValue() == index) {
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				sb.append(": " + infoIds.get(i).getKey() + "\n");
				return sb.toString();
			}
			sb.append(showTree(infoIds.get(i).getValue()));
		}
		return sb.toString();
	}

	/**
	 * Returns the result of this train with ID3 algorithm.
	 * 
	 * @return the result of this train with ID3 algorithm
	 */
	public String toString() {

		return showTree(0);
	}
}
