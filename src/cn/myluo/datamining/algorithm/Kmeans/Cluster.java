/*
 *    Cluster.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.Kmeans;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import cn.myluo.datamining.data.Instance;

/**
 * The cluster of K-means algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Cluster {

	/** the center of this cluster. */
	private Instance m_Center;

	/** the mean of instances in this cluster. */
	private Instance m_Mean;

	/** the variance of instances in this cluster. */
	private Instance m_Variance;

	/** the map of instances and distances of center in this cluster. */
	private Map<Instance, Double> m_InstanceError;

	/**
	 * Creates a cluster.
	 */
	private Cluster() {

		m_InstanceError = new HashMap<Instance, Double>();
	}

	/**
	 * Creates a cluster with the given seed.
	 * 
	 * @param seed
	 *            the given seed
	 */
	Cluster(Instance seed) {

		this();
		m_Center = seed.clone();
		center2double();
		m_Mean = m_Center.clone();
		m_Variance = m_Center.clone();
		clear();
	}

	/**
	 * Translate the center's values type to double
	 */
	private void center2double() {

		if (m_Center.numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Center.getValues()
					.entrySet()) {
				m_Center.append(entry.getKey(),
						Double.parseDouble((String) entry.getValue()));
			}
		}
	}

	/**
	 * Clears the mean, variance and the map of instances and distances of
	 * center.
	 */
	private void clear() {

		if (m_Mean.numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Mean.getValues()
					.entrySet()) {
				m_Mean.append(entry.getKey(), 0.0);
			}
		}
		if (m_Variance.numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Variance.getValues()
					.entrySet()) {
				m_Variance.append(entry.getKey(), 0.0);
			}
		}
		m_InstanceError.clear();
	}

	/**
	 * Computes the distance of the center and the given instance.
	 * 
	 * @param instance
	 *            the given instance
	 * @return the distance of the center and the given instance
	 */
	double distance(Instance instance) {

		double d = 0;
		for (Map.Entry<String, Object> entry : m_Center.getValues().entrySet()) {
			d += Math.pow(
					Double.parseDouble((String) instance.getValues()
							.getOrDefault(entry.getKey(), 0.0))
							- (Double) entry.getValue(), 2);
		}
		return Math.sqrt(d);
	}

	/**
	 * Adds the given instance into the map of instances and distances of center
	 * 
	 * @param instance
	 *            the given instance
	 */
	void appendInstance(Instance instance) {

		// updates variance
		if (m_Variance.numValues() > 0 && numInstances() > 0) {
			for (Map.Entry<String, Object> entry : m_Variance.getValues()
					.entrySet()) {
				m_Variance.append(
						entry.getKey(),
						(Double) entry.getValue()
								* ((double) numInstances() - 1.0)
								/ (double) numInstances()
								+ Math.pow(
										Double.parseDouble((String) instance
												.getValues()
												.get(entry.getKey()))
												- (Double) m_Mean.getValues()
														.get(entry.getKey()),
										2.0) / ((double) numInstances() + 1.0));
			}
		}
		// updates mean
		if (m_Mean.numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Mean.getValues()
					.entrySet()) {
				m_Mean.append(
						entry.getKey(),
						((Double) entry.getValue() * (double) numInstances() + Double
								.parseDouble((String) instance.getValues().get(
										entry.getKey())))
								/ ((double) numInstances() + 1.0));
			}
		}
		m_InstanceError.put(instance, distance(instance));
	}

	/**
	 * Computes the SSE of all instances in this cluster.
	 * 
	 * @return the SSE of all instances in this cluster
	 */
	double SSE() {

		double sse = 0;
		if (m_Variance.numValues() > 0) {
			for (Map.Entry<String, Object> entry : m_Variance.getValues()
					.entrySet()) {
				sse += (Double) entry.getValue();
			}
		}
		sse *= (double) numInstances() - 1.0;
		return sse;
	}

	/**
	 * Is the center changed?
	 * 
	 * @return true if the center changed, false otherwise.
	 */
	boolean isChange() {

		return m_Center.compareTo(m_Mean) == 0;
	}

	/**
	 * Updates the center.
	 */
	void updateCenter() {

		m_Center = m_Mean.clone();
		clear();
	}

	/**
	 * Gets the number of instances in this cluster.
	 * 
	 * @return the number of instances in this cluster
	 */
	public int numInstances() {

		return m_InstanceError.size();
	}

	/**
	 * Returns a description of this cluster.
	 * 
	 * @return a description of this cluster as a string
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("   中心:\n      ");
		for (Map.Entry<String, Object> entry : m_Center.getValues().entrySet()) {
			sb.append(entry.getKey()
					+ "="
					+ new DecimalFormat("#0.00").format((Double) entry
							.getValue()) + ",");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		sb.append("\n   数据(" + numInstances() + "):\n");
		for (Map.Entry<Instance, Double> entry : m_InstanceError.entrySet()) {
			sb.append("      ");
			for (Map.Entry<String, Object> e : entry.getKey().getValues()
					.entrySet()) {
				sb.append(e.getKey() + "=" + e.getValue() + ",");
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
		}
		return sb.toString();
	}
}
