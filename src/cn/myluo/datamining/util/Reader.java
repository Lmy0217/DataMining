/*
 *    Reader.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

import cn.myluo.datamining.data.Attribute;
import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.data.Instance;

/**
 * Reads data set from an TXT file.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Reader {

	/** The data set file. */
	private File m_File;

	/** The file reader. */
	private java.io.Reader m_Reader;

	/** The file tokenizer. */
	private StreamTokenizer m_Tokenizer;

	/** The data set. */
	private DataSet m_Dataset;

	/** The number of error instances. */
	private int m_NumErrorInstances;

	/**
	 * Creates a reader.
	 */
	private Reader() {

		m_Dataset = new DataSet();
	}

	/**
	 * Creates a reader with the given data set file.
	 * 
	 * @param file
	 *            the given data set file
	 */
	public Reader(File file) {

		this();
		setReader(file);
		initTokenizer();
		read();
	}

	/**
	 * Gets the data set file
	 * 
	 * @return the data set file
	 */
	public File getFile() {

		return m_File;
	}

	/**
	 * Gets the data set
	 * 
	 * @return the data set
	 */
	public DataSet getDataSet() {

		return m_Dataset;
	}

	/**
	 * Gets the number of the error instances.
	 * 
	 * @return the number of the error instances
	 */
	public int numErrorInstances() {

		return m_NumErrorInstances;
	}

	/**
	 * Sets the reader with the given data set file.
	 * 
	 * @param file
	 *            the given data set file
	 */
	private void setReader(File file) {

		try {
			m_Reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			this.m_File = file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the StreamTokenizer used for reading the TXT file.
	 */
	private void initTokenizer() {

		if (m_Reader == null)
			return;

		m_Tokenizer = new StreamTokenizer(m_Reader);
		m_Tokenizer.resetSyntax();
		m_Tokenizer.whitespaceChars(0, ' ' - 1);
		m_Tokenizer.wordChars(' ', '\u00FF');
		m_Tokenizer.whitespaceChars(',', ',');
		m_Tokenizer.commentChar('%');
		m_Tokenizer.quoteChar('"');
		// tokenizer.quoteChar('\'');
		m_Tokenizer.ordinaryChar('{');
		m_Tokenizer.ordinaryChar('}');
		m_Tokenizer.eolIsSignificant(true);
	}

	/**
	 * Reads the data set from file.
	 */
	private void read() {

		if (m_Tokenizer == null)
			return;

		m_Dataset.setName(readName());

		// Saves the current line.
		List<String> line = null;
		boolean dataStart = false;
		try {
			while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				if (m_Tokenizer.ttype != StreamTokenizer.TT_EOL) {
					if (line == null)
						line = new ArrayList<String>();
					if (m_Tokenizer.ttype == StreamTokenizer.TT_WORD) {
						line.add(m_Tokenizer.sval);
					} else if (m_Tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
						line.add("" + m_Tokenizer.nval);
					}
				} else {
					// Checks if the current line is attribute.
					if (!dataStart
							&& line != null
							&& line.size() == 2
							&& (line.get(1).equals("0") || line.get(1).equals(
									"1"))) {
						m_Dataset.appendAttribute(readAttribute(line));
					} else if (line != null && line.size() > 0) {
						dataStart = true;
						m_Dataset.appendInstance(readInstance(
								m_Dataset.getAttributes(), line));
					}
					line = null;
				}
			}
			if (!dataStart && line != null && line.size() == 2
					&& (line.get(1).equals("0") || line.get(1).equals("1"))) {
				m_Dataset.appendAttribute(readAttribute(line));
			} else if (line != null && line.size() > 0) {
				dataStart = true;
				m_Dataset.appendInstance(readInstance(
						m_Dataset.getAttributes(), line));
			}
			line = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the data set name.
	 * 
	 * @return the data set name
	 */
	private String readName() {

		String name = null;
		try {
			while (m_Tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				if (name != null && m_Tokenizer.ttype == StreamTokenizer.TT_EOL)
					break;
				if (m_Tokenizer.ttype == StreamTokenizer.TT_WORD
						&& name == null) {
					name = m_Tokenizer.sval;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name != null ? name : "";
	}

	/**
	 * Reads a attribute with the current line.
	 * 
	 * @param line
	 *            the current line
	 * @return the attribute with the current line
	 */
	private Attribute readAttribute(List<String> line) {

		return new Attribute(line.get(0), line.get(1));
	}

	/**
	 * Reads a instance with the current line and the given attributes.
	 * 
	 * @param attributes
	 *            the given attributes
	 * @param line
	 *            the current line
	 * @return the instance with the current line and the given attributes
	 */
	private Instance readInstance(List<Attribute> attributes, List<String> line) {

		// Checks length
		if (attributes.size() != line.size()) {
			m_NumErrorInstances++;
			return null;
		}

		Instance instance = new Instance();
		for (int i = 0; i < line.size(); i++) {
			// Checks type
			if (attributes.get(i).getType().equals("0")) {
				try {
					Double.parseDouble(line.get(i));
				} catch (NumberFormatException e) {
					m_NumErrorInstances++;
					return null;
				}
			}
			instance.append(attributes.get(i).getName(), line.get(i));
			attributes.get(i).append(line.get(i), 1);
		}
		return instance;
	}

	/**
	 * Returns a description of this Reader in TXT format.
	 * 
	 * @return a description of this Reader as a string
	 */
	public String toString() {

		return m_Dataset.toString();
	}

	/**
	 * Tests this Reader with the given data set in input file.
	 * 
	 * @param args
	 *            should contain the name of an input file.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + Reader.class.getName()
					+ " <dataset>\n");
			return;
		}

		try {
			System.out.println(new Reader(new File(args[0])));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
