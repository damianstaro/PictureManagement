package picsmgmt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Picture selection application
 */
public class PicsMgmtWindow extends JFrame {

    private static final String SEP = System.getProperty("file.separator");

    /** to avoid warnings */
    private static final long serialVersionUID = 1L;

	private static PicsMgmtWindow instance = new PicsMgmtWindow();

	private HashMap<String, PicTopic> topics = new  HashMap<String, PicTopic>();
	
    /** current picture index */
    private int picIndex = 0;
    
    /** all picture files found in the dir */
    private List<PicFile> picFiles;
    
    /** main panel for picture display */
    private AutoResize mainPanel = new AutoResize();

    private PicTopic lastTopic = PicTopic.MISC;
    
    /** handles key events and translates into appropriate actions */
    private KeyListener keyListener = new KeyListener() {

        /** Handles the arrows and enter */
        public void keyPressed(KeyEvent e) {
            //System.out.println(e);
            switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                prevB.doClick();
                break;
            case KeyEvent.VK_RIGHT:
                nextB.doClick();
                break;
            }            
        }

        /** does nothing */
        public void keyReleased(KeyEvent e) { }

        /** handles the button mnemonics */
        public void keyTyped(KeyEvent e) {
            System.out.println(e);
            switch (e.getKeyChar()) {
            case 'r':
            case 'R':
            	rotateB.doClick();
                break;
            case 'u':
            case 'U':
            	unrotateB.doClick();
                break;
            case 'p':
            case 'P':
                prevB.doClick();
                break;
            case 'n':
            case 'N':
                nextB.doClick();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            	rate(e.getKeyChar());
            	break;
            case 'd':
            case 'D':
                doneB.doClick();
                break;
            case 'A':
            case 'a':
            case '+':
                	addTopicB.doClick();
            	break;
            case 'S':
            case 's':
            	topicCB.requestFocus();
            	topicCB.showPopup();
            	break;
            case 'T':
            case 't':
            	incTopic();
            	break;
            }
            
        }
        
    };


    private JButton rotateB = newJButton("Rotate");
    private JButton unrotateB = newJButton("Unrotate");

    /** previous button */
    private JButton prevB = newJButton("Prev");

    /** next button button */
    private JButton nextB = newJButton("Next");

    /** done button */
    private JButton doneB = newJButton("Done");

    private JComboBox topicCB = new JComboBox();
    
    private JButton addTopicB = newJButton("+ Add");
    
    private JLabel ratingLabel = new JLabel("");
    
    /** displays current picture file name */
    private JLabel currPicLabel = newJLabel();
    private JLabel picNumberLabel = newJLabel();

	private SortedComboBoxModel topicCBModel = new SortedComboBoxModel();;

    
    /** factory method for a button */
    private JButton newJButton(String name) {
        JButton b = new JButton(name);
        setFont(b);
        b.addKeyListener(keyListener);
        return b;
    }

	/** factory method for a label */
    private JLabel newJLabel() {
        return newJLabel(null);
    }
    
    /** factory method for a label */
    private JLabel newJLabel(String s) {
        JLabel l = new JLabel(s);
        setFont(l);
        return l;
    }
    
    private void setFont(JComponent c) {
    	setFont(c, 20);
    }
    /** increases the font of a component */
    private void setFont(JComponent c, int s) {
        c.setFont(new Font("Dialog", Font.BOLD, s));
    }

    private void addTopic(PicTopic topic) {
    	if (topics.get(topic.getName()) != null)
    		return;
        topics.put(topic.getName(), topic);
        topicCBModel.addElement(topic.getName());
    }

    private void incTopic() {
    	Object o = topicCBModel.getSelectedItem();
    	int i = topicCBModel.getIndexOf(o);
    	i = (i + 1) % topicCBModel.getSize();
    	topicCBModel.setSelectedItem(topicCBModel.getElementAt(i));
    	
    }
    
    private void build() {
        File picsDir = new File(SelectDirWindow.instance().getInputDir());
        
        addTopic(PicTopic.MISC);
        
        // Populates all files found in the directory and any subdirectories
        picFiles = new ArrayList<PicFile>();
        Utils.getFiles(picsDir, picFiles);
        // Sort files by path name
        Collections.sort(picFiles);
        
        // Layout stuff
        setSize(500, 400);
        setTitle("Picture Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        // Set button mnemonics
        rotateB.setMnemonic(KeyEvent.VK_R);
        unrotateB.setMnemonic(KeyEvent.VK_U);
        prevB.setMnemonic(KeyEvent.VK_P);
        nextB.setMnemonic(KeyEvent.VK_N);
        doneB.setMnemonic(KeyEvent.VK_D);

        setFont(ratingLabel, 40);
        ratingLabel.setOpaque(true);
        ratingLabel.setForeground(Color.RED);
        ratingLabel.setBackground(Color.BLACK);
        
        topicCB.setModel(topicCBModel);
        topicCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setTopic(topics.get((String) topicCB.getSelectedItem()));
			}

        	
        });
        topicCB.addKeyListener(keyListener);
        setFont(topicCB);
        
        // Build top panel
        topPanel.add(rotateB);
        topPanel.add(unrotateB);
        topPanel.add(prevB);
        topPanel.add(nextB);
        topPanel.add(doneB);
        topPanel.add(ratingLabel);
        topPanel.add(topicCB);
        topPanel.add(addTopicB);
        topPanel.add(picNumberLabel);
        setFont(currPicLabel, 15);
        bottomPanel.add(currPicLabel);
        
        
        // Add actions to the buttons
        
		addTopicB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(PicsMgmtWindow.this,
						"Enter topic:\n" + "\"Topic:\"", "Topic",
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				if ((s != null) && (s.length() > 0)) {
					PicTopic t = new PicTopic(s);
					addTopic(t);
					setTopic(t);
				}
			}
		});
		rotateB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.rotate(picFiles.get(picIndex).getPath(), 90);
				refresh(true);
			}
		});
		unrotateB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.rotate(picFiles.get(picIndex).getPath(), 270);
				refresh(true);
			}
		});
            
        nextB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inc();
                
            }
        });
        prevB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dec();
               
            }
        });
        doneB.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
            	for (PicFile f : picFiles) {
            		if (f.getTopic() != null)
            			f.getTopic().process(f);
            	}
            	for (PicFile f : picFiles) {
            		if ((f.getTopic() == null) || (f.getRating() == '?'))
            			continue;
            		Calendar c1 = new GregorianCalendar();
            		c1.setTime(f.getTopic().getMinDate());
            		Calendar c2 = new GregorianCalendar();
            		c2.setTime(f.getTopic().getMaxDate());
            		String destinationPath = SelectDirWindow.instance().getOutputDir() + SEP +
            				c1.get(Calendar.YEAR) + SEP + 
            				f.getTopic().getName() + "_" + 
            				c1.get(Calendar.YEAR) + "-" + c1.get(Calendar.MONTH) + "-" + c1.get(Calendar.DAY_OF_MONTH) + "_to_" +
            				c2.get(Calendar.YEAR) + "-" + c2.get(Calendar.MONTH) + "-" + c2.get(Calendar.DAY_OF_MONTH) + SEP + "rated-" +
            				f.getRating();
            		System.out.println("Move " + f.getPath() + " to " + destinationPath);
            		File destinationDir = new File(destinationPath);
            		if (!destinationDir.exists())
            			destinationDir.mkdirs();
            		File source = new File(f.getPath());
            		
            		File destination = new File(destinationDir + SEP + new File(source.getName()));
            		while (destination.exists()) {
            			destination = new File(destination.getPath() + "_");
            		}
            		source.renameTo(destination);

            	}
        		JOptionPane.showMessageDialog(PicsMgmtWindow.this, "Completed");
        		System.exit(0);
            	
            }
        });
        
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        
        refresh(true);
        
    	
    }
    
    /** builds the application main window */
    public PicsMgmtWindow() 
    {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			build();
    		}
    	});
    }
    
    protected void rate(char keyChar) {
    	picFiles.get(picIndex).setRating(keyChar);
    	refresh(false);
		
	}
	private void setTopic(PicTopic picTopic) {
		System.out.println("Setting topic " + picTopic + " for picture index " + picIndex);
		System.out.println("Last topic is " + picTopic);
		lastTopic = picTopic;
		picFiles.get(picIndex).setTopic(picTopic);
    	refresh(false);
		
	}

    /** goes forward or backward in the picture list */
    private boolean add(int n) {
        int newIndex = picIndex + n;
        if ((0 <= newIndex) && (newIndex < picFiles.size())) {
            picIndex = newIndex;
            refresh(true);
            return true;
        }
        return false;
    }

    /** goes forward */
    private boolean inc() {
        return add(1);
    }

    /** goes backward */
    private boolean dec() {
        return add(-1);
    }
    
    
    /** refreshes the screen, reflecting selection state of the current picture */
    private void refresh(boolean readFromFile) {
        if (picFiles != null) {
            if ((picFiles.size() > picIndex) && (picIndex >=0)) {
                try {
                	
                	if (picFiles.get(picIndex).getTopic() == null) {
                		System.out.println("Setting last topic " + lastTopic + " for picture " + picIndex);
                		picFiles.get(picIndex).setTopic(lastTopic);
                	}
                	topicCB.setSelectedItem(picFiles.get(picIndex).getTopic().getName());
                	
                    ratingLabel.setText("    " + picFiles.get(picIndex).getRating() + "    ");
                    ratingLabel.setForeground(Color.RED);
                    if (picFiles.get(picIndex).getRating() >= '2')
                    	ratingLabel.setForeground(Color.YELLOW);
                    if (picFiles.get(picIndex).getRating() >= '4')
                    	ratingLabel.setForeground(Color.GREEN);
                    if (picFiles.get(picIndex).getRating() == '?')
                    	ratingLabel.setForeground(Color.BLUE);
                    
                    picNumberLabel.setText("   Number: " + (picIndex + 1) + "   Total: " + picFiles.size());
                    currPicLabel.setText("File Name: " + picFiles.get(picIndex).getPath());
                    if (readFromFile)
                    	mainPanel.setImage(ImageIO.read(new File(picFiles.get(picIndex).getPath())));
                    mainPanel.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /** Auto-resizing panel for picture display */
    public class AutoResize extends JPanel {
        
        /** avoids warning */
        private static final long serialVersionUID = 1L;
        
        /** the image */
        private BufferedImage image = null;
    
        /** constructor */
        public AutoResize() {
            
        }
    
        /** scales the image according to component height */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null)
                return;
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            int w = getWidth();
            int h = getHeight();
            int iw = image.getWidth();
            int ih = image.getHeight();
            double xScale = (double)w/iw;
            double yScale = (double)h/ih;
            double scale = Math.min(xScale, yScale);    // scale to fit
                           //Math.max(xScale, yScale);  // scale to fill
            int width = (int)(scale*iw);
            int height = (int)(scale*ih);
            int x = (w - width)/2;
            int y = (h - height)/2;
            g2.drawImage(image, x, y, width, height, this);
        }

        /** @return the image */
        public BufferedImage getImage() {
            return image;
        }

        /** sets the image */
        public void setImage(BufferedImage image) {
            this.image = image;
        }
    }

	public static PicsMgmtWindow instance() {
		return instance ;
	}
    
}
