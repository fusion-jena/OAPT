package fusion.oapt.general.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import fusion.oapt.algorithm.merger.genericMerge.MergeApp;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.FindOptimalClusterIntractive;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.SelectCH;
import fusion.oapt.general.analysis.Analysis;
import fusion.oapt.general.analysis.Compare;
import fusion.oapt.general.cc.Cleaning_Allthings;
import fusion.oapt.general.cc.Configuration;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.visualization.Hierarchy;
import fusion.oapt.general.visualization.Visualization;
import fusion.oapt.general.visualization.Visualization2;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSpinner;

import javax.swing.SpinnerNumberModel;


public class OPATgui {

	private JFrame frmOapt;
	public static String NameAddressOnt;
	public static String NameAddressOnt1; 
	public static String NameAddressOnt2; 
	public static javax.swing.JButton ExecuteButton;
	public static javax.swing.JCheckBox includeDefinitionLinksCheckBox;
	public static javax.swing.JComboBox includeDefinitionResourcesComboBox;
	private javax.swing.JLabel TypeofAlgorithmLabel;
	private javax.swing.JLabel includeDefinitionResourcesLabel;
	public static javax.swing.JCheckBox includeDistanceLinksCheckBox;
	public static javax.swing.JCheckBox includePropertyLinksCheckBox;
	public static javax.swing.JCheckBox includeSubclassLinksCheckBox;
	public static javax.swing.JCheckBox includeSubstringLinksCheckBox;
	public static javax.swing.JComboBox linkTypeComboBox;
	private javax.swing.JLabel linkTypeLabel;
	private javax.swing.JLabel maxDistanceLabel;
	public static javax.swing.JSpinner maxDistanceSpinner;
	public static javax.swing.JButton ontologyFileChooserButton;
	private javax.swing.JLabel ontologyFileLabel;
	public static JTextArea ontologyFileTextField;
	private javax.swing.JFileChooser openOntologyFileChooser;
	private javax.swing.JPanel RunPanel;
	private javax.swing.JPanel OntologySelectionPanel;
	private javax.swing.JPanel RequiredArgumentsPanel;
	private javax.swing.JFileChooser saveFileChooser;
	private javax.swing.JLabel strengthDefinitionLinksLabel;
    public static javax.swing.JSpinner strengthDefinitionLinksSpinner;
    private javax.swing.JLabel strengthDistanceLinksLabel;
    public static javax.swing.JSpinner strengthDistanceLinksSpinner;
    private javax.swing.JLabel strengthPropertyLinksLabel;
    public static javax.swing.JSpinner strengthPropertyLinksSpinner;
    private javax.swing.JLabel strengthSubclassLinksLabel;
    public static javax.swing.JSpinner strengthSubclassLinksSpinner;
    private javax.swing.JLabel strengthSubstringLinksLabel;
    public static javax.swing.JSpinner strengthSubstringLinksSpinner;
    private javax.swing.JComboBox ListTypeComboBox;
    private javax.swing.JLabel NumCHLabel;
    private javax.swing.JLabel EmptyLabel;
    private javax.swing.JLabel EmptyLabel2;
    public static javax.swing.JTextField NumCHTextField;
    public static javax.swing.JButton SuggestButton;
    public static javax.swing.JButton SaveButton;
    public static javax.swing.JButton ViewGraphButton;
    public static javax.swing.JButton ViewAllGraphButton; 
    public static javax.swing.JButton ViewHierarchyButton;
    private javax.swing.ImageIcon pic1 ;
    private javax.swing.JPanel PicturePanel;
    private javax.swing.JPanel OutputPanel;
    private javax.swing.JPanel VisualViewPanel;
    private javax.swing.JScrollPane OutputScrollPane;
    public static javax.swing.JTextArea OutputPartitioningTextArea;
    public static javax.swing.JTextField NumModuleTextField;
    private javax.swing.JLabel NumModuleShowLabel;        
    public static javax.swing.JCheckBox showAllModuleCheckBox;
    static JProgressBar progressBar = new JProgressBar(1, 100);
    Controller con;

    //For partitioning evaluation panel
    private javax.swing.JPanel AvgEvaluationResultPanel;
    private javax.swing.JPanel DetailEvaluationResultPanel;
    private javax.swing.JLabel AvgLabel1;
    private javax.swing.JLabel AvgLabel2;
    private javax.swing.JLabel AvgLabel3;
    private javax.swing.JLabel DetailLabel1;
    private javax.swing.JLabel DetailLabel2;
    private javax.swing.JLabel DetailLabel3;
       
    public static  JTextArea Result1_Details;
    public static JTextArea Result2_Details;
    public static JTextArea Result3_Details;
    public static JTextField AvgTextBox1 ;
    public static JTextField AvgTextBox2 ;
    public static JTextField AvgTextBox3 ;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JLabel label_3;
    private JPanel panel_4;
    private JLabel label_4;
    private JTextField ont1stTextField;
    private JButton button;
    private JLabel label_5;
    private JTextField ont2ndTextField;
    private JButton button_1;
    private JPanel panel_5;
    private JProgressBar progressBar_1;
    private JButton AnalysisButton;
    private JButton CompareButton;
    private GridBagConstraints gbc_AnalysisButton;
    private JPanel outputPanel;
    
    //For analysis panel
//    public static  JTabbedPane outputPane = new JTabbedPane();
    private JButton btnNewButton;
    private JTabbedPane tabbedPane_1;
    private JTabbedPane outputPane;
    private JPanel GeneralPanel1;
    private JPanel ConsistencyPanel1;
    private JPanel RichnessPanel1;
    private JPanel GeneralPanel2;
    private JPanel ConsistencyPanel2;
    private JPanel RichnessPanel2;
    public static JTextArea GeneralTextArea1;
    public static JTextArea GeneralTextArea2;
    public static JTextArea ConsistencyTextArea1;
    public static JTextArea ConsistencyTextArea2;
    public static JTextArea RichnessTextArea1;
    public static JTextArea RichnessTextArea2;
    private JPanel matchPanel;
    private JPanel evalMatchPanel;
    private JPanel mergePanel;
    private JPanel evalMergingPanel;
    private JPanel SemSimPanel;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane_1;
    private JScrollPane scrollPane_2;
    private JScrollPane scrollPane_3;
    private JScrollPane scrollPane_4;
    private JScrollPane scrollPane_5;
    private JLabel label_6;
    private JPanel panel_6;
    private JLabel lblOntology_1;
    private JButton minusbutton;
    private JButton plusbutton;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JLabel lblTypeOfSimilarity;
    private JComboBox SimComboBox;
    private JPanel RunPanel2;
    private JProgressBar progressBar_2;
    private JButton Mergebtn;
    private JButton btnSaveOutput;
    private JLabel lblTypeOfAlgorithm_1;
    private JComboBox MergeTypeComboBox;
    private JTextArea OutputPartitioningTextAreaMerge;
    private JScrollPane scrollPane_6;
    private JButton btnViewGraph;
    private JPanel panel_10;

    //For Merge panel
    public static ArrayList<String> MergeModels;
    public static String numAlgorithm;
    private JPanel panel_11;
    private JPanel panel_12;
    public static JTextArea ontologyFileTextFieldMerge;
    private JLabel ShowModuleLabel;
  
    
	
