
package fusion.oapt.general.gui;

import org.apache.jena.ontology.OntModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fusion.oapt.general.gui.EvalPanelPartitioning;
import fusion.oapt.general.gui.EvalPanelMerging;
import fusion.oapt.general.gui.PartitioningPanel;
import fusion.oapt.general.visualization.GraphFrame;

public class MainFrame
{
	
	public static  JFrame frame = null;
	public static OntModel model1 = null;
	protected GraphFrame graphFrame;
    
    public static final JTabbedPane tabbedPane = new JTabbedPane();

    public MainFrame()
    {

                
        File tempDir = new File("./temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        File tempResult = new File("./temp/tempResult.rdf");
        if (tempResult.exists()) {
            tempResult.delete();
        }
        try {
            tempResult.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    public void init()
    {
        MainFrame mainframe = null;
    	graphFrame = new GraphFrame(mainframe, null);

    	tabbedPane.setSize(new Dimension(800, 1000));

    	PartitioningPanel partitionPanel = new PartitioningPanel();
    	EvalPanelPartitioning evalPartitioningPanel = new EvalPanelPartitioning();
    	MatchingPanel matchPanel = new MatchingPanel();
    	EvalPanelMatching evalMatchPanel = new EvalPanelMatching();
    	MergingPanel mergePanel = new MergingPanel();
    	EvalPanelMerging evalMergingPanel = new EvalPanelMerging();
    	AnalysisPanel analyzPanel = new AnalysisPanel();
    	SemSimPanel SemSimPanel = new SemSimPanel();
//    	guiNew gn = new guiNew();
    	
//    	tabbedPane.addTab("new",  gn);
    	tabbedPane.addTab("Analysis", null, analyzPanel, null); 
        tabbedPane.addTab("Partitioning", null, partitionPanel, null);
        tabbedPane.addTab("Partitioning Evaluation", null, evalPartitioningPanel, null);
        tabbedPane.addTab("Matching", null, matchPanel, null);
        tabbedPane.addTab("Matching Evaluation", null, evalMatchPanel, null);
        tabbedPane.addTab("Merging", null, mergePanel, null);
        tabbedPane.addTab("Merging Evaluation", null, evalMergingPanel, null);
        tabbedPane.addTab("Semantic Similarity", null, SemSimPanel, null);
                
        //Disactive bellow tabs since we do not implement them in demo paper

        //tabbedPane.setEnabledAt(3, false);
       // tabbedPane.setEnabledAt(4, false);
       // tabbedPane.setEnabledAt(5, false);

        //tabbedPane.setEnabledAt(3, false);
        //tabbedPane.setEnabledAt(4, false);
        //tabbedPane.setEnabledAt(5, false);

        
        frame = new JFrame();
        frame.setSize(new Dimension(800, 800));
        frame.getContentPane().setBounds(0, 0, 800, 800);
        frame.setTitle("OAPT: Ontoloyg Analysis & Partitioning Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        final JMenu fileMenu = new JMenu();
        menuBar.add(fileMenu);
        fileMenu.setText("File");

        final JMenuItem exitMenu = new JMenuItem();
        exitMenu.setText("Exit");
        exitMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	System.exit(0);
            }
        });
        fileMenu.add(exitMenu);

        
        final JMenu helpMenu = new JMenu();
        menuBar.add(helpMenu);
        helpMenu.setText("Help");

        final JMenuItem aboutMenu = new JMenuItem();
        aboutMenu.setText("About");
        aboutMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	AboutDialog AboutDialogP = new AboutDialog(); 
            	AboutDialogP.setVisible(true);
            }
        });
        helpMenu.add(aboutMenu);

        
        
        final JMenu info = new JMenu();
        menuBar.add(info);
        info.setText("Information");

        final JMenuItem SeeCont_Clustering_Menu = new JMenuItem();
        SeeCont_Clustering_Menu.setText("SeeCont Clustering");
        SeeCont_Clustering_Menu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String s = "SeeCont Clustering \r\n";
                s += "Designed by Samira Babalou in 2015,\r\n";
                s += "This is seeding based clustering method";
                JOptionPane.showMessageDialog(
                		null, s, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        info.add(SeeCont_Clustering_Menu);
        
        final JMenuItem bottomUp_Clustering_Menu = new JMenuItem();
        bottomUp_Clustering_Menu.setText("bottomUp Clustering");
        bottomUp_Clustering_Menu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String s = "bottomUp Clustering \r\n";
                s += "Designed by Dr. Alsayed Algergawy,\r\n";
                s += "This is bottomUp clustering method";
                JOptionPane.showMessageDialog(
                		null, s, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        info.add(bottomUp_Clustering_Menu);
        
        
        final JPanel mainResult = new JPanel();
        mainResult.setLayout(new BorderLayout());
        frame.getContentPane().add(mainResult);
        mainResult.add(tabbedPane);
       
        
    }

    public static void main(String args[])
    {
        MainFrame window = new MainFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = window.frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        int height = (screenSize.height - frameSize.height) / 2;
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        int width = (screenSize.width - frameSize.width) / 2;
        window.frame.setLocation(width, height);
        window.frame.setVisible(true);
        
        PartitioningPanel.SaveButton.setEnabled(false);
     	PartitioningPanel.ViewGraphButton.setEnabled(false);
     	PartitioningPanel.ViewAllGraphButton.setEnabled(false);
        
    }
}
