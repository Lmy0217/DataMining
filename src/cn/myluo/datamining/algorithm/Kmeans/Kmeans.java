/*
 *    Kmeans.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.Kmeans;

import java.io.File;

import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.util.Reader;

/**
 * Class for run K-means algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Kmeans {

	/** the partition for K-means. */
	private Partition m_Partition;

	/**
	 * Run K-means algorithm with the given data set and K parameter.
	 * 
	 * @param dataset
	 *            the given data set
	 * @param k
	 *            the given K parameter
	 */
	public Kmeans(DataSet dataset, int k) {

		m_Partition = new Partition(dataset, k);
	}

	/**
	 * Returns the result of this K-means algorithm with data set.
	 * 
	 * @return the result of this K-means algorithm with data set
	 */
	public String toString() {

		return m_Partition.toString();
	}

	/**
	 * Tests this K-means algorithm with the given data set and K parameter.
	 * 
	 * @param args
	 *            must contain the name of an data set file and K parameter to
	 *            load.
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			System.out.println("\nUsage: " + Kmeans.class.getName()
					+ " <dataset> <K>\n");
			return;
		}

		try {
			System.out.println(new Kmeans(new Reader(new File(args[0]))
					.getDataSet(), Integer.parseInt(args[1])));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
