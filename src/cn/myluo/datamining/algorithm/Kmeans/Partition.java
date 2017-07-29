/*
 *    Partition.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.Kmeans;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.data.Instance;

/**
 * Class for partition with K-means algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Partition {

	/** the train set. */
	private DataSet trainset;

	/** the K parameter. */
	private int k;

	/** the list of clusters. */
	private List<Cluster> clusterlist;

	/**
	 * Creates a Partition class.
	 */
	private Partition() {

		clusterlist = new ArrayList<Cluster>();
	}

	/**
	 * Creates a Partition class with the given train set and K parameter.
	 * 
	 * @param trainset
	 *            the given train set
	 * @param k
	 *            the K parameter
	 */
	public Partition(DataSet trainset, int k) {

		this();
		this.trainset = trainset;
		this.k = k;
		train();
	}

	/**
	 * Partition with K-means algorithm.
	 */
	private void train() {

		// picks cluster centers at random.
		int[] randoms = random();
		for (int i = 0; i < k; i++) {
			clusterlist
					.add(new Cluster(trainset.getInstances().get(randoms[i])));
		}
		while (true) {
			for (Instance instance : trainset.getInstances()) {
				// computes distances and picks the max distance.
				double distance = Double.MAX_VALUE;
				int clusterindex = -1;
				for (int j = 0; j < clusterlist.size(); j++) {
					double d = clusterlist.get(j).distance(instance);
					if (d < distance) {
						distance = d;
						clusterindex = j;
					}
				}
				clusterlist.get(clusterindex).appendInstance(instance);
			}
			// checks all clusters have change.
			boolean isChange = true;
			for (Cluster cluster : clusterlist) {
				isChange &= cluster.isChange();
			}
			if (!isChange)
				break;
			// updates all clusters
			for (Cluster cluster : clusterlist) {
				cluster.updateCenter();
			}
		}
	}

	/**
	 * Gets the random.
	 * 
	 * @return the random
	 */
	private int[] random() {

		int[] randoms = new int[k];
		for (int i = 0; i < k;) {
			boolean flag = true;
			int r = (int) (Math.random() * trainset.numInstances());
			for (int j = 0; j < i; j++) {
				if (r == randoms[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				randoms[i] = r;
				i++;
			}
		}
		return randoms;
	}

	/**
	 * Gets the list of clusters.
	 * 
	 * @return the list of clusters
	 */
	public List<Cluster> getClusterList() {

		return clusterlist;
	}

	/**
	 * Computes the SSE of all clusters.
	 * 
	 * @return the SSE of all clusters
	 */
	public double SSE() {

		double sse = 0;
		for (Cluster cluster : clusterlist) {
			sse += cluster.SSE();
		}
		return sse;
	}

	/**
	 * Returns the result of this partition with K-means algorithm.
	 * 
	 * @return the result of this partition with K-means algorithm
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("ŒÛ≤Ó∆Ω∑Ω∫Õ(SSE): " + new DecimalFormat("#0.00").format(SSE())
				+ "\n\n");
		sb.append("¥ÿ(" + clusterlist.size() + "):\n\n");
		for (int i = 0; i < clusterlist.size(); i++) {
			sb.append("¥ÿ #" + (i + 1) + "\n");
			sb.append(clusterlist.get(i).toString());
			sb.append("\n");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
