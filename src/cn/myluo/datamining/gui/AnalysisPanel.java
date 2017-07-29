/*
 *    AnalysisPanel.java
 *    Copyright (C) 2016 NanChang University, JiangXi, China
 *
 */

package cn.myluo.datamining.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cn.myluo.datamining.algorithm.ID3.ID3;
import cn.myluo.datamining.algorithm.Kmeans.Kmeans;
import cn.myluo.datamining.data.Attribute;
import cn.myluo.datamining.data.DataSet;
import cn.myluo.datamining.util.Reader;

/**
 * The mining analysis panel.
 * 
 * @author Luo Mingyuan
 * @version 1612
 */
public class AnalysisPanel extends JPanel {

	/** for serialization */
	private static final long serialVersionUID = -4913007978534178569L;

	/** The label for no source. */
	protected JLabel m_FromLab = new JLabel("无数据");

	/** The file chooser for data source. */
	protected JFileChooser m_FileChooser = new JFileChooser(new File(
			System.getProperty("user.dir")));

	/** The file chooser for save file. */
	protected JFileChooser m_SaveFileChooser = new JFileChooser(new File(
			System.getProperty("user.dir")));

	/** The thread for load file. */
	protected Thread m_LoadThread;

	/** The thread for save file. */
	protected Thread m_SaveThread;

	/** The choose file button. */
	protected JButton m_FromFileBut = new JButton("文件...");

	/** The algorithm label. */
	protected JLabel m_AlgorithmClassesLabel = new JLabel("算法",
			SwingConstants.RIGHT);

	/** The string array for data mining algorithms. */
	private static String[] m_Algorithms = { "ID3", "K-means" };

	/** The combo box model for algorithms. */
	protected static DefaultComboBoxModel<String> m_AlgorithmClassesModel = new DefaultComboBoxModel<String>(
			m_Algorithms);

	/** The choose algorithms combo box. */
	protected JComboBox<String> m_AlgorithmClasses = new JComboBox<String>(
			m_AlgorithmClassesModel);

	/** K of K-means. */
	private int m_K = 2;

	/** The Text field for K. */
	protected JTextField m_KTex = new JTextField("" + m_K);

	/** The start mining button. */
	protected JButton m_StartBtn = new JButton("开始挖掘");

	/** The save file button. */
	protected JButton m_SaveBtn = new JButton("保存结果");

	/** The text area saving operations. */
	protected JTextArea m_OperateText = new JTextArea();

	/** The text area saving result. */
	protected JTextArea m_OutText = new JTextArea();

	/** The index of selected algorithm. */
	private int m_SelectedAlgorithmIndex;

	/** The main frame for data mining. */
	private Main m_Main;

	/**
	 * Creates the mining analysis panel with the given main frame.
	 * 
	 * @param main
	 *            the main frame for data mining
	 */
	public AnalysisPanel(Main main) {

		this();
		this.m_Main = main;
	}

