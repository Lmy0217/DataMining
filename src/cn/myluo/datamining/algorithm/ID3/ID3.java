/*
 *    ID3.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.algorithm.ID3;

import java.io.File;

import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.util.Reader;

/**
 * Class for run ID3 algorithm.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class ID3 {

	/** the train for ID3. */
	private Train m_Train;

	/**
	 * Run ID3 algorithm with the given train set.
	 * 
	 * @param trainset
	 *            the given train set
	 */
	public ID3(DataSet trainset) {

		m_Train = new Train(trainset);
	}

	/**
	 * Returns the result of this ID3 algorithm with train set.
	 * 
	 * @return the result of this ID3 algorithm with train set
	 */
	public String toString() {

		return m_Train.toString();
	}

	/**
	 * Tests this ID3 algorithm with the given train set.
	 * 
	 * @param args
	 *            must contain the name of an train set file to load.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + ID3.class.getName()
					+ " <dataset>\n");
			return;
		}

		try {
			System.out
					.print(new ID3(new Reader(new File(args[0])).getDataSet()));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
