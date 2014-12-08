package picsmgmt;

import javax.swing.SwingUtilities;

public class Launcher {
	
    /** main method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SelectDirWindow.instance().setVisible(true);
            }
        });
    }

}