	/**
	 * Creates the mining analysis panel.
	 */
	public AnalysisPanel() {

		m_FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// the choose file button adds action listener for load data file.
		m_FromFileBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = m_FileChooser.showOpenDialog(AnalysisPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File selected = m_FileChooser.getSelectedFile();
					if (m_LoadThread == null) {
						m_LoadThread = new Thread() {
							public void run() {
								readDataSetFromFile(selected);
								m_LoadThread = null;
							}
						};
						m_LoadThread.start();
					}
				}
			}
		});

		// p1 is the choose file panel in the north of the analysis panel.
		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder("数据集"));
		// p2 is the button panel in p1.
		JPanel p2 = new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		GridBagLayout gb = new GridBagLayout();
		p2.setLayout(gb);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.insets = new Insets(0, 2, 0, 2);
		// p2 adds the choose file button.
		p2.add(m_FromFileBut, constraints);
		p1.setLayout(new BorderLayout());
		// p1 adds the file information label.
		p1.add(m_FromLab, BorderLayout.CENTER);
		// p1 adds p2.
		p1.add(p2, BorderLayout.EAST);

		// p3 is the panel including algorithms' parameters.
		JPanel p3 = new JPanel();
		p3.setBorder(BorderFactory.createTitledBorder("参数列表"));
		GridBagLayout gbL = new GridBagLayout();
		p3.setLayout(gbL);

		GridBagConstraints gbC = new GridBagConstraints();
		gbC.anchor = GridBagConstraints.EAST;
		gbC.gridy = 0;
		gbC.gridx = 0;
		gbC.insets = new Insets(2, 10, 2, 10);
		gbL.setConstraints(m_AlgorithmClassesLabel, gbC);
		m_AlgorithmClassesLabel.setLabelFor(m_AlgorithmClasses);
		// p3 adds the algorithm label.
		p3.add(m_AlgorithmClassesLabel);
		gbC = new GridBagConstraints();
		gbC.gridy = 0;
		gbC.gridx = 1;
		gbC.weightx = 100;
		gbC.insets = new Insets(5, 0, 5, 0);
		gbC.fill = GridBagConstraints.HORIZONTAL;
		gbL.setConstraints(m_AlgorithmClasses, gbC);
		m_AlgorithmClasses.setPreferredSize(new Dimension(150, 28));
		m_AlgorithmClasses.setMaximumSize(new Dimension(150, 28));
		m_AlgorithmClasses.setMinimumSize(new Dimension(150, 28));
		m_AlgorithmClasses.setEnabled(false);
		// p3 adds the choose algorithms combo box.
		p3.add(m_AlgorithmClasses);
		m_AlgorithmClasses.setSelectedIndex(0);
		changeAlgorithm();
		// the choose algorithms combo box adds action listener for change
		// selected algorithm.
		m_AlgorithmClasses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeAlgorithm();
			}
		});

		// the K label in p3.
		JLabel lab = new JLabel("K", SwingConstants.RIGHT);
		lab.setLabelFor(m_KTex);
		gbC = new GridBagConstraints();
		gbC.anchor = GridBagConstraints.EAST;
		gbC.gridy = 1;
		gbC.gridx = 0;
		gbC.insets = new Insets(2, 10, 2, 10);
		gbL.setConstraints(lab, gbC);
		// p3 adds the K label.
		p3.add(lab);
		gbC = new GridBagConstraints();
		gbC.fill = GridBagConstraints.HORIZONTAL;
		gbC.gridy = 1;
		gbC.gridx = 1;
		gbC.weightx = 100;
		gbL.setConstraints(m_KTex, gbC);
		m_KTex.setPreferredSize(new Dimension(150, 28));
		m_KTex.setMaximumSize(new Dimension(150, 28));
		m_KTex.setMinimumSize(new Dimension(150, 28));
		m_KTex.setEnabled(false);
		// the K text field adds action listener for change K of K-means.
		m_KTex.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				try {
					m_K = Integer.parseInt(m_KTex.getText());
					if (m_K < 2
							|| (m_Main != null && m_K > m_Main.m_DataPanel
									.getDataSet().numInstances())) {
						m_K = 2;
						m_KTex.setText("" + m_K);
					}
				} catch (NumberFormatException e1) {
					m_K = 2;
					m_KTex.setText("" + m_K);
				}
			}
		});
		// p3 adds the K text field.
		p3.add(m_KTex);

		// mondo is the panel in the east of the analysis panel.
		JPanel mondo = new JPanel();
		gbL = new GridBagLayout();
		mondo.setLayout(gbL);
		gbC = new GridBagConstraints();
		gbC.gridy = 0;
		gbC.gridx = 0;
		gbL.setConstraints(p3, gbC);
		// mondo adds p3.
		mondo.add(p3);

		// bts is the button panel including start mining button and save file
		// button.
		JPanel bts = new JPanel();
		bts.setLayout(new GridLayout(1, 2, 5, 5));
		m_StartBtn.setEnabled(false);
		// start mining button adds action listener for start data mining.
		m_StartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check if the data set could mining in selected algorithm.
				String error = getError();
				if (error != null) {
					if (JOptionPane.showOptionDialog(AnalysisPanel.this, "数据集'"
							+ m_Main.m_DataPanel.getDataSet().getName() + "'不能使用"
							+ m_Algorithms[m_SelectedAlgorithmIndex] + "算法.\n"
							+ "原因:\n" + error, "开始挖掘", 0,
							JOptionPane.ERROR_MESSAGE, null,
							new String[] { "确定" }, null) == 1) {

					}
					return;
				}
				m_SaveBtn.setEnabled(true);
				String scheme = m_Algorithms[m_SelectedAlgorithmIndex]
						+ (m_KTex.isEnabled() ? (" K=" + m_K) : "") + "\n";
				// print start mining operation.
				printOperation(scheme);
				String datasetAbstract = "";
				if (m_Main != null)
					// get data set abstract.
					datasetAbstract = m_Main.m_DataPanel.getDataSet()
							.getAbstract();
				// get data mining result.
				String result = analysis();
				printResult("算法:         " + scheme + datasetAbstract, result);
			}
		});
		// bts adds start mining button.
		bts.add(m_StartBtn);
		m_SaveBtn.setEnabled(false);
		m_SaveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// save file button adds action listener for save data mining result.
		m_SaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check if the result text area is empty.
				if (m_OutText.getText().length() == 0) {
					m_SaveBtn.setEnabled(false);
					return;
				}
				int returnVal = m_SaveFileChooser
						.showSaveDialog(AnalysisPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File selected = m_SaveFileChooser.getSelectedFile();
					if (m_SaveThread == null) {
						m_SaveThread = new Thread() {
							public void run() {
								saveResult(selected);
								m_SaveThread = null;
							}
						};
						m_SaveThread.start();
					}
				}
			}
		});
		// bts adds save file button.
		bts.add(m_SaveBtn);
		gbC = new GridBagConstraints();
		gbC.anchor = GridBagConstraints.NORTH;
		gbC.fill = GridBagConstraints.HORIZONTAL;
		gbC.gridy = 1;
		gbC.gridx = 0;
		gbC.insets = new Insets(5, 5, 5, 5);
		gbL.setConstraints(bts, gbC);
		// mondo adds bts.
		mondo.add(bts);

		// operation is the panel save all operations.
		JPanel operation = new JPanel();
		operation.setLayout(new BorderLayout());
		operation.setBorder(BorderFactory.createTitledBorder("操作列表"));
		m_OperateText.setFont(new Font("Monospaced", Font.PLAIN, 12));
		m_OperateText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_OperateText.setEditable(false);
		operation.add(new JScrollPane(m_OperateText), BorderLayout.CENTER);
		gbC = new GridBagConstraints();
		gbC.fill = GridBagConstraints.BOTH;
		gbC.gridy = 2;
		gbC.gridx = 0;
		gbC.weightx = 0;
		gbC.weighty = 100;
		gbL.setConstraints(operation, gbC);
		// mondo adds operation.
		mondo.add(operation);

		// output is the result panel.
		JPanel output = new JPanel();
		output.setLayout(new BorderLayout());
		output.setBorder(BorderFactory.createTitledBorder("挖掘结果"));
		m_OutText.setFont(new Font("Monospaced", Font.PLAIN, 12));
		m_OutText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		m_OutText.setEditable(false);
		// output adds outText with scroll panel.
		output.add(new JScrollPane(m_OutText), BorderLayout.CENTER);

		// splitPane splits the analysis panel in two part with mondo and
		// output.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				mondo, output);
		splitPane.setOneTouchExpandable(true);

		setLayout(new BorderLayout());
		// the analysis panel adds p1.
		add(p1, BorderLayout.NORTH);
		// the analysis panel adds splitPane.
		add(splitPane, BorderLayout.CENTER);
	}

	/**
	 * Changes selected algorithm.
	 */
	public void changeAlgorithm() {

		m_SelectedAlgorithmIndex = m_AlgorithmClasses.getSelectedIndex();
		if (m_SelectedAlgorithmIndex == 1 && m_AlgorithmClasses.isEnabled()) {
			m_KTex.setEnabled(true);
		} else {
			m_KTex.setEnabled(false);
		}
	}

	/**
	 * Checks if the current data set could mining with the selected algorithm.
	 * 
	 * @return the error information
	 */
	public String getError() {

		if (m_Main == null)
			return null;

		String error = null;
		if (m_Main.m_DataPanel.getDataSet().numInstances() == 0) {
			error = "没有数据!";
		} else if (m_SelectedAlgorithmIndex == 1
				&& m_Main.m_DataPanel.getDataSet().numInstances() == 1) {
			error = "没有足够的数据!";
		} else {
			for (Attribute a : m_Main.m_DataPanel.getDataSet().getAttributes()) {
				if (m_SelectedAlgorithmIndex == 0 && a.getType().equals("0")) {
					error = "包含连续型属性!";
					break;
				} else if (m_SelectedAlgorithmIndex == 1
						&& a.getType().equals("1")) {
					error = "包含分类型属性!";
					break;
				}
			}
		}
		return error;
	}

	/**
	 * Data mining with the current data set and the selected algorithm.
	 * 
	 * @return the data mining result
	 */
	public String analysis() {

		if (m_Main == null)
			return null;

		String str = null;
		switch (m_SelectedAlgorithmIndex) {
		case 0:
			str = new ID3(m_Main.m_DataPanel.getDataSet()).toString();
			break;
		case 1:
			str = new Kmeans(m_Main.m_DataPanel.getDataSet(), m_K).toString();
			break;
		}
		return str;
	}

	/**
	 * Saves the result in the given file.
	 * 
	 * @param file
	 *            the given file
	 */
	public void saveResult(File file) {

		boolean flag = true;
		if (!file.getPath().endsWith(".txt"))
			file = new File(file.getPath() + ".txt");
		FileOutputStream fos = null;
		try {
			if (!file.exists())
				file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(m_OutText.getText().getBytes());
			fos.flush();
		} catch (IOException e) {
			flag = false;
		} finally {
			try {
				if (fos != null) fos.close();
			} catch (IOException e) {
			}
		}
		if (flag)
			printOperation("保存结果\n");
		JOptionPane.showMessageDialog(this, flag ? "结果保存成功!\n注意：换行符使用LF！" : "结果保存失败!",
				"保存结果", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Prints the given operation.
	 * 
	 * @param operation
	 *            the given operation
	 */
	public void printOperation(String operation) {

		m_OperateText.append(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ " - ");
		m_OperateText.append(operation);
		m_OperateText.paintImmediately(m_OperateText.getBounds());
		m_OperateText.setCaretPosition(m_OperateText.getText().length());
	}

	/**
	 * Prints the given data set information and data mining result.
	 * 
	 * @param runInfo
	 *            the given data set information
	 * @param result
	 *            the given data mining result
	 */
	public void printResult(String runInfo, String result) {

		m_OutText.setText("=== 挖掘信息 ===\n\n");
		m_OutText.append(runInfo);
		m_OutText.append("\n=== 结果 ===\n\n");
		m_OutText.append(result);
		m_OutText.paintImmediately(m_OutText.getBounds());
		m_OutText.setCaretPosition(m_OutText.getText().length());
	}

	/**
	 * Reads the data set from the given file.
	 * 
	 * @param file
	 *            the given file
	 */
	protected void readDataSetFromFile(File file) {

		String fileType = file.getName();
		try {
			m_FromLab.setText("读取文件...");
			if (file.getName().toLowerCase().endsWith(".txt")) {
				fileType = "txt";
				Reader reader = new Reader(file);
				DataSet dataset = reader.getDataSet();
				int numErrorInstances = reader.numErrorInstances();
				if (m_Main != null) {
					m_Main.m_DataPanel.setDataSet(dataset);
					m_Main.m_TabbedPane.setEnabledAt(1, true);
				}
				if (!m_AlgorithmClasses.isEnabled())
					m_AlgorithmClasses.setEnabled(true);
				changeAlgorithm();
				if (!m_StartBtn.isEnabled())
					m_StartBtn.setEnabled(true);
				printOperation("加载数据集\n");
				m_FromLab.setText("获得" + dataset.numInstances() + "个数据");
				if (numErrorInstances > 0) {
					if (JOptionPane.showOptionDialog(AnalysisPanel.this, "数据集'"
							+ dataset.getName() + "'包含的" + numErrorInstances
							+ "个错误数据(属性值缺失或类型错误)被忽略!", "加载数据集", 0,
							JOptionPane.WARNING_MESSAGE, null,
							new String[] { "确定" }, null) == 1) {

					}
				}
			} else {
				throw new Exception("不能识别的文件类型");
			}
		} catch (Exception ex) {
			m_FromLab.setText("文件'" + file.getName() + "'不能识别为" + fileType
					+ "文件.");
			if (JOptionPane.showOptionDialog(AnalysisPanel.this,
					"文件'" + file.getName() + "'不能识别为" + fileType + "文件.\n"
							+ "原因:\n" + ex.getMessage(), "加载数据集", 0,
					JOptionPane.ERROR_MESSAGE, null, new String[] { "确定" },
					null) == 1) {

			}
		}
	}

	/**
	 * Tests the analysis panel.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		try {
			final JFrame jf = new JFrame("AnalysisPanel--这里不能测试算法");
			jf.getContentPane().setLayout(new BorderLayout());
			AnalysisPanel sp = new AnalysisPanel();
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
			e.printStackTrace();
		}
	}
}
