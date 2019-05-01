package fusion.oapt.algorithm.partitioner.SeeCOnt.Findk;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.PartitioningPanel;
import fusion.oapt.algorithm.partitioner.SeeCOnt.CClustering;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import fusion.oapt.algorithm.partitioner.SeeCOnt.EvaluationSeeCOnt;
import fusion.oapt.general.visualization.Visualization;
import fusion.oapt.general.gui.MainFrame;

public class CHframe extends javax.swing.JFrame {
	public static String NameAddressOnt; 
	public static String[] CHComboBoxTitle = new String[] {};
	public static DefaultListModel<String> ListArrayCH= new DefaultListModel<>();
	public static List<String> selectedValuesList;
    private static final long serialVersionUID = 1L;
    private MainFrame mainframe;
    private Controller con;
    public CHframe(MainFrame mainframe, Controller con) {
        //super();
        setPreferredSize(new Dimension(800, 800));
        setLayout(new BorderLayout());
        initComponents();
        this.mainframe = mainframe;
        this.con=con;
    }

    private void initComponents() {
        
    	java.awt.GridBagConstraints gridBagConstraints;
        
    	
    	
    	setTitle("SelectCH");
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

    	
        //String[] CHComboBoxTitle = new String[] {"SeeCOnt", "AxCOnt", "PBM"};
        
        ApplyListButton = new javax.swing.JButton();
        ApplyTreeButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        GraphButton = new javax.swing.JButton();
        TreeButton = new javax.swing.JButton();
//        TreeTextArea= new javax.swing.JTextArea();
        TreeTextArea= new JTree();
        TreeScrollPane = new javax.swing.JScrollPane();
        
        PicturePanel = new javax.swing.JPanel();                  PicturePanel.setPreferredSize(new Dimension(800, 120)); PicturePanel.setMaximumSize(new Dimension(800, 120)); PicturePanel.setMinimumSize(new Dimension(800, 120));
        infoPanel = new javax.swing.JPanel();        infoPanel.setPreferredSize(new Dimension(800, 100)); infoPanel.setMaximumSize(new Dimension(800, 100)); infoPanel.setMinimumSize(new Dimension(800, 100));
        SelectPanel = new javax.swing.JPanel();       SelectPanel.setPreferredSize(new Dimension(800, 420)); SelectPanel.setMaximumSize(new Dimension(800, 420)); SelectPanel.setMinimumSize(new Dimension(800, 420));
        SelectPanel1 = new javax.swing.JPanel();       SelectPanel1.setPreferredSize(new Dimension(380, 420)); SelectPanel1.setMaximumSize(new Dimension(380, 420)); SelectPanel1.setMinimumSize(new Dimension(380, 420));
        SelectPanel2 = new javax.swing.JPanel();       SelectPanel2.setPreferredSize(new Dimension(380, 420)); SelectPanel2.setMaximumSize(new Dimension(380, 420)); SelectPanel2.setMinimumSize(new Dimension(380, 420));
        ActionPanel = new javax.swing.JPanel();       ActionPanel.setPreferredSize(new Dimension(800, 60)); ActionPanel.setMaximumSize(new Dimension(800, 60)); ActionPanel.setMinimumSize(new Dimension(800, 60));
        StatusPanel = new javax.swing.JPanel();       StatusPanel.setPreferredSize(new Dimension(800, 40)); StatusPanel.setMaximumSize(new Dimension(800, 40)); StatusPanel.setMinimumSize(new Dimension(800, 40));
        //OutputPanel = new javax.swing.JPanel();                   OutputPanel.setPreferredSize(new Dimension(800, 100));  OutputPanel.setMaximumSize(new Dimension(800, 100)); OutputPanel.setMinimumSize(new Dimension(800, 100));
                
        CurrentCHLabel = new javax.swing.JLabel();
        CurrentCHScrollPane = new javax.swing.JScrollPane();
        CurrentNumCHLabel = new javax.swing.JLabel();
        CHLabel = new javax.swing.JTextArea();
        NumCHLabel= new javax.swing.JTextArea();
        ListLabel = new javax.swing.JLabel();
        TreeLabel = new javax.swing.JLabel();
        StatusLabel= new javax.swing.JLabel();
        EmptyLabel = new javax.swing.JLabel();
        pic1 = new ImageIcon("fig/logo1.png");
        OutputScrollPane = new javax.swing.JScrollPane();
        OutputPartitioningTextArea = new javax.swing.JTextArea();   
        CHComboBox = new JComboBox<>(CHComboBoxTitle);
        
        //DefaultListModel<String> ListArrayCH = new DefaultListModel<>();
        ListScrollPane = new javax.swing.JScrollPane(ItemList);
        
        ItemList = new JList<>(ListArrayCH);
        ItemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //add(ItemList);
        
        setLayout(new java.awt.GridBagLayout());
        PicturePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        PicturePanel.add(new JLabel(pic1),gridBagConstraints);
        add(PicturePanel, gridBagConstraints);
        
        setLayout(new java.awt.GridBagLayout());

        infoPanel.setLayout(new java.awt.GridBagLayout());
        infoPanel.setBorder(new javax.swing.border.TitledBorder("Information"));
        
        CurrentNumCHLabel.setText("Current Num CH:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        infoPanel.add(CurrentNumCHLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        infoPanel.add(NumCHLabel, gridBagConstraints);

        
        CurrentCHLabel.setText("Current CH:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        infoPanel.add(CurrentCHLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        CHLabel.setEditable(false);
        CHLabel.setLineWrap(true);
        CHLabel.setCaretPosition(0);
        CHLabel.setWrapStyleWord(true);
        CurrentCHScrollPane.setViewportView(CHLabel);
        //infoPanel.add(CHLabel, gridBagConstraints);
        infoPanel.add(CurrentCHScrollPane, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(infoPanel, gridBagConstraints);
        
        
       
       
        /*
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        SelectPanel.add(TreeButton, gridBagConstraints);
               
        TreeButton.setText("View Ontology Tree");
        TreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	TreeButtonActionPerformed(evt); 
            }
        });
        */

        SelectPanel1.setLayout(new java.awt.GridBagLayout());  
        
        TreeLabel.setText("Hierarchy view:                               ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        SelectPanel1.add(TreeLabel, gridBagConstraints);

 
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        //to print in JtextArea the next two line should be work
       // PrintStream printStreamPartitioner = new PrintStream(new CustomOutputStreamm(PartitioningPanel.OutputPartitioningTextArea)); // It is for printing output in textbox, not in the console
       // System.setOut(printStreamPartitioner);
        SelectPanel1.add(TreeScrollPane, gridBagConstraints);
        
        
//        #
        SelectPanel2.setLayout(new java.awt.GridBagLayout());
        ListLabel.setText("Order of classes:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        SelectPanel2.add(ListLabel, gridBagConstraints);

        
//        TreeTextArea.setEditable(false);
//        TreeTextArea.setLineWrap(true);
//        TreeTextArea.setCaretPosition(0);
//        TreeTextArea.setWrapStyleWord(true);
//        TreeTextArea.setText(" "+'\n'+" ");
//        TreeScrollPane.setViewportView(TreeTextArea);

        
        
        /*
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        SelectPanel.add(CHComboBox, gridBagConstraints);

        CHComboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        */
        

        
       
        //CHframe.OutputPartitioningTextArea.setEditable(false);
        //CHframe.OutputPartitioningTextArea.setLineWrap(true);
        //CHframe.OutputPartitioningTextArea.setCaretPosition(0);
        //CHframe.OutputPartitioningTextArea.setWrapStyleWord(true);
        //CHframe.OutputPartitioningTextArea.setText(" "+'\n'+" ");
        ListScrollPane.setViewportView(ItemList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        //to print in JtextArea the next two line should be work
       // PrintStream printStreamPartitioner = new PrintStream(new CustomOutputStreamm(PartitioningPanel.OutputPartitioningTextArea)); // It is for printing output in textbox, not in the console
       // System.setOut(printStreamPartitioner);
        SelectPanel2.add(ListScrollPane, gridBagConstraints);
       
       
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        SelectPanel.add(SelectPanel1, gridBagConstraints);
        

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        SelectPanel.add(SelectPanel2, gridBagConstraints);
        
        SelectPanel.setLayout(new java.awt.GridBagLayout());
//        SelectPanel.setBorder(new javax.swing.border.TitledBorder("Select"));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(SelectPanel, gridBagConstraints);       
        
        /*
        OutputPanel.setLayout(new java.awt.GridBagLayout());

        OutputPanel.setBorder(new javax.swing.border.TitledBorder("Output"));
        
        CHframe.OutputPartitioningTextArea.setEditable(false);
        CHframe.OutputPartitioningTextArea.setLineWrap(true);
        CHframe.OutputPartitioningTextArea.setCaretPosition(0);
        CHframe.OutputPartitioningTextArea.setWrapStyleWord(true);
        CHframe.OutputPartitioningTextArea.setText(" "+'\n'+" ");
        OutputScrollPane.setViewportView(CHframe.OutputPartitioningTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        //to print in JtextArea the next two line should be work
       // PrintStream printStreamPartitioner = new PrintStream(new CustomOutputStreamm(PartitioningPanel.OutputPartitioningTextArea)); // It is for printing output in textbox, not in the console
       // System.setOut(printStreamPartitioner);
        OutputPanel.add(OutputScrollPane, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        add(OutputPanel, gridBagConstraints); 
       */
        
        ActionPanel.setLayout(new java.awt.GridBagLayout());
        ActionPanel.setBorder(new javax.swing.border.TitledBorder("Action"));

     

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ActionPanel.add(ApplyTreeButton, gridBagConstraints);
               
        ApplyTreeButton.setText("Apply Selected CH from Tree");
        ApplyTreeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyTreeButtonActionPerformed(evt); 
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ActionPanel.add(ApplyListButton, gridBagConstraints);
               
        ApplyListButton.setText("Apply Selected CH from List");
        ApplyListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyListButtonActionPerformed(evt); 
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ActionPanel.add(closeButton, gridBagConstraints);
               
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	closeButtonActionPerformed(evt); 
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ActionPanel.add(GraphButton, gridBagConstraints);
               
        GraphButton.setText("View Ontology Graph");
        GraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	GraphButtonActionPerformed(evt); 
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(ActionPanel, gridBagConstraints);
        
        
        StatusPanel.setLayout(new java.awt.GridBagLayout());
        StatusPanel.setBorder(new javax.swing.border.TitledBorder("Status"));
        
        StatusLabel.setText("Click on Apply button to partition the ontology by new selected CH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        StatusPanel.add(StatusLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(StatusPanel, gridBagConstraints);

        
        
        pack(); //with this line, the window will be created
     }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void ApplyListButtonActionPerformed(ActionEvent evt) {
    	selectedValuesList = ItemList.getSelectedValuesList();
    	if (selectedValuesList.size() > 0) {
	    	//Show info 
	    	String n = selectedValuesList.size() + " ";
	       	NumCHLabel.setText(n);
	       	CHLabel.setText(null);
	       	String allCH=null;
	       	for (int i=0; i<selectedValuesList.size();i++){
	       		if (allCH != null ) {
	       			allCH = allCH + " , " + selectedValuesList.get(i);
	       		} else{
	       			allCH = selectedValuesList.get(i);
	       		}
	       	}
	       	CHLabel.setText(allCH);
	       	
   
	       	//Run SeeCONT with these new CHs
	       	//CentralClustering SeeCOntObj= new CentralClustering ();
	       	//SeeCOntObj.SeeCOntAlogorithm(1); //call from Apply button of adding CH (as an interactive process)
	       	//LinkedHashMap<Integer, Cluster> Clusters;
	       	//Clusters = SeeCOntObj.SeeCOntAlogorithm(1); //call from Apply button of adding CH (as an interactive process)
	       	
	       	
	       	CClustering cc= new CClustering (new ModelBuild());
	       	cc.StepsCClustering(1); //call from Apply button of adding CH (as an interactive process)
	       	
	       	//write finish successfully
	       	StatusLabel.setText("Partitioning has been done by your "+ Coordinator.KNumCH + " cluster heads successfully.");
	       	
	       	//do evaluation for these new clustering
	       	EvaluationSeeCOnt ES=new EvaluationSeeCOnt();
	       	ES.Evaluation_SeeCont();
	       //	EvaluationSeeCOnt.Evaluation_SeeCont();
	       	
	       	PartitioningPanel.NumCHTextField.setText(n);
    	} else{
  		  JOptionPane.showMessageDialog(null, "Please Select your CH", "Error", JOptionPane.ERROR_MESSAGE);
    	}
   }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void ApplyTreeButtonActionPerformed(ActionEvent evt) {
    	selectedValuesList = new ArrayList<String>();
    	String n = null;
    	TreePath[] nodes = ShowTree.tree.getSelectionPaths ();    
	
    	if (nodes == null){
		    	  JOptionPane.showMessageDialog(null, "Please Select your CH", "Error", JOptionPane.ERROR_MESSAGE);
		      }else{	      
		      	n = nodes.length + " ";
		    	NumCHLabel.setText(n);
		    	CHLabel.setText(null);
		    	String allCH=null;
		    	
			      for (int i = 0; i < nodes.length; i ++)
			      {
			          TreePath treePath = nodes[i];
			          DefaultMutableTreeNode node =(DefaultMutableTreeNode)treePath.getLastPathComponent ();
			          String aw = node.getUserObject ().toString();
			          selectedValuesList.add(aw);
			          if (allCH != null ) {
			       			allCH = allCH + " , " + aw;
			       		} else{
			       			allCH = aw;
			       		}
			      }
			      CHLabel.setText(allCH);
		      

			      CClustering cc= new CClustering (new ModelBuild());
			      cc.StepsCClustering(1); //call from Apply button of adding CH (as an interactive process)
	       	
			      //write finish successfully
			      StatusLabel.setText("Partitioning has been done by your "+ Coordinator.KNumCH + " cluster heads successfully.");
	       	
			      //do evaluation for these new clustering
			  	 EvaluationSeeCOnt ES=new EvaluationSeeCOnt();
		       	 ES.Evaluation_SeeCont();
			  // EvaluationSeeCOnt.Evaluation_SeeCont();
	       	
			      PartitioningPanel.NumCHTextField.setText(n);
		      } 
   }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void closeButtonActionPerformed(ActionEvent evt) {
        this.setVisible(false);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void GraphButtonActionPerformed(ActionEvent evt) {
    	Coordinator.argVisulization = 2;
    	Visualization.Run(Coordinator.argVisulization,con); //2 means call from CH selection panel
	  }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void TreeButtonActionPerformed(ActionEvent evt) {
    	//https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
    	ModelBuild mb=null;
    	ShowTree.ShowTreeCalculating(mb);
	  }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static javax.swing.JButton ApplyListButton;
    public static javax.swing.JButton ApplyTreeButton;
    public static javax.swing.JButton closeButton;
    public static javax.swing.JButton GraphButton;
    public static javax.swing.JButton TreeButton;
    private javax.swing.JLabel CurrentCHLabel;
    private javax.swing.JScrollPane CurrentCHScrollPane;
    private javax.swing.JLabel CurrentNumCHLabel;
    public static javax.swing.JTextArea CHLabel;
    public static javax.swing.JTextArea NumCHLabel;
    private javax.swing.JLabel ListLabel;
    private javax.swing.JLabel TreeLabel;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel SelectPanel1;
    private javax.swing.JPanel SelectPanel2;
    private javax.swing.JPanel SelectPanel;
    private javax.swing.JPanel ActionPanel;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JLabel EmptyLabel;
    private javax.swing.JLabel EmptyLabel2;
    private javax.swing.ImageIcon pic1 ;
    private javax.swing.JPanel PicturePanel;
    //private javax.swing.JPanel OutputPanel;
    private javax.swing.JScrollPane OutputScrollPane;
    public static javax.swing.JScrollPane TreeScrollPane;//#
//    private javax.swing.JTextArea TreeTextArea;
    private JTree TreeTextArea;
    public static javax.swing.JTextArea OutputPartitioningTextArea;
    final static JProgressBar progressBar = new JProgressBar(1, 100);
    private javax.swing.JComboBox CHComboBox;
    
    private javax.swing.JScrollPane ListScrollPane;
    private JList<String> ItemList ;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //mainframe.graphMenuItem.doClick();
    	this.setVisible(true);
    }//GEN-LAST:event_formWindowClosing
        
}