    public static void main(String[] args) {
		MergeModels = new ArrayList<String>();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OPATgui window = new OPATgui();
					window.frmOapt.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OPATgui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmOapt = new JFrame();
		frmOapt.setTitle("OAPT: Ontoloyg Analysis & Partitioning Tool");
		frmOapt.setBounds(100, 100, 900, 800);
		frmOapt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmOapt.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel analyzPanel = new JPanel();
		tabbedPane.addTab("Analysis", null, analyzPanel, null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 106, 65, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		analyzPanel.setLayout(gridBagLayout);
		
		label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon("fig/logo5.png"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		analyzPanel.add(label_3, gbc);
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Selecting Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 1;
		analyzPanel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 531, 0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		label_4 = new JLabel("1st Ontology:");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 0;
		panel_4.add(label_4, gbc_label_4);
		
		ont1stTextField = new JTextField();
		ont1stTextField.setColumns(10);
		GridBagConstraints gbc_ont1stTextField = new GridBagConstraints();
		gbc_ont1stTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ont1stTextField.insets = new Insets(0, 0, 5, 5);
		gbc_ont1stTextField.gridx = 1;
		gbc_ont1stTextField.gridy = 0;
		panel_4.add(ont1stTextField, gbc_ont1stTextField);
		
		button = new JButton("Browes...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ontologyFileChooserButtonActionPerformed1(e);
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		panel_4.add(button, gbc_button);
		
		label_5 = new JLabel("2nd Ontology:");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.EAST;
		gbc_label_5.insets = new Insets(0, 0, 0, 5);
		gbc_label_5.gridx = 0;
		gbc_label_5.gridy = 1;
		panel_4.add(label_5, gbc_label_5);
		
		ont2ndTextField = new JTextField();
		ont2ndTextField.setColumns(10);
		GridBagConstraints gbc_ont2ndTextField = new GridBagConstraints();
		gbc_ont2ndTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ont2ndTextField.insets = new Insets(0, 0, 0, 5);
		gbc_ont2ndTextField.gridx = 1;
		gbc_ont2ndTextField.gridy = 1;
		panel_4.add(ont2ndTextField, gbc_ont2ndTextField);
		
		button_1 = new JButton("Browes...");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ontologyFileChooserButtonActionPerformed2(e);
			}
		});
		
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.gridx = 2;
		gbc_button_1.gridy = 1;
		panel_4.add(button_1, gbc_button_1);
		
		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Run", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 5, 0);
		gbc_panel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 2;
		analyzPanel.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 16, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		progressBar_1 = new JProgressBar();
		GridBagConstraints gbc_progressBar_1 = new GridBagConstraints();
		gbc_progressBar_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar_1.gridwidth = 2;
		gbc_progressBar_1.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar_1.gridx = 0;
		gbc_progressBar_1.gridy = 0;
		panel_5.add(progressBar_1, gbc_progressBar_1);
		
		AnalysisButton = new JButton("Analysis");
		AnalysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnalysisButtonActionPerformed (e);
			}
		});
		GridBagConstraints gbc_btnNewButton_1;
		gbc_AnalysisButton = new GridBagConstraints();
		gbc_AnalysisButton.anchor = GridBagConstraints.EAST;
		gbc_AnalysisButton.insets = new Insets(0, 0, 0, 5);
		gbc_AnalysisButton.gridx = 0;
		gbc_AnalysisButton.gridy = 1;
		panel_5.add(AnalysisButton, gbc_AnalysisButton);
		
		CompareButton = new JButton("Compare");
		CompareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CompareButtonButtonActionPerformed(e);
			}
		});
		GridBagConstraints gbc_CompareButton = new GridBagConstraints();
		gbc_CompareButton.anchor = GridBagConstraints.WEST;
		gbc_CompareButton.gridx = 1;
		gbc_CompareButton.gridy = 1;
		panel_5.add(CompareButton, gbc_CompareButton);
		
		outputPanel = new JPanel();
		outputPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GridBagConstraints gbc_outputPanel = new GridBagConstraints();
		gbc_outputPanel.fill = GridBagConstraints.BOTH;
		gbc_outputPanel.gridx = 0;
		gbc_outputPanel.gridy = 3;
		analyzPanel.add(outputPanel, gbc_outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWidths = new int[]{0, 0};
		gbl_outputPanel.rowHeights = new int[]{0, 0};
		gbl_outputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_outputPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		outputPanel.setLayout(gbl_outputPanel);
		
		outputPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_outputPane = new GridBagConstraints();
		gbc_outputPane.fill = GridBagConstraints.BOTH;
		gbc_outputPane.gridx = 0;
		gbc_outputPane.gridy = 0;
		outputPanel.add(outputPane, gbc_outputPane);
		
		GeneralPanel1 = new JPanel();
		outputPane.addTab("General Info 1st Ont.", null, GeneralPanel1, null);
		GridBagLayout gbl_GeneralPanel1 = new GridBagLayout();
		gbl_GeneralPanel1.columnWidths = new int[]{0, 0};
		gbl_GeneralPanel1.rowHeights = new int[]{0, 0};
		gbl_GeneralPanel1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_GeneralPanel1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		GeneralPanel1.setLayout(gbl_GeneralPanel1);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		GeneralPanel1.add(scrollPane, gbc_scrollPane);
		
		GeneralTextArea1 = new JTextArea();
		scrollPane.setViewportView(GeneralTextArea1);
		GeneralTextArea1.setWrapStyleWord(true);
		GeneralTextArea1.setLineWrap(true);
		
		ConsistencyPanel1 = new JPanel();
		outputPane.addTab("Consistency 1st Ont.", null, ConsistencyPanel1, null);
		GridBagLayout gbl_ConsistencyPanel1 = new GridBagLayout();
		gbl_ConsistencyPanel1.columnWidths = new int[]{0, 0};
		gbl_ConsistencyPanel1.rowHeights = new int[]{0, 0};
		gbl_ConsistencyPanel1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_ConsistencyPanel1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		ConsistencyPanel1.setLayout(gbl_ConsistencyPanel1);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		ConsistencyPanel1.add(scrollPane_1, gbc_scrollPane_1);
		
		ConsistencyTextArea1 = new JTextArea();
		scrollPane_1.setViewportView(ConsistencyTextArea1);
		
		RichnessPanel1 = new JPanel();
		outputPane.addTab("Richness 1st Ont.", null, RichnessPanel1, null);
		GridBagLayout gbl_RichnessPanel1 = new GridBagLayout();
		gbl_RichnessPanel1.columnWidths = new int[]{0, 0};
		gbl_RichnessPanel1.rowHeights = new int[]{0, 0};
		gbl_RichnessPanel1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_RichnessPanel1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		RichnessPanel1.setLayout(gbl_RichnessPanel1);
		
		scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 0;
		RichnessPanel1.add(scrollPane_2, gbc_scrollPane_2);
		
		RichnessTextArea1 = new JTextArea();
		scrollPane_2.setViewportView(RichnessTextArea1);
		
		GeneralPanel2 = new JPanel();
		outputPane.addTab("General Info 1st Ont.", null, GeneralPanel2, null);
		GridBagLayout gbl_GeneralPanel2 = new GridBagLayout();
		gbl_GeneralPanel2.columnWidths = new int[]{0, 0};
		gbl_GeneralPanel2.rowHeights = new int[]{0, 0};
		gbl_GeneralPanel2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_GeneralPanel2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		GeneralPanel2.setLayout(gbl_GeneralPanel2);
		
		scrollPane_3 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.gridx = 0;
		gbc_scrollPane_3.gridy = 0;
		GeneralPanel2.add(scrollPane_3, gbc_scrollPane_3);
		
		GeneralTextArea2 = new JTextArea();
		scrollPane_3.setViewportView(GeneralTextArea2);
		
		ConsistencyPanel2 = new JPanel();
		outputPane.addTab("Consistency 2nd Ont.", null, ConsistencyPanel2, null);
		GridBagLayout gbl_ConsistencyPanel2 = new GridBagLayout();
		gbl_ConsistencyPanel2.columnWidths = new int[]{0, 0};
		gbl_ConsistencyPanel2.rowHeights = new int[]{0, 0};
		gbl_ConsistencyPanel2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_ConsistencyPanel2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		ConsistencyPanel2.setLayout(gbl_ConsistencyPanel2);
		
		scrollPane_4 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_4 = new GridBagConstraints();
		gbc_scrollPane_4.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_4.gridx = 0;
		gbc_scrollPane_4.gridy = 0;
		ConsistencyPanel2.add(scrollPane_4, gbc_scrollPane_4);
		
		ConsistencyTextArea2 = new JTextArea();
		scrollPane_4.setViewportView(ConsistencyTextArea2);
		
		RichnessPanel2 = new JPanel();
		outputPane.addTab("Richness 2nd Ont.", null, RichnessPanel2, null);
		GridBagLayout gbl_RichnessPanel2 = new GridBagLayout();
		gbl_RichnessPanel2.columnWidths = new int[]{0, 0};
		gbl_RichnessPanel2.rowHeights = new int[]{0, 0};
		gbl_RichnessPanel2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_RichnessPanel2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		RichnessPanel2.setLayout(gbl_RichnessPanel2);
		
		scrollPane_5 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_5 = new GridBagConstraints();
		gbc_scrollPane_5.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_5.gridx = 0;
		gbc_scrollPane_5.gridy = 0;
		RichnessPanel2.add(scrollPane_5, gbc_scrollPane_5);
		
		RichnessTextArea2 = new JTextArea();
		scrollPane_5.setViewportView(RichnessTextArea2);
		
		
		JPanel partitionPanel = new JPanel();
		tabbedPane.addTab("Partitioning", null, partitionPanel, null);
		GridBagLayout gbl_PartitioningPanel = new GridBagLayout();
		gbl_PartitioningPanel.columnWidths = new int[]{198, 0};
		gbl_PartitioningPanel.rowHeights = new int[]{152, 108, 84, 75, 281, 0, 0};
		gbl_PartitioningPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_PartitioningPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		partitionPanel.setLayout(gbl_PartitioningPanel);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("fig/logo5.png"));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		partitionPanel.add(label, gbc_label);
		
		JPanel SelectionPanel = new JPanel();
		SelectionPanel.setBorder(new TitledBorder(null, "Selecting Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_SelectionPanel = new GridBagConstraints();
		gbc_SelectionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_SelectionPanel.fill = GridBagConstraints.BOTH;
		gbc_SelectionPanel.gridx = 0;
		gbc_SelectionPanel.gridy = 1;
		partitionPanel.add(SelectionPanel, gbc_SelectionPanel);
		GridBagLayout gbl_SelectionPanel = new GridBagLayout();
		gbl_SelectionPanel.columnWidths = new int[]{0, 622, 0, 0};
		gbl_SelectionPanel.rowHeights = new int[]{0, 0};
		gbl_SelectionPanel.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_SelectionPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		SelectionPanel.setLayout(gbl_SelectionPanel);
		
		JLabel lblOntology = new JLabel("Ontology :");
		GridBagConstraints gbc_lblOntology = new GridBagConstraints();
		gbc_lblOntology.fill = GridBagConstraints.VERTICAL;
		gbc_lblOntology.insets = new Insets(0, 0, 0, 5);
		gbc_lblOntology.gridx = 0;
		gbc_lblOntology.gridy = 0;
		SelectionPanel.add(lblOntology, gbc_lblOntology);
		
		ontologyFileTextField = new JTextArea();
		GridBagConstraints gbc_ontologyFileTextField = new GridBagConstraints();
		gbc_ontologyFileTextField.insets = new Insets(0, 0, 0, 5);
		gbc_ontologyFileTextField.fill = GridBagConstraints.BOTH;
		gbc_ontologyFileTextField.gridx = 1;
		gbc_ontologyFileTextField.gridy = 0;
		SelectionPanel.add(ontologyFileTextField, gbc_ontologyFileTextField);
		
		ontologyFileChooserButton = new JButton("Browes...");
		   ontologyFileChooserButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
//	                ontologyFileChooserButtonActionPerformed(evt);
	            	 JFileChooser fileChooser = new JFileChooser(".");
	  	           fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	  	           fileChooser.setName("Open");
	  	           fileChooser.setToolTipText("Please input a file");
	  	           int rVal = fileChooser.showOpenDialog(null);
	  	           if (rVal == JFileChooser.APPROVE_OPTION) {
	  	          	 ontologyFileTextField.setText(fileChooser.getSelectedFile().toString());
	  	           }
	            }
	        });
		   
		GridBagConstraints gbc_btnBrowes = new GridBagConstraints();
		gbc_btnBrowes.gridx = 2;
		gbc_btnBrowes.gridy = 0;
		SelectionPanel.add(ontologyFileChooserButton, gbc_btnBrowes);
		
		JPanel arguments = new JPanel();
		arguments.setBorder(new TitledBorder(null, "Required Arguments", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_arguments = new GridBagConstraints();
		gbc_arguments.insets = new Insets(0, 0, 5, 0);
		gbc_arguments.fill = GridBagConstraints.BOTH;
		gbc_arguments.gridx = 0;
		gbc_arguments.gridy = 2;
		partitionPanel.add(arguments, gbc_arguments);
		GridBagLayout gbl_arguments = new GridBagLayout();
		gbl_arguments.columnWidths = new int[]{0, 181, 0, 98, 0, 0};
		gbl_arguments.rowHeights = new int[]{64, 0};
		gbl_arguments.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_arguments.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		arguments.setLayout(gbl_arguments);
		
		JLabel lblTypeOfAlgorithm = new JLabel("Type of Algorithm ");
		GridBagConstraints gbc_lblTypeOfAlgorithm = new GridBagConstraints();
		gbc_lblTypeOfAlgorithm.insets = new Insets(0, 0, 0, 5);
		gbc_lblTypeOfAlgorithm.anchor = GridBagConstraints.EAST;
		gbc_lblTypeOfAlgorithm.gridx = 0;
		gbc_lblTypeOfAlgorithm.gridy = 0;
		arguments.add(lblTypeOfAlgorithm, gbc_lblTypeOfAlgorithm);
		
		NumCHLabel = new JLabel("Number of Module ");
		GridBagConstraints gbc_NumCHLabel = new GridBagConstraints();
		gbc_NumCHLabel.anchor = GridBagConstraints.EAST;
		gbc_NumCHLabel.insets = new Insets(0, 0, 0, 5);
		gbc_NumCHLabel.gridx = 2;
		gbc_NumCHLabel.gridy = 0;
		arguments.add(NumCHLabel, gbc_NumCHLabel);
		
		
		SuggestButton = new JButton("Module Number Recommender");
		GridBagConstraints gbc_SuggestButton = new GridBagConstraints();
		gbc_SuggestButton.anchor = GridBagConstraints.WEST;
		gbc_SuggestButton.gridx = 4;
		gbc_SuggestButton.gridy = 0;
		arguments.add(SuggestButton, gbc_SuggestButton);
		SuggestButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	SuggestButtonActionPerformed(evt); 
	            }
	        });
		
		ListTypeComboBox = new JComboBox();
		 ListTypeComboBox.addActionListener (new ActionListener () {
	            public void actionPerformed(ActionEvent e) {
				if (ListTypeComboBox.getSelectedItem().toString().equals("AxCOnt") || ListTypeComboBox.getSelectedItem().toString().equals("PBM")){
        			NumCHLabel.setEnabled(false);
        			NumCHTextField.setEnabled(false);
        			SuggestButton.setEnabled(false);
        	}else{
        		NumCHLabel.setEnabled(true);
    			NumCHTextField.setEnabled(true);
    			SuggestButton.setEnabled(true);
        	}
			}
		});
		ListTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"SeeCOnt", "PBM", "AxCOnt"}));
		GridBagConstraints gbc_ListTypeComboBox = new GridBagConstraints();
		gbc_ListTypeComboBox.anchor = GridBagConstraints.WEST;
		gbc_ListTypeComboBox.insets = new Insets(0, 0, 0, 5);
		gbc_ListTypeComboBox.gridx = 1;
		gbc_ListTypeComboBox.gridy = 0;
		arguments.add(ListTypeComboBox, gbc_ListTypeComboBox);
		
	
		
		NumCHTextField = new JTextField();
		GridBagConstraints gbc_NumCHTextField = new GridBagConstraints();
		gbc_NumCHTextField.insets = new Insets(0, 0, 0, 5);
		gbc_NumCHTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_NumCHTextField.gridx = 3;
		gbc_NumCHTextField.gridy = 0;
		arguments.add(NumCHTextField, gbc_NumCHTextField);
		NumCHTextField.setColumns(10);
		
		JPanel runProgress = new JPanel();
		runProgress.setBorder(new TitledBorder(null, "Run", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_runProgress = new GridBagConstraints();
		gbc_runProgress.insets = new Insets(0, 0, 5, 0);
		gbc_runProgress.fill = GridBagConstraints.BOTH;
		gbc_runProgress.gridx = 0;
		gbc_runProgress.gridy = 3;
		partitionPanel.add(runProgress, gbc_runProgress);
		GridBagLayout gbl_runProgress = new GridBagLayout();
		gbl_runProgress.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_runProgress.rowHeights = new int[]{56, 0};
		gbl_runProgress.columnWeights = new double[]{1.0, Double.MIN_VALUE, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_runProgress.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		runProgress.setLayout(gbl_runProgress);
		
		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 5;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 0;
		runProgress.add(progressBar, gbc_progressBar);
		
		ExecuteButton = new JButton("Execute");
		GridBagConstraints gbc_ExecuteButton = new GridBagConstraints();
		gbc_ExecuteButton.insets = new Insets(0, 0, 0, 5);
		gbc_ExecuteButton.gridx = 5;
		gbc_ExecuteButton.gridy = 0;
		runProgress.add(ExecuteButton, gbc_ExecuteButton);
		ExecuteButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	                ExecuteButtonActionPerformed(evt); 
	            }
	        });
		
		SaveButton = new JButton("Save Output");
		GridBagConstraints gbc_SaveButton = new GridBagConstraints();
		gbc_SaveButton.gridx = 6;
		gbc_SaveButton.gridy = 0;
		runProgress.add(SaveButton, gbc_SaveButton);
		SaveButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			SaveButtonActionPerformed( evt) ;  
			     }
		});
	         
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		partitionPanel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{255, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JTextArea textArea_1 = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 0;
		panel_1.add(textArea_1, gbc_textArea_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Visual View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 5;
		partitionPanel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{14, 160, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		panel_11 = new JPanel();
		GridBagConstraints gbc_panel_11 = new GridBagConstraints();
		gbc_panel_11.insets = new Insets(0, 0, 5, 5);
		gbc_panel_11.fill = GridBagConstraints.BOTH;
		gbc_panel_11.gridx = 0;
		gbc_panel_11.gridy = 0;
		panel_3.add(panel_11, gbc_panel_11);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_11.rowHeights = new int[]{47, 0};
		gbl_panel_11.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_11.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_11.setLayout(gbl_panel_11);
		
		ViewHierarchyButton = new JButton("View Hierarchy");
		GridBagConstraints gbc_ViewHierarchyButton = new GridBagConstraints();
		gbc_ViewHierarchyButton.insets = new Insets(0, 0, 0, 5);
		gbc_ViewHierarchyButton.gridx = 0;
		gbc_ViewHierarchyButton.gridy = 0;
		panel_11.add(ViewHierarchyButton, gbc_ViewHierarchyButton);
	       ViewHierarchyButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	Hierarchy.Run();
	            }
	        });
		
		
		ViewGraphButton = new JButton("View Graph");
		GridBagConstraints gbc_ViewGraphButton = new GridBagConstraints();
		gbc_ViewGraphButton.insets = new Insets(0, 0, 0, 5);
		gbc_ViewGraphButton.gridx = 1;
		gbc_ViewGraphButton.gridy = 0;
		panel_11.add(ViewGraphButton, gbc_ViewGraphButton);
		ViewGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (Coordinator.FinishPartitioning== true){
            		LockedGUI();
            		Coordinator.levelDetail = Integer.parseInt(JOptionPane.showInputDialog("Enter level's detail of showing ontology (1 means all, 2 means half of the nodes)"));
            		Coordinator.argVisulization = 0;
            		Visualization.Run(Coordinator.argVisulization,con); //0:shows that visualization called from partitioning panel
            		Un_LockedGUI();
            	}else{
            		JOptionPane.showMessageDialog(null, "Your selected ontology does not partition, please before seeing the visual output, do the partitioning job.", "Error", JOptionPane.INFORMATION_MESSAGE);
            	}
            }
        });
		
		ViewAllGraphButton = new JButton("View Whole Graph");
		GridBagConstraints gbc_ViewAllGraphButton = new GridBagConstraints();
		gbc_ViewAllGraphButton.insets = new Insets(0, 0, 0, 5);
		gbc_ViewAllGraphButton.gridx = 2;
		gbc_ViewAllGraphButton.gridy = 0;
		panel_11.add(ViewAllGraphButton, gbc_ViewAllGraphButton);
		ViewAllGraphButton.addActionListener(new java.awt.event.ActionListener() 
		{
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (Coordinator.FinishPartitioning== true){
            		LockedGUI();
            		Coordinator.levelDetail = Integer.parseInt(JOptionPane.showInputDialog("Enter level's detail of showing ontology (1 means all, 2 means half of the nodes)"));
            		Visualization.Run(Coordinator.argVisulization,con);
            		//Visualization2.Run(con); 
            		Un_LockedGUI();
            	}else{
            		JOptionPane.showMessageDialog(null, "Your selected ontology does not partition, please before seeing the visual output, do the partitioning job.", "Error", JOptionPane.INFORMATION_MESSAGE);
            	}
            }
        });
		
		showAllModuleCheckBox = new JCheckBox("Show All Modules");
		showAllModuleCheckBox.setSelected(true);
		showAllModuleCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (showAllModuleCheckBox.isSelected()) {
					ShowModuleLabel.setEnabled(false);
					NumModuleTextField.setEnabled(false);
		        } else {
		        	ShowModuleLabel.setEnabled(true);
		        	NumModuleTextField.setEnabled(true);
		        }
			}
		});
		GridBagConstraints gbc_showAllModuleCheckBox = new GridBagConstraints();
		gbc_showAllModuleCheckBox.insets = new Insets(0, 0, 0, 5);
		gbc_showAllModuleCheckBox.gridx = 3;
		gbc_showAllModuleCheckBox.gridy = 0;
		panel_11.add(showAllModuleCheckBox, gbc_showAllModuleCheckBox);
		
		ShowModuleLabel = new JLabel("Show Module:");
		GridBagConstraints gbc_ShowModuleLabel = new GridBagConstraints();
		gbc_ShowModuleLabel.insets = new Insets(0, 0, 0, 5);
		gbc_ShowModuleLabel.anchor = GridBagConstraints.EAST;
		gbc_ShowModuleLabel.gridx = 4;
		gbc_ShowModuleLabel.gridy = 0;
		panel_11.add(ShowModuleLabel, gbc_ShowModuleLabel);
		
		NumModuleTextField = new JTextField();
		GridBagConstraints gbc_NumModuleTextField = new GridBagConstraints();
		gbc_NumModuleTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_NumModuleTextField.gridx = 5;
		gbc_NumModuleTextField.gridy = 0;
		panel_11.add(NumModuleTextField, gbc_NumModuleTextField);
		NumModuleTextField.setColumns(10);
		
		panel_12 = new JPanel();
		GridBagConstraints gbc_panel_12 = new GridBagConstraints();
		gbc_panel_12.insets = new Insets(0, 0, 0, 5);
		gbc_panel_12.fill = GridBagConstraints.BOTH;
		gbc_panel_12.gridx = 0;
		gbc_panel_12.gridy = 1;
		panel_3.add(panel_12, gbc_panel_12);
		GridBagLayout gbl_panel_12 = new GridBagLayout();
		gbl_panel_12.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_12.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_12.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_12.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_12.setLayout(gbl_panel_12);
		
		includeSubclassLinksCheckBox = new JCheckBox("Include subclass links");
		includeSubclassLinksCheckBox.setSelected(true);
		GridBagConstraints gbc_includeSubclassLinksCheckBox = new GridBagConstraints();
		gbc_includeSubclassLinksCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_includeSubclassLinksCheckBox.gridx = 0;
		gbc_includeSubclassLinksCheckBox.gridy = 0;
		panel_12.add(includeSubclassLinksCheckBox, gbc_includeSubclassLinksCheckBox);
		includeSubclassLinksCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                includeSubclassLinksCheckBoxStateChanged(evt);
            }
        });
		
		strengthSubclassLinksLabel = new JLabel("Strength subclass links");
		GridBagConstraints gbc_strengthSubclassLinksLabel = new GridBagConstraints();
		gbc_strengthSubclassLinksLabel.insets = new Insets(0, 0, 5, 5);
		gbc_strengthSubclassLinksLabel.gridx = 1;
		gbc_strengthSubclassLinksLabel.gridy = 0;
		panel_12.add(strengthSubclassLinksLabel, gbc_strengthSubclassLinksLabel);
		
		strengthSubclassLinksSpinner = new JSpinner(new SpinnerNumberModel(1, 0.0, 10.0, 0.5));
