/*
 *    Main.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main frame for data mining.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class Main extends JPanel {

	/** for serialization */
	private static final long serialVersionUID = -5353819160783611451L;

	/** The tabbedPane. */
	protected JTabbedPane m_TabbedPane = new JTabbedPane();

	/** The analysis panel. */
	protected AnalysisPanel m_AnalysisPanel;

	/** The data panel. */
	protected DataPanel m_DataPanel;

	/**
	 * Creates main frame.
	 */
	public Main() {

		m_AnalysisPanel = new AnalysisPanel(this);
		m_DataPanel = new DataPanel();

		m_TabbedPane.addTab("挖掘", m_AnalysisPanel);
		m_TabbedPane.addTab("数据", m_DataPanel);

		m_TabbedPane.setSelectedIndex(0);
		m_TabbedPane.setEnabledAt(1, false);

		setLayout(new BorderLayout());
		add(m_TabbedPane, BorderLayout.CENTER);
	}

	/**
	 * Sets the current look and feel.
	 */
	public static void setLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		setLookAndFeel();
		Main main = new Main();
		final JFrame jf = new JFrame("数据挖掘");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(main, BorderLayout.CENTER);
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jf.dispose();
				System.exit(0);
			}
		});
		jf.pack();
		jf.setSize(800, 600);
		jf.setVisible(true);
	}
}
