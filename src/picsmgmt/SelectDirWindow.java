package picsmgmt;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SelectDirWindow extends JFrame {
	
    /** to avoid warnings */
    private static final long serialVersionUID = 1L;

    private static SelectDirWindow instance = new SelectDirWindow();
    
    private Props props = Props.instance();
    
    private JTextField inputDirF = new JTextField(48);
    private JTextField outputDirF = new JTextField(48);
    private JLabel errorLabel = new JLabel();
    private JButton okButton = new JButton("OK");
    
    public static SelectDirWindow instance() {
    	return instance;
    }
    
    public String getInputDir() {
    	return inputDirF.getText();
    }
    
    public String getOutputDir() {
    	return outputDirF.getText();
    }
    
    private void build() {
        // Layout stuff
        setSize(800, 300);
        setTitle("Select Directories");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4,1));
        
        JPanel panel;
        JLabel label;
        JButton button;
        
        panel = new JPanel();
        label = new JLabel("Input Directory");
        button = new JButton("...");
        button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
		        JFileChooser chooser = new JFileChooser();
		        chooser.setSelectedFile(new File(inputDirF.getText()));
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int returnVal = chooser.showDialog(SelectDirWindow.this, "Select Directory");              

		        if(returnVal == JFileChooser.APPROVE_OPTION){
		            inputDirF.setText(chooser.getSelectedFile().getPath());
		        }

			}
        	
        });
	inputDirF.setText(props.getProperty("inputDir"));
	outputDirF.setText(props.getProperty("outputDir"));
       
        panel.add(label);
        panel.add(inputDirF);
        panel.add(button);
        add(panel, 0, 0);
        
        panel = new JPanel();
        label = new JLabel("Output Directory");
        button = new JButton("...");
        button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
		        JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        chooser.setSelectedFile(new File(outputDirF.getText()));
		        int returnVal = chooser.showDialog(SelectDirWindow.this, "Select Directory");              

		        if(returnVal == JFileChooser.APPROVE_OPTION){
		            outputDirF.setText(chooser.getSelectedFile().getPath());
		        }

			}
        	
        });
        panel.add(label);
        panel.add(outputDirF);
        panel.add(button);
        add(panel, 0, 1);
        
        add(errorLabel, 0, 2);
        
        panel = new JPanel();
        panel.add(okButton);
        add(panel, 0, 3);
        
        okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				errorLabel.setText("");
				errorLabel.setForeground(Color.RED);
				if (!new File(inputDirF.getText()).isDirectory()) {
					errorLabel.setText("Invalid input directory");
					return;
				}
				if (!new File(outputDirF.getText()).isDirectory()) {
					errorLabel.setText("Invalid output directory");
					return;
				}
				
				props.setProperty("inputDir", inputDirF.getText());
				props.setProperty("outputDir", outputDirF.getText());
				props.doSave();
				// inputDirF.setText(props.getProperty("inputDir"));
				// outputDirF.setText(props.getProperty("outputDir"));

				
				SelectDirWindow.instance().setVisible(false);
				PicsMgmtWindow.instance().setVisible(true);
			}
        	
        });
    
    	
    }
    
    public SelectDirWindow() {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			build();
    		}
    	});
    }

}