//		strengthSubclassLinksSpinner.setModel(new SpinnerListModel(new String[] {"1", "0.0", "10.0", "0.5"}));
		GridBagConstraints gbc_strengthSubclassLinksSpinner = new GridBagConstraints();
		gbc_strengthSubclassLinksSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_strengthSubclassLinksSpinner.gridx = 2;
		gbc_strengthSubclassLinksSpinner.gridy = 0;
		panel_12.add(strengthSubclassLinksSpinner, gbc_strengthSubclassLinksSpinner);
		
		linkTypeLabel = new JLabel("Link Type");
		GridBagConstraints gbc_linkTypeLabel = new GridBagConstraints();
		gbc_linkTypeLabel.anchor = GridBagConstraints.EAST;
		gbc_linkTypeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_linkTypeLabel.gridx = 3;
		gbc_linkTypeLabel.gridy = 0;
		panel_12.add(linkTypeLabel, gbc_linkTypeLabel);
		
		linkTypeComboBox = new JComboBox();
		linkTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Edge", "UniEdge"}));
		GridBagConstraints gbc_linkTypeComboBox = new GridBagConstraints();
		gbc_linkTypeComboBox.anchor = GridBagConstraints.WEST;
		gbc_linkTypeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_linkTypeComboBox.gridx = 4;
		gbc_linkTypeComboBox.gridy = 0;
		panel_12.add(linkTypeComboBox, gbc_linkTypeComboBox);
		
		includePropertyLinksCheckBox = new JCheckBox("Include property links");
		includePropertyLinksCheckBox.setSelected(true);
		GridBagConstraints gbc_includePropertyLinksCheckBox = new GridBagConstraints();
		gbc_includePropertyLinksCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_includePropertyLinksCheckBox.gridx = 0;
		gbc_includePropertyLinksCheckBox.gridy = 1;
		panel_12.add(includePropertyLinksCheckBox, gbc_includePropertyLinksCheckBox);
		
		strengthPropertyLinksLabel = new JLabel("Strength property links");
		GridBagConstraints gbc_strengthPropertyLinksLabel = new GridBagConstraints();
		gbc_strengthPropertyLinksLabel.insets = new Insets(0, 0, 5, 5);
		gbc_strengthPropertyLinksLabel.gridx = 1;
		gbc_strengthPropertyLinksLabel.gridy = 1;
		panel_12.add(strengthPropertyLinksLabel, gbc_strengthPropertyLinksLabel);
		
		strengthPropertyLinksSpinner = new JSpinner(new SpinnerNumberModel(1, 0.0, 10.0, 0.5));
