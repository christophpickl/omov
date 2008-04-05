package at.ac.tuwien.e0525580.omov.gui.export;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov.bo.Movie;

public class BackupExportPanel extends JPanel {

    private static final long serialVersionUID = -8464758891434308258L;

    public BackupExportPanel() {

        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        
        final String infoString = "<html>" +
            "This export format will let you create a <b>backup</b><br>" +
            "of your movies, so they can be imported again,<br>" +
            "either in your or in a friend's OurMovies application.<br>" +
            "(<i>The exported movie data version is <b>v"+Movie.DATA_VERSION+"</b></i>)" +
            "</html>";
        
        final JLabel infoText = new JLabel(infoString);
        infoText.setFont(new Font("default", Font.PLAIN, 12));
        
        this.add(infoText);
    }
}
