
package fusion.oapt.general.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

//import fusion.oapt.algorithm.matcher.Matching;
import fusion.oapt.general.cc.Cleaning_Allthings;
import fusion.oapt.general.cc.Configuration;
import fusion.oapt.general.output.Alignment;
import fusion.oapt.general.output.AlignmentWriter2;



public class MatchingPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    public static JTextField textFieldOnto1 = null;
    public static JTextField textFieldOnto2 = null;
    private Alignment alignment = null;

    public MatchingPanel()
    {
        super();
        setPreferredSize(new Dimension(640, 480));
        setLayout(new BorderLayout());

        //final TablePanel alignTable = new TablePanel(alignment);
        final TablePanel alignTable = new TablePanel();
        alignTable.setPreferredSize(new Dimension(600, 200));

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        final JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        inputPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        final JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(300, 60));
        panel.setPreferredSize(new Dimension(300, 60));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(2);
        panel.setLayout(gridLayout);
        inputPanel.add(panel);

        final JPanel panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(0, 30));
        panel.add(panel_1);

        final JLabel label_1 = new JLabel();
        panel_1.add(label_1);
        label_1.setText("   1st Modual: ");

        textFieldOnto1 = new JTextField();
        textFieldOnto1.setPreferredSize(new Dimension(475, 22));
        panel_1.add(textFieldOnto1);

        final JButton browseButton1 = new JButton();
        browseButton1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setName("Open");
                fileChooser.setToolTipText("Please input an alignment file");
                int rVal = fileChooser.showOpenDialog(mainPanel);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    textFieldOnto1.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });
        browseButton1.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        panel_1.add(browseButton1);
        browseButton1.setText("   Browse   ");

        final JPanel panel_2 = new JPanel();
        panel.add(panel_2);

        final JLabel label_2 = new JLabel();
        panel_2.add(label_2);
        label_2.setText("  2nd Modual: ");

        textFieldOnto2 = new JTextField();
        textFieldOnto2.setPreferredSize(new Dimension(475, 22));
        panel_2.add(textFieldOnto2);

        final JButton browseButton2 = new JButton();
        browseButton2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setName("Open");
                fileChooser.setToolTipText("Please input an alignment file");
                int rVal = fileChooser.showOpenDialog(mainPanel);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    textFieldOnto2.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });
        browseButton2.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        panel_2.add(browseButton2);
        browseButton2.setText("   Browse   ");

        final JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        final JPanel detailPanel = new JPanel();
        detailPanel.setBorder(new LineBorder(new Color(96, 13, 48), 2, false));
        detailPanel.setLayout(new BorderLayout());
        resultPanel.add(detailPanel);

        final JPanel progressPanel = new JPanel();
        detailPanel.add(progressPanel, BorderLayout.NORTH);
        final JProgressBar progressBar = new JProgressBar(1, 100);
        progressBar.setPreferredSize(new Dimension(750, 20));
        progressPanel.add(progressBar);

        final JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        detailPanel.add(tablePanel);

        tablePanel.add(alignTable, BorderLayout.CENTER);

        final JPanel savePanel = new JPanel();
        JButton saveButton = new JButton();
        saveButton.setText("Save");
        savePanel.add(saveButton);
        detailPanel.add(savePanel, BorderLayout.SOUTH);
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setName("Save as the RDF/XML format");
                fileChooser.setToolTipText("Please input a file to save");
                int rVal = fileChooser.showSaveDialog(mainPanel);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fp = file.getAbsolutePath();
                    AlignmentWriter2 writer = new AlignmentWriter2(alignment, fp);
                    writer.write("onto1", "onto2", "onto1", "onto2");
                }
            }
        });

        final JPanel panel_3 = new JPanel();
        final GridLayout gridLayout_1 = new GridLayout(1, 1);
        panel_3.setLayout(gridLayout_1);
        panel_3.setPreferredSize(new Dimension(100, 0));
        inputPanel.add(panel_3, BorderLayout.EAST);

        final JButton matchButton = new JButton();
        matchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                final String s1 = textFieldOnto1.getText().trim();
                final String s2 = textFieldOnto2.getText().trim();
                if (s1.length() > 0 && s2.length() > 0) {
                    progressBar.setStringPainted(true);
                    progressBar.setString("I am running. Please wait!");
                    progressBar.setIndeterminate(true);
                    Thread match = new Thread()
                    {
                        @Override
                        public void run()
                        {
                        	//call cleaning
                        	Cleaning_Allthings.clean(3); // 3 is the index of the "Matching" tab
                        	alignTable.setAlignment(null); alignTable.repaintTable(); //for being empty the result
                        	
                            Configuration config = new Configuration();
                            config.init();
                            String fp1 = s1, fp2 = s2;
                            if (!fp1.startsWith("file:")) {
                                fp1 = "file:" + fp1;
                            }
                            if (!fp2.startsWith("file:")) {
                                fp2 = "file:" + fp2;
                            }
//                            Controller controller = new Controller(fp1, fp2);
                            /*Matching controller = new Matching();
                            alignment = controller.run();
                            String fp = "./temp/tempResult.rdf";
                            AlignmentWriter2 writer = new AlignmentWriter2(alignment, fp);
                            writer.write("onto1", "onto2", "onto1", "onto2");
                            alignTable.setAlignment(alignment);
                            alignTable.repaintTable();
                            progressBar.setIndeterminate(false);
                            progressBar.setString("Complete. Have fun!");
                             matchButton.setEnabled(true);
                            MainFrame.model1 = controller.getOntModel1();
                            MainFrame.model1 = controller.getOntModel2();
                            MainFrame.tabbedPane.setEnabledAt(1, true);*/
                        }
                    };
                    match.start();
                }
            }
        });
        panel_3.add(matchButton);
        matchButton.setText("Match!");
    }
}