//		strengthPropertyLinksSpinner.setModel(new SpinnerListModel(new String[] {"1", "0.0", "10.0", "0.5"}));
		GridBagConstraints gbc_strengthPropertyLinksSpinner = new GridBagConstraints();
		gbc_strengthPropertyLinksSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_strengthPropertyLinksSpinner.gridx = 2;
		gbc_strengthPropertyLinksSpinner.gridy = 1;
		panel_12.add(strengthPropertyLinksSpinner, gbc_strengthPropertyLinksSpinner);
		
		includeDefinitionLinksCheckBox = new JCheckBox("Include definition links");
		GridBagConstraints gbc_includeDefinitionLinksCheckBox = new GridBagConstraints();
		gbc_includeDefinitionLinksCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_includeDefinitionLinksCheckBox.gridx = 0;
		gbc_includeDefinitionLinksCheckBox.gridy = 2;
		panel_12.add(includeDefinitionLinksCheckBox, gbc_includeDefinitionLinksCheckBox);
		
		strengthDefinitionLinksLabel = new JLabel("Strength definition links");
		GridBagConstraints gbc_strengthDefinitionLinksLabel = new GridBagConstraints();
		gbc_strengthDefinitionLinksLabel.insets = new Insets(0, 0, 5, 5);
		gbc_strengthDefinitionLinksLabel.gridx = 1;
		gbc_strengthDefinitionLinksLabel.gridy = 2;
		panel_12.add(strengthDefinitionLinksLabel, gbc_strengthDefinitionLinksLabel);
		
		strengthDefinitionLinksSpinner = new JSpinner(new SpinnerNumberModel(1, 0.0, 10.0, 0.5));
		GridBagConstraints gbc_strengthDefinitionLinksSpinner = new GridBagConstraints();
		gbc_strengthDefinitionLinksSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_strengthDefinitionLinksSpinner.gridx = 2;
		gbc_strengthDefinitionLinksSpinner.gridy = 2;
		panel_12.add(strengthDefinitionLinksSpinner, gbc_strengthDefinitionLinksSpinner);
		
		includeDefinitionResourcesLabel = new JLabel("Included definition resources");
		GridBagConstraints gbc_includeDefinitionResourcesLabel = new GridBagConstraints();
		gbc_includeDefinitionResourcesLabel.anchor = GridBagConstraints.EAST;
		gbc_includeDefinitionResourcesLabel.insets = new Insets(0, 0, 5, 5);
		gbc_includeDefinitionResourcesLabel.gridx = 3;
		gbc_includeDefinitionResourcesLabel.gridy = 2;
		panel_12.add(includeDefinitionResourcesLabel, gbc_includeDefinitionResourcesLabel);
		
		includeDefinitionResourcesComboBox = new JComboBox();
		includeDefinitionResourcesComboBox.setModel(new DefaultComboBoxModel(new String[] {"Only properties", "All resources"}));
		GridBagConstraints gbc_includeDefinitionResourcesComboBox = new GridBagConstraints();
		gbc_includeDefinitionResourcesComboBox.anchor = GridBagConstraints.WEST;
		gbc_includeDefinitionResourcesComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_includeDefinitionResourcesComboBox.gridx = 4;
		gbc_includeDefinitionResourcesComboBox.gridy = 2;
		panel_12.add(includeDefinitionResourcesComboBox, gbc_includeDefinitionResourcesComboBox);
		
		includeSubstringLinksCheckBox = new JCheckBox("Include substring links");
		GridBagConstraints gbc_includeSubstringLinksCheckBox = new GridBagConstraints();
		gbc_includeSubstringLinksCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_includeSubstringLinksCheckBox.gridx = 0;
		gbc_includeSubstringLinksCheckBox.gridy = 3;
		panel_12.add(includeSubstringLinksCheckBox, gbc_includeSubstringLinksCheckBox);
		
		strengthSubstringLinksLabel = new JLabel("Strength substring links");
		GridBagConstraints gbc_strengthSubstringLinksLabel = new GridBagConstraints();
		gbc_strengthSubstringLinksLabel.insets = new Insets(0, 0, 5, 5);
		gbc_strengthSubstringLinksLabel.gridx = 1;
		gbc_strengthSubstringLinksLabel.gridy = 3;
		panel_12.add(strengthSubstringLinksLabel, gbc_strengthSubstringLinksLabel);
		
		strengthSubstringLinksSpinner = new JSpinner(new SpinnerNumberModel(1, 0.0, 10.0, 0.5));
		GridBagConstraints gbc_strengthSubstringLinksSpinner = new GridBagConstraints();
		gbc_strengthSubstringLinksSpinner.insets = new Insets(0, 0, 5, 5);
		gbc_strengthSubstringLinksSpinner.gridx = 2;
		gbc_strengthSubstringLinksSpinner.gridy = 3;
		panel_12.add(strengthSubstringLinksSpinner, gbc_strengthSubstringLinksSpinner);
		
		includeDistanceLinksCheckBox = new JCheckBox("Include distance links");
		GridBagConstraints gbc_includeDistanceLinksCheckBox = new GridBagConstraints();
		gbc_includeDistanceLinksCheckBox.insets = new Insets(0, 0, 0, 5);
		gbc_includeDistanceLinksCheckBox.gridx = 0;
		gbc_includeDistanceLinksCheckBox.gridy = 4;
		panel_12.add(includeDistanceLinksCheckBox, gbc_includeDistanceLinksCheckBox);
		
		strengthDistanceLinksLabel = new JLabel("Strength distance links");
		GridBagConstraints gbc_strengthDistanceLinksLabel = new GridBagConstraints();
		gbc_strengthDistanceLinksLabel.insets = new Insets(0, 0, 0, 5);
		gbc_strengthDistanceLinksLabel.gridx = 1;
		gbc_strengthDistanceLinksLabel.gridy = 4;
		panel_12.add(strengthDistanceLinksLabel, gbc_strengthDistanceLinksLabel);
		
		strengthDistanceLinksSpinner = new JSpinner(new SpinnerNumberModel(1, 0.0, 10.0, 0.5));
		GridBagConstraints gbc_strengthDistanceLinksSpinner = new GridBagConstraints();
		gbc_strengthDistanceLinksSpinner.insets = new Insets(0, 0, 0, 5);
		gbc_strengthDistanceLinksSpinner.gridx = 2;
		gbc_strengthDistanceLinksSpinner.gridy = 4;
		panel_12.add(strengthDistanceLinksSpinner, gbc_strengthDistanceLinksSpinner);
		
		maxDistanceLabel = new JLabel("Maximum distance");
		GridBagConstraints gbc_maxDistanceLabel = new GridBagConstraints();
		gbc_maxDistanceLabel.anchor = GridBagConstraints.EAST;
		gbc_maxDistanceLabel.insets = new Insets(0, 0, 0, 5);
		gbc_maxDistanceLabel.gridx = 3;
		gbc_maxDistanceLabel.gridy = 4;
		panel_12.add(maxDistanceLabel, gbc_maxDistanceLabel);
		
		maxDistanceSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10, 1));
		GridBagConstraints gbc_maxDistanceSpinner = new GridBagConstraints();
		gbc_maxDistanceSpinner.anchor = GridBagConstraints.WEST;
		gbc_maxDistanceSpinner.gridx = 4;
		gbc_maxDistanceSpinner.gridy = 4;
		panel_12.add(maxDistanceSpinner, gbc_maxDistanceSpinner);
		
		includeDistanceLinksCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                includeDistanceLinksCheckBoxStateChanged(evt);
            }
        });
		
		
		includeSubstringLinksCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                includeSubstringLinksCheckBoxStateChanged(evt);
            }
        });
		
		
		includeDefinitionLinksCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                includeDefinitionLinksCheckBoxStateChanged(evt);
            }
        });

		
		includePropertyLinksCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                includePropertyLinksCheckBoxStateChanged(evt);
            }
        });
	
		
		JPanel evalPartitioningPanel = new JPanel();
		tabbedPane.addTab("Partitioning Evaluation", null, evalPartitioningPanel, null);
		GridBagLayout gbl_evalPartitioningPanel = new GridBagLayout();
		gbl_evalPartitioningPanel.columnWidths = new int[]{0, 0};
		gbl_evalPartitioningPanel.rowHeights = new int[]{146, 0, 0, 0};
		gbl_evalPartitioningPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_evalPartitioningPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		evalPartitioningPanel.setLayout(gbl_evalPartitioningPanel);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("\\img\\logo1.png"));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 0);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 0;
		evalPartitioningPanel.add(label_2, gbc_label_2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Evaluation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		evalPartitioningPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 175, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		AvgLabel1 = new JLabel("Average Size:");
		GridBagConstraints gbc_lblAaa = new GridBagConstraints();
		gbc_lblAaa.insets = new Insets(0, 0, 0, 5);
		gbc_lblAaa.anchor = GridBagConstraints.EAST;
		gbc_lblAaa.gridx = 0;
		gbc_lblAaa.gridy = 0;
		panel.add(AvgLabel1, gbc_lblAaa);
		
		AvgTextBox1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 0, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 0;
		panel.add(AvgTextBox1, gbc_textField_1);
		AvgTextBox1.setColumns(10);
		
		AvgLabel2 = new JLabel("Average Coupling:");
		GridBagConstraints gbc_AvgLabel2 = new GridBagConstraints();
		gbc_AvgLabel2.anchor = GridBagConstraints.EAST;
		gbc_AvgLabel2.insets = new Insets(0, 0, 0, 5);
		gbc_AvgLabel2.gridx = 2;
		gbc_AvgLabel2.gridy = 0;
		panel.add(AvgLabel2, gbc_AvgLabel2);
		
		AvgTextBox2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 0, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 0;
		panel.add(AvgTextBox2, gbc_textField_2);
		AvgTextBox2.setColumns(10);
		
		AvgLabel3 = new JLabel("Average Cohesion:");
		GridBagConstraints gbc_lblAverageCohesion = new GridBagConstraints();
		gbc_lblAverageCohesion.anchor = GridBagConstraints.EAST;
		gbc_lblAverageCohesion.insets = new Insets(0, 0, 0, 5);
		gbc_lblAverageCohesion.gridx = 4;
		gbc_lblAverageCohesion.gridy = 0;
		panel.add(AvgLabel3, gbc_lblAverageCohesion);
		
		AvgTextBox3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 5;
		gbc_textField_3.gridy = 0;
		panel.add(AvgTextBox3, gbc_textField_3);
		AvgTextBox3.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		evalPartitioningPanel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		DetailLabel1 = new JLabel("Details Size");
		GridBagConstraints gbc_DetailLabel1 = new GridBagConstraints();
		gbc_DetailLabel1.insets = new Insets(0, 0, 5, 5);
		gbc_DetailLabel1.gridx = 0;
		gbc_DetailLabel1.gridy = 0;
		panel_2.add(DetailLabel1, gbc_DetailLabel1);
		
		DetailLabel2 = new JLabel("Details Coupling");
		GridBagConstraints gbc_DetailLabel2 = new GridBagConstraints();
		gbc_DetailLabel2.insets = new Insets(0, 0, 5, 5);
		gbc_DetailLabel2.gridx = 1;
		gbc_DetailLabel2.gridy = 0;
		panel_2.add(DetailLabel2, gbc_DetailLabel2);
		
		DetailLabel3 = new JLabel("Details Cohesion");
		GridBagConstraints gbc_DetailLabel3 = new GridBagConstraints();
		gbc_DetailLabel3.insets = new Insets(0, 0, 5, 0);
		gbc_DetailLabel3.gridx = 2;
		gbc_DetailLabel3.gridy = 0;
		panel_2.add(DetailLabel3, gbc_DetailLabel3);
		
		Result1_Details = new JTextArea();
		GridBagConstraints gbc_Result1_Details = new GridBagConstraints();
		gbc_Result1_Details.insets = new Insets(0, 0, 0, 5);
		gbc_Result1_Details.fill = GridBagConstraints.BOTH;
		gbc_Result1_Details.gridx = 0;
		gbc_Result1_Details.gridy = 1;
	    Result1_Details.setEditable(false);
        Result1_Details.setLineWrap(true);
        Result1_Details.setCaretPosition(0);
        Result1_Details.setWrapStyleWord(true);
        Result1_Details.setText("Your selected ontology does not partition");
		panel_2.add(Result1_Details, gbc_Result1_Details);
//		ResultScrollPane1.setViewportView(Result1_Details);
		
		Result2_Details = new JTextArea();
		GridBagConstraints gbc_Result2_Details = new GridBagConstraints();
		gbc_Result2_Details.insets = new Insets(0, 0, 0, 5);
		gbc_Result2_Details.fill = GridBagConstraints.BOTH;
		gbc_Result2_Details.gridx = 1;
		gbc_Result2_Details.gridy = 1;
		Result2_Details.setEditable(false);
        Result2_Details.setLineWrap(true);
        Result2_Details.setCaretPosition(0);
        Result2_Details.setWrapStyleWord(true);
        Result2_Details.setText("Your selected ontology does not partition");
		panel_2.add(Result2_Details, gbc_Result2_Details);
		
		Result3_Details = new JTextArea();
		GridBagConstraints gbc_Result3_Details = new GridBagConstraints();
		gbc_Result3_Details.fill = GridBagConstraints.BOTH;
		gbc_Result3_Details.gridx = 2;
		gbc_Result3_Details.gridy = 1;
        Result3_Details.setEditable(false);
        Result3_Details.setLineWrap(true);
        Result3_Details.setCaretPosition(0);
        Result3_Details.setWrapStyleWord(true);
        Result3_Details.setText("Your selected ontology does not partition");
		panel_2.add(Result3_Details, gbc_Result3_Details);
		
//		matchPanel = new JPanel();
		MatchingPanel matchPanel = new MatchingPanel();
		tabbedPane.addTab("Matching", null, matchPanel, null);
		
//		evalMatchPanel = new JPanel();
		EvalPanelMatching evalMatchPanel = new EvalPanelMatching();
		tabbedPane.addTab("Matching Evaluation", null, evalMatchPanel, null);
		
		mergePanel = new JPanel();
		tabbedPane.addTab("Merging", null, mergePanel, null);
		GridBagLayout gbl_mergePanel = new GridBagLayout();
		gbl_mergePanel.columnWidths = new int[]{0, 0};
		gbl_mergePanel.rowHeights = new int[]{0, 135, 46, 72, 0, 0, 0};
		gbl_mergePanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_mergePanel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		mergePanel.setLayout(gbl_mergePanel);
		
		label_6 = new JLabel("");
		label_6.setIcon(new ImageIcon("fig/logo5.png"));
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.insets = new Insets(0, 0, 5, 0);
		gbc_label_6.gridx = 0;
		gbc_label_6.gridy = 0; 
		mergePanel.add(label_6, gbc_label_6);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Selecting Ontology", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 1;
		mergePanel.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 627, 0, 0};
		gbl_panel_6.rowHeights = new int[]{100, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		lblOntology_1 = new JLabel("Ontology");
		GridBagConstraints gbc_lblOntology_1 = new GridBagConstraints();
		gbc_lblOntology_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblOntology_1.gridx = 0;
		gbc_lblOntology_1.gridy = 0;
		panel_6.add(lblOntology_1, gbc_lblOntology_1);
		
		ontologyFileTextFieldMerge = new JTextArea();
		GridBagConstraints gbc_ontologyFileTextFieldMerge = new GridBagConstraints();
		gbc_ontologyFileTextFieldMerge.insets = new Insets(0, 0, 0, 5);
		gbc_ontologyFileTextFieldMerge.fill = GridBagConstraints.BOTH;
		gbc_ontologyFileTextFieldMerge.gridx = 1;
		gbc_ontologyFileTextFieldMerge.gridy = 0;
		panel_6.add(ontologyFileTextFieldMerge, gbc_ontologyFileTextFieldMerge);
		
		panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.fill = GridBagConstraints.BOTH;
		gbc_panel_10.gridx = 2;
		gbc_panel_10.gridy = 0;
		panel_6.add(panel_10, gbc_panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{0, 0};
		gbl_panel_10.rowHeights = new int[]{0, 0, 0};
		gbl_panel_10.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		plusbutton = new JButton("  Add Ontologies  ");
		plusbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlusbuttonActionPerformed(); 
			}
		});
	        
		GridBagConstraints gbc_plusbutton = new GridBagConstraints();
		gbc_plusbutton.insets = new Insets(0, 0, 5, 0);
		gbc_plusbutton.gridx = 0;
		gbc_plusbutton.gridy = 0;
		panel_10.add(plusbutton, gbc_plusbutton);
		
		minusbutton = new JButton("Delete Ontologies");
		minusbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MinusbuttonActionPerformed(); 
			}
		});
		GridBagConstraints gbc_minusbutton = new GridBagConstraints();
		gbc_minusbutton.gridx = 0;
		gbc_minusbutton.gridy = 1;
		panel_10.add(minusbutton, gbc_minusbutton);
		
		RunPanel2 = new JPanel();
		RunPanel2.setBorder(new TitledBorder(null, "Run", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_RunPanel2 = new GridBagConstraints();
		gbc_RunPanel2.insets = new Insets(0, 0, 5, 0);
		gbc_RunPanel2.fill = GridBagConstraints.BOTH;
		gbc_RunPanel2.gridx = 0;
		gbc_RunPanel2.gridy = 2;
		mergePanel.add(RunPanel2, gbc_RunPanel2);
		GridBagLayout gbl_RunPanel2 = new GridBagLayout();
		gbl_RunPanel2.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_RunPanel2.rowHeights = new int[]{0, 0};
		gbl_RunPanel2.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_RunPanel2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		RunPanel2.setLayout(gbl_RunPanel2);
		
		progressBar_2 = new JProgressBar();
		GridBagConstraints gbc_progressBar_2 = new GridBagConstraints();
		gbc_progressBar_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar_2.gridwidth = 2;
		gbc_progressBar_2.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar_2.gridx = 0;
		gbc_progressBar_2.gridy = 0;
		RunPanel2.add(progressBar_2, gbc_progressBar_2);
		
		Mergebtn = new JButton("Merge");
		Mergebtn.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	MergeButtonActionPerformed(evt); 
	            }
	        });
		
		GridBagConstraints gbc_Mergebtn = new GridBagConstraints();
		gbc_Mergebtn.insets = new Insets(0, 0, 0, 5);
		gbc_Mergebtn.gridx = 2;
		gbc_Mergebtn.gridy = 0;
		RunPanel2.add(Mergebtn, gbc_Mergebtn);
		
		btnSaveOutput = new JButton("Save Output");
		GridBagConstraints gbc_btnSaveOutput = new GridBagConstraints();
		gbc_btnSaveOutput.gridx = 3;
		gbc_btnSaveOutput.gridy = 0;
		RunPanel2.add(btnSaveOutput, gbc_btnSaveOutput);
		
		panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Selecting Measures", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.insets = new Insets(0, 0, 5, 0);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 3;
		mergePanel.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{0, 0, 174, 0, 0, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0};
		gbl_panel_7.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		lblTypeOfSimilarity = new JLabel("Type of Similarity");
		GridBagConstraints gbc_lblTypeOfSimilarity = new GridBagConstraints();
		gbc_lblTypeOfSimilarity.insets = new Insets(0, 0, 0, 5);
		gbc_lblTypeOfSimilarity.anchor = GridBagConstraints.EAST;
		gbc_lblTypeOfSimilarity.gridx = 0;
		gbc_lblTypeOfSimilarity.gridy = 0;
		panel_7.add(lblTypeOfSimilarity, gbc_lblTypeOfSimilarity);
		
		SimComboBox = new JComboBox();
		SimComboBox.setModel(new DefaultComboBoxModel(new String[] {"IC", "Structural Sim"}));
		GridBagConstraints gbc_SimComboBox = new GridBagConstraints();
		gbc_SimComboBox.insets = new Insets(0, 0, 0, 5);
		gbc_SimComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_SimComboBox.gridx = 1;
		gbc_SimComboBox.gridy = 0;
		panel_7.add(SimComboBox, gbc_SimComboBox);
		
		lblTypeOfAlgorithm_1 = new JLabel("Type of Algorithm");
		GridBagConstraints gbc_lblTypeOfAlgorithm_1 = new GridBagConstraints();
		gbc_lblTypeOfAlgorithm_1.anchor = GridBagConstraints.EAST;
		gbc_lblTypeOfAlgorithm_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblTypeOfAlgorithm_1.gridx = 3;
		gbc_lblTypeOfAlgorithm_1.gridy = 0;
		panel_7.add(lblTypeOfAlgorithm_1, gbc_lblTypeOfAlgorithm_1);
		
		MergeTypeComboBox = new JComboBox();
		MergeTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"GraphMerge", "GenericMerge", "APIMerge", "MatchingMerge"}));
		GridBagConstraints gbc_MergeTypeComboBox = new GridBagConstraints();
		gbc_MergeTypeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_MergeTypeComboBox.gridx = 4;
		gbc_MergeTypeComboBox.gridy = 0;
		panel_7.add(MergeTypeComboBox, gbc_MergeTypeComboBox);
		
		panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.BOTH;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 4;
		mergePanel.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[]{0, 0};
		gbl_panel_8.rowHeights = new int[]{0, 0};
		gbl_panel_8.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_8.setLayout(gbl_panel_8);
		
		scrollPane_6 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_6 = new GridBagConstraints();
		gbc_scrollPane_6.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_6.gridx = 0;
		gbc_scrollPane_6.gridy = 0;
		panel_8.add(scrollPane_6, gbc_scrollPane_6);
		
		OutputPartitioningTextAreaMerge = new JTextArea();
		scrollPane_6.setViewportView(OutputPartitioningTextAreaMerge);
		
		panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "Visual View", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 5;
		mergePanel.add(panel_9, gbc_panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[]{0, 0};
		gbl_panel_9.rowHeights = new int[]{0, 0};
		gbl_panel_9.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_9.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_9.setLayout(gbl_panel_9);
		
		btnViewGraph = new JButton("View Graph");
		GridBagConstraints gbc_btnViewGraph = new GridBagConstraints();
		gbc_btnViewGraph.gridx = 0;
		gbc_btnViewGraph.gridy = 0;
		panel_9.add(btnViewGraph, gbc_btnViewGraph);
		btnViewGraph.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            		LockedGUI();
            		Coordinator.argVisulization = 1;
            		Visualization.Run(Coordinator.argVisulization,con);  //1:shows that visualization called from merging panel
            		Un_LockedGUI();
            }
        });
		
		evalMergingPanel = new JPanel();
		tabbedPane.addTab("Merging Evaluation", null, evalMergingPanel, null);
		
		SemSimPanel = new JPanel();
		tabbedPane.addTab("Semantic Similarity", null, SemSimPanel, null);
		//ImageIcon img2 = new ImageIcon(this.getClass().getResource("/logo1.png").getFile());
		//ImageIcon img2 = new ImageIcon("/logo1.png");

	}
