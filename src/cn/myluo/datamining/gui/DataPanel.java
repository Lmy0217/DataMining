/*
 *    DataPanel.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.BorderFactory;

import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.util.Reader;

/**
 * The data set panel.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class DataPanel extends JPanel {

	/** for serialization */
	private static final long serialVersionUID = 627131485290359194L;

	/**
	 * A table model that displays the data set.
	 * 
	 * @author Luo Mingyuan
	 * @version 1612
	 */
	class DataTableModel extends AbstractTableModel {

		/** for serialization */
		private static final long serialVersionUID = -4152987434024338064L;

		/** The data set. */
		protected DataSet m_Dataset;

		/**
		 * Creates the table model with the given data set.
		 * 
		 * @param dataset
		 *            the given data set
		 */
		public DataTableModel(DataSet dataset) {

			setDataSet(dataset);
		}

		/**
		 * Sets the data set with the given data set.
		 * 
		 * @param dataset
		 *            the given data set
		 */
		public void setDataSet(DataSet dataset) {

			this.m_Dataset = dataset;
		}

		/**
		 * Gets the number of attributes.
		 * 
		 * @return the number of attributes.
		 */
		public int getRowCount() {

			return m_Dataset.numInstances();
		}

		/**
		 * Gets the number of instances.
		 * 
		 * @return the number of instances.
		 */
		public int getColumnCount() {

			return m_Dataset.numAttributes();
		}

		/**
		 * Gets a table cell
		 * 
		 * @param row
		 *            the row index
		 * @param column
		 *            the column index
		 * @return the value at row, column
		 */
		public Object getValueAt(int row, int column) {

			return m_Dataset.getInstances().get(row).getValues()
					.get(getColumnName(column));
		}

		/**
		 * Gets the name for a column.
		 * 
		 * @param column
		 *            the column index.
		 * @return the name of the column.
		 */
		public String getColumnName(int column) {

			return m_Dataset.getAttributes().get(column).getName();
		}

		/**
		 * Sets the value at a cell.
		 * 
		 * @param value
		 *            the new value.
		 * @param row
		 *            the row index.
		 * @param col
		 *            the column index.
		 */
		public void setValueAt(Object value, int row, int col) {
		}

		/**
		 * Gets the class of elements in a column.
		 * 
		 * @param col
		 *            the column index.
		 * @return the class of elements in the column.
		 */
		public Class<?> getColumnClass(int col) {

			return getValueAt(0, col).getClass();
		}

		/**
		 * Returns true if the column is the "selected" column.
		 * 
		 * @param row
		 *            ignored
		 * @param col
		 *            ignored
		 * @return false
		 */
		public boolean isCellEditable(int row, int col) {

			return false;
		}

	}

	/** The data set table. */
	protected JTable m_Table = new JTable();

	/** The data set table model. */
	protected DataTableModel m_Model;

	/**
	 * Creates the data set panel.
	 */
	public DataPanel() {

		m_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_Table.setColumnSelectionAllowed(false);
		m_Table.setPreferredScrollableViewportSize(new Dimension(250, 150));

		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		p1.setLayout(new GridLayout(1, 4, 5, 5));

		setLayout(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		add(new JScrollPane(m_Table), BorderLayout.CENTER);
	}

	/**
	 * Sets the data set with the given data set.
	 * 
	 * @param dataset
	 *            the given data set
	 */
	public void setDataSet(DataSet dataset) {

		m_Model = new DataTableModel(dataset);
		m_Table.setModel(m_Model);

		m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = m_Table.getColumnModel();
		int totalColumnWidth = 0;
		int[] widths = new int[m_Model.getColumnCount()];
		for (int i = 0; i < widths.length; i++) {
			widths[i] = (int) m_Table
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(m_Table,
							tcm.getColumn(i).getIdentifier(), false, false, -1,
							i).getPreferredSize().getWidth();
			totalColumnWidth += widths[i];
		}

		// resets widths to adaptive size.
		if (tcm.getTotalColumnWidth() < 800) {
			double ratio = 800.0 / totalColumnWidth;
			for (int i = 0; i < tcm.getColumnCount(); i++) {
				m_Table.getColumnModel().getColumn(i)
						.setMinWidth((int) (ratio * (double) widths[i]));
			}
		} else {
			for (int i = 0; i < tcm.getColumnCount(); i++) {
				int width = widths[i];
				for (int j = 0; j < m_Model.getRowCount(); j++) {
					int preferedWidth = (int) m_Table
							.getCellRenderer(j, i)
							.getTableCellRendererComponent(m_Table,
									m_Table.getValueAt(j, i), false, false, j,
									i).getPreferredSize().getWidth();
					if (preferedWidth > width)
						width = preferedWidth;
				}
				m_Table.getColumnModel().getColumn(i).setMinWidth(width + 4);
			}
		}

		m_Table.clearSelection();
		m_Table.doLayout();
		m_Table.revalidate();
		m_Table.repaint();
	}

	/**
	 * Gets the current data set.
	 * 
	 * @return the current data set
	 */
	public DataSet getDataSet() {

		return m_Model.m_Dataset;
	}

	/**
	 * Gets the current selected model.
	 * 
	 * @return the current selected model
	 */
	public ListSelectionModel getSelectionModel() {

		return m_Table.getSelectionModel();
	}

	/**
	 * Tests the data set panel with the given data set.
	 * 
	 * @param args
	 *            must contain the name of an data set file to load.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("\nUsage: " + DataPanel.class.getName()
					+ " <dataset>\n");
			return;
		}

		try {
			final JFrame jf = new JFrame("DataPanel");
			jf.getContentPane().setLayout(new BorderLayout());
			DataPanel sp = new DataPanel();
			sp.setDataSet(new Reader(new File(args[0])).getDataSet());
			jf.getContentPane().add(sp, BorderLayout.CENTER);
			jf.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					jf.dispose();
					System.exit(0);
				}
			});
			jf.pack();
			jf.setSize(700, 550);
			jf.setVisible(true);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