//}
	private void SuggestButtonActionPerformed(ActionEvent evt) {
//    	Cleaning_Allthings.clean(1); // 1 is the index of the "Partitioning" tab TO:DO:Samira
    	
    	int optimalNumCH=1;
    	NameAddressOnt = ontologyFileTextField.getText().trim();
    	File ontologyFile = new File(NameAddressOnt);
    	if (NameAddressOnt.length() <= 0){
    		  JOptionPane.showMessageDialog(null, "Please Select your Ontology File", "Error", JOptionPane.ERROR_MESSAGE);
    		  return;
    	} else if (!ontologyFile.exists()) {
            JOptionPane.showMessageDialog(null,"The ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
            return;
    	}else{
    		  LockedGUI();
    		  double start=System.currentTimeMillis();
    		   FindOptimalClusterIntractive IOP;
    		  if(con.CheckBuildModel==false)
    			  IOP=new FindOptimalClusterIntractive(NameAddressOnt);
    		  else
    			   IOP=new FindOptimalClusterIntractive(con.MB);
    		  //FindOptimalCluster OP=new FindOptimalCluster(NameAddressOnt);
    		 optimalNumCH = IOP.FindOptimalClusterFunc();
    		  		   
    		  double end1=System.currentTimeMillis();
    		  System.out.println("The optimal num. of partition time---->"+(end1-start)*.001+"\t sec");
              SelectCH CH=new SelectCH(IOP.getModelBuild());
    		  CH.Run();
    		  Coordinator.KNumCH=optimalNumCH;
    		  NumCHTextField.setText(String.valueOf(optimalNumCH));
    		  Un_LockedGUI();
    }
	}
	
	 private void ExecuteButtonActionPerformed(ActionEvent evt) {
	    	
//	    	Cleaning_Allthings.clean(1);
	    	Coordinator.FinishPartitioning = false;
	    	ViewGraphButton.setEnabled(false);
	    	ViewAllGraphButton.setEnabled(false);
	    	ViewHierarchyButton.setEnabled(false);
	    	
	    	NameAddressOnt = ontologyFileTextField.getText().trim();
	    	File ontologyFile = new File(NameAddressOnt);
	    	if (NameAddressOnt.length() <= 0){
	  		  JOptionPane.showMessageDialog(null, "Please Select your Ontology File", "Error", JOptionPane.ERROR_MESSAGE);
	  		  return;
	  	    } else if (!ontologyFile.exists()) {
	            JOptionPane.showMessageDialog(null,"The ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
	            return;
	  	    }
	  	    
	        final String StrNumCH = NumCHTextField.getText().trim();
	        if (StrNumCH.length() <= 0 && ListTypeComboBox.getSelectedItem() == "SeeCOnt"){
	    		  JOptionPane.showMessageDialog(null, "Please Input the number of Cluster", "Error", JOptionPane.ERROR_MESSAGE);
	    		  return;
	    	    }
	        final int NumCH;
	        if (StrNumCH.length()>0) {
	        	NumCH=Integer.parseInt(StrNumCH);
	        }else{
	        	NumCH=0;			
	        	}
	        Coordinator.KNumCH = NumCH;
	                                       
	        if (ModelBuild.analysisTest == false){
	        	JOptionPane.showMessageDialog(null, "Your selected ontology is not analysis, Please at first do it", "Warning", JOptionPane.INFORMATION_MESSAGE);
	        }
	  
	        LockedGUI();
//	        MainFrame.tabbedPane.setEnabledAt(2, false); TO DO:samira
	        
	        if (NameAddressOnt.length() > 0 ) {
	        	Thread partition = new Thread()
	            {
	                @Override
	                public void run()
	                {
	                	
	                	this.setName("partition");
	                	String selectedType = (String) ListTypeComboBox.getSelectedItem();      //get the selected item
	                    Configuration config = new Configuration();
	                    config.init();
	                    String fp1 = NameAddressOnt;
	                    if (!fp1.startsWith("file:")) {
	                        fp1 = "" + fp1;
	                    }
	                    if(con.CheckBuildModel==false) con = new Controller(fp1);
	                    con.InitialRun(selectedType, Coordinator.KNumCH);  /////////*****main part*******////////////
	                    MainFrame.model1 =con.getOntModel();
//	                    MainFrame.tabbedPane.setEnabledAt(1, true);
	                    Coordinator.FinishPartitioning = true; // By this flag, the View Graph button can work
	                    ViewGraphButton.setEnabled(true);
	                    ViewAllGraphButton.setEnabled(true);
	                    ViewHierarchyButton.setEnabled(true);
	                    SaveButton.setEnabled(true);
	                    Un_LockedGUI();
//	                    MainFrame.tabbedPane.setEnabledAt(2, true);
	                }
	            };
	            partition.start(); 
	            
	        }else{
	        	JOptionPane.showMessageDialog(null, "Please Enter the Parameters (ontology file and the number of clusters)", "Error", JOptionPane.ERROR_MESSAGE);
	        	return;
	        } 
	    }
	    
     public void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
         	JFileChooser fileChooser = new JFileChooser(".");
             fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
             fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
             fileChooser.setName("Save as the RDF/XML format");
             fileChooser.setToolTipText("Please input a file to save");
             int rVal = fileChooser.showSaveDialog(null);
             if (rVal == JFileChooser.APPROVE_OPTION) {
                 File file = fileChooser.getSelectedFile(); 
                 String fp = file.getAbsolutePath();
                 int CHNo= con.getModules().size();
                 String s1=".";
                 int index=ModelBuild.fn1.indexOf(s1);
                 for (int cid=0; cid<CHNo; cid++){
                 	String srcName = ModelBuild.wd + ModelBuild.fn1.substring(0, index) + "_module_" + cid + ".owl"; // Read our created modules
                 	//if the user does not determine the extension, we add it to the name of file
                 	String destName = null;
                 	if (fp.contains(".") == false) {
                 			destName = fp + "_module_" +cid + ".owl";
                 		}else{
                 		    String []iname = fp.split("\\."); 
                 			destName = iname[0] + cid + "." + iname[1];
                 		}
             	File src = new File(srcName);
             	File target = new File(destName);

             	try {
 					Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
 				} catch (IOException e) {
 					e.printStackTrace();
 				}

             }
             }
         }
     private void ontologyFileChooserButtonActionPerformed1(ActionEvent evt) {
    	 JFileChooser fileChooser = new JFileChooser(".");
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         fileChooser.setName("Open");
         fileChooser.setToolTipText("Please input a file");
         int rVal = fileChooser.showOpenDialog(null);
         if (rVal == JFileChooser.APPROVE_OPTION) {
        	 ont1stTextField.setText(fileChooser.getSelectedFile().toString());
         }
    }
     private void ontologyFileChooserButtonActionPerformed2(ActionEvent evt) {
    	 JFileChooser fileChooser = new JFileChooser(".");
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         fileChooser.setName("Open");
         fileChooser.setToolTipText("Please input a file");
         int rVal = fileChooser.showOpenDialog(null);
         if (rVal == JFileChooser.APPROVE_OPTION) {
        	 ont2ndTextField.setText(fileChooser.getSelectedFile().toString());
         }
    }
     private void AnalysisButtonActionPerformed(ActionEvent evt) {
     	
//     	Cleaning_Allthings.clean(0); TO DO:samira
     	
     	NameAddressOnt1 = ont1stTextField.getText().trim();
     	
     	ModelBuild MB=new ModelBuild(NameAddressOnt1);
     	File ontologyFile = new File(NameAddressOnt1); 
     	if (NameAddressOnt1.length() <= 0){
   		  JOptionPane.showMessageDialog(null, "Please Select your Ontology File", "Error", JOptionPane.ERROR_MESSAGE);
   		  return;
   	    }else if (!ontologyFile.exists()) {
             JOptionPane.showMessageDialog(null,"The ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
             return;
     	}else
         if (NameAddressOnt1.length() > 0  ) {
         	LockedGUI();
             // Build the model
         	con=new Controller(NameAddressOnt1);
             if (Controller.CheckBuildModel == false) {
            	MB.build();
          		//BuildModel.BuildModelOnt(NameAddressOnt1);
          		Controller.CheckBuildModel =true;
          	}
             
             // Add this selected ontology to the next tab
             ontologyFileTextField.setText(NameAddressOnt1);
             Analysis AT= new Analysis (con.getOntModel(),con.MB.getOWLModel(),con.getRBGModel());
             AT.AnalyzingTestRun();
             ModelBuild.analysisTest = true;
             Un_LockedGUI();  
         }
     

     }
     
     private void CompareButtonButtonActionPerformed(ActionEvent evt) {
    		
//    		Cleaning_Allthings.clean(0);
    		NameAddressOnt1 = ont1stTextField.getText().trim();
    		NameAddressOnt2 = ont2ndTextField.getText().trim();
    		File ontologyFile1 = new File(NameAddressOnt1);
    		File ontologyFile2 = new File(NameAddressOnt2);
    		
    		if (ont1stTextField.getText().length() <= 0 ){
    			  JOptionPane.showMessageDialog(null, "Please Select your first Ontology", "Error", JOptionPane.ERROR_MESSAGE);
    			  return;
    		    } else if (ont2ndTextField.getText().length() <= 0){
    	  		  JOptionPane.showMessageDialog(null, "Please Select your second Ontology", "Error", JOptionPane.ERROR_MESSAGE);
    	  		  return;
    	  	    } else if (!ontologyFile1.exists() ) {
    	              JOptionPane.showMessageDialog(null,"The first ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
    	              return;
    	        } else if (!ontologyFile2.exists()) {
    	            JOptionPane.showMessageDialog(null,"The second ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
    	            return;
    	  	    }
    		            	               	
    	    if (NameAddressOnt1.length() > 0 && NameAddressOnt2.length() > 0) {
    	    	
    	    	// Add one of the two candidate ontologies to the next tab
    	        ontologyFileTextField.setText(NameAddressOnt1);
    	        Thread cmp = new Thread()
    	        {
    	            @Override
    	            public void run()
    	            {
//    	            	Cleaning_Allthings.clean(3); 
    	            	
    	                Configuration config = new Configuration();
    	                config.init();
    	                String fp1 = NameAddressOnt1, fp2 = NameAddressOnt2;
    	              /*  if (!fp1.startsWith("file:")) {
    	                    fp1 = "file:" + fp1;
    	                }
    	                if (!fp2.startsWith("file:")) {
    	                    fp2 = "file:" + fp2;
    	                }*/
    	                
    	                
    	                Compare.compareFunc(fp1 ,fp2);
//    	                outputPane.setEnabledAt(3, true); TO DO:samira
//    	                outputPane.setEnabledAt(4, true);
//    	                outputPane.setEnabledAt(5, true);
    	            }
    	        };
    	        cmp.start();
    	    }
    	}	
    	    
     private void PlusbuttonActionPerformed(){
      	 JFileChooser fileChooser = new JFileChooser(".");
      	 fileChooser.setMultiSelectionEnabled(true);
         fileChooser.setName("Open");
         fileChooser.setToolTipText("Please input a file");
         int rVal = fileChooser.showOpenDialog(null);
         File[] ontfiles = fileChooser.getSelectedFiles();
         if (ontfiles.length > 0){ //if ontfiles.length ==0, it means the user select "cancel" button.
        	 for(int i=0; i<ontfiles.length;i++){
        		 MergeModels.add(ontfiles[i].toString());
        		 ontologyFileTextFieldMerge.append(ontfiles[i].toString() + "\n");
        	 }
         }

    	   // if (MergeModels.size()>1) {
    		//  JOptionPane.showMessageDialog(null, "Currently, OAPT can only merge two models at a time.", "Too many models", JOptionPane.ERROR_MESSAGE);
    	  //}
       }
      //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       private void MinusbuttonActionPerformed (){
    	   String comm = "";
    	   if (MergeModels.size() < 1){
    		   JOptionPane.showMessageDialog(null, "You do not have any ontolgies to be delete", "Error", JOptionPane.ERROR_MESSAGE);
    	   }else{
    		   for (int i=0; i<MergeModels.size(); i++){
    			   comm = comm +  "\n Index = " + i +" :  " + MergeModels.get(i); 
    		   }
    		   String stridx= JOptionPane.showInputDialog("Please enter the index of your selected ontolgy \n " + comm);
    	       if (stridx != null){ //if the user select "cancel", the showInputDialog return null
    	    	   int idx = Integer.parseInt(stridx);
    	    	   //TO DO: it is better it implements by map
    		       ArrayList<String> tempMergeModels;
    		       tempMergeModels =new ArrayList<String>();
    		       ontologyFileTextFieldMerge.setText(null);
    		       for (int i=0; i<MergeModels.size(); i++){
    		    	   if (i != idx) {
    		    		   tempMergeModels.add(MergeModels.get(i));
    		    		   ontologyFileTextFieldMerge.append(MergeModels.get(i) + "\n");
    		    	   }
    		       }
    		       ontologyFileTextFieldMerge.repaint();
    		       MergeModels = tempMergeModels;
    	       }
    	   }
    	}
       
       private void MergeButtonActionPerformed(ActionEvent evt) {
       	
//       	Cleaning_Allthings.clean(1);
       	Coordinator.FinishPartitioning = false;
       	ViewGraphButton.setEnabled(false);
       	SaveButton.setEnabled(false);
       	
       	//check for the existence of ontologies
       	/*NameAddressOnt = ontologyFileTextField.getText().trim();
       	File ontologyFile = new File(NameAddressOnt);
       	if (NameAddressOnt.length() <= 0){
     		  JOptionPane.showMessageDialog(null, "Please Select your Ontology File", "Error", JOptionPane.ERROR_MESSAGE);
     		  return;
     	    } else if (!ontologyFile.exists()) {
               JOptionPane.showMessageDialog(null,"The ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
               return;
     	    }
     	     */
           LockedGUI();
           
           //if (NameAddressOnt.length() > 0 ) {
           	Thread mergeProcess = new Thread()
               {
                   @Override
                   public void run()
                   {
                   	
                   	this.setName("Merge Process");
                   	
               		String selectedType = (String) MergeTypeComboBox.getSelectedItem();
               		try {
   						numAlgorithm = selectedType;
               			MergeApp.mainA(selectedType);
   					} catch (Exception e) {
   						// TODO Auto-generated catch block
   						e.printStackTrace();
   					}
                   	
                   	Un_LockedGUI();
                       ViewGraphButton.setEnabled(true);
                       SaveButton.setEnabled(true);
                   }
               };
               mergeProcess.start(); 
               
           //}
       }
       private void includeDefinitionLinksCheckBoxStateChanged(ChangeEvent evt) {
           if (includeDefinitionLinksCheckBox.isSelected()) {
               strengthDefinitionLinksSpinner.setEnabled(true);
               includeDefinitionResourcesComboBox.setEnabled(true);
           } else {
               strengthDefinitionLinksSpinner.setEnabled(false);
               includeDefinitionResourcesComboBox.setEnabled(false);
           }
       }
       
       private void includePropertyLinksCheckBoxStateChanged(ChangeEvent evt) {
           if (includePropertyLinksCheckBox.isSelected()) {
               strengthPropertyLinksSpinner.setEnabled(true);
           } else {
               strengthPropertyLinksSpinner.setEnabled(false);
           }
       }
       
       private void includeSubclassLinksCheckBoxStateChanged(ChangeEvent evt) {
           if (includeSubclassLinksCheckBox.isSelected()) {
               strengthSubclassLinksSpinner.setEnabled(true);
           } else {
               strengthSubclassLinksSpinner.setEnabled(false);
           }
       } 
       private void includeSubstringLinksCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {
           if (includeSubstringLinksCheckBox.isSelected()) {
               strengthSubstringLinksSpinner.setEnabled(true);
           } else {
               strengthSubstringLinksSpinner.setEnabled(false);
           }
       }
       private void includeDistanceLinksCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {
           if (includeDistanceLinksCheckBox.isSelected()) {
               strengthDistanceLinksSpinner.setEnabled(true);
               maxDistanceSpinner.setEnabled(true);
           } else {
               strengthDistanceLinksSpinner.setEnabled(false);
               maxDistanceSpinner.setEnabled(false);
           }
       }
     /////////////////////////////////
    	 public static void LockedGUI(){
    	    	SuggestButton.setEnabled(false);
    	    	SaveButton.setEnabled(false);
    	    	ExecuteButton.setEnabled(false);
    	    	ViewGraphButton.setEnabled(false);
    	    	ViewHierarchyButton.setEnabled(false);
    	    	SaveButton.setEnabled(false);
    	    	ontologyFileChooserButton.setEnabled(false);
    	    	progressBar.setStringPainted(true);
    	        progressBar.setString("I am running. Please wait!");
    	        progressBar.setIndeterminate(true);
    	        
    	    }

    	    public static void Un_LockedGUI(){
    	    	SuggestButton.setEnabled(true);
    	    	SaveButton.setEnabled(true);
    	    	ExecuteButton.setEnabled(true);
    	    	ViewGraphButton.setEnabled(true);
    	    	ViewAllGraphButton.setEnabled(true);
    	    	ViewHierarchyButton.setEnabled(true);
    	    	ontologyFileChooserButton.setEnabled(true);
    	    	progressBar.setIndeterminate(false);
    	        progressBar.setString("Complete. Have fun!");
    	        
    	    }
//    }
}
