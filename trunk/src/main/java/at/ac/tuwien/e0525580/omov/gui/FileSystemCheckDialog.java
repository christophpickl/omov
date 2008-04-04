package at.ac.tuwien.e0525580.omov.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.tools.FileSystemChecker.FileSystemCheckResult;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class FileSystemCheckDialog extends JDialog {

    private static final long serialVersionUID = -2421626719137999448L;

    private final FileSystemCheckResult result;

    
    public FileSystemCheckDialog(FileSystemCheckResult result, JDialog owner) {
        super(owner);
        this.result = result;
        this.pseudoConstructor();
    }
    
    public FileSystemCheckDialog(FileSystemCheckResult result) {
        this.result = result;
        this.pseudoConstructor();
    }
    
    private void pseudoConstructor() {
        this.setTitle("File System Check");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Constants.COLOR_WINDOW_BACKGROUND);

        panel.add(this.northPanel(), BorderLayout.NORTH);
        panel.add(this.centerContent(), BorderLayout.CENTER);
        panel.add(this.southPanel(), BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel northPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        panel.setOpaque(false);

        panel.add(new JLabel("<html>It seems as the data stored in OurMovies is out of sync with your filesystem.<br>" +
                             "<font size=3>(You can disable this feature by unchecking the button in the preferences dialog.)</font></html>"));

        return panel;
    }
    
    private Component centerContent() {
        final StringBuilder sb = new StringBuilder();
        
        buildText(sb, this.result.getMissingFolders(), "Folder", "Folders");
        if(this.result.getMissingFolders().size() > 0) {
            sb.append(Constants.LINE_SEPARATOR);
        }
        buildText(sb, this.result.getMissingFiles(), "File", "Files");
        

        final JTextArea text = new JTextArea(sb.toString(), 8, 60);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        
        
        return new JScrollPane(text);
    }
    
    private static void buildText(StringBuilder sb, Map<Movie, List<String>> map, String labelSingular, String labelPlural) {

        if(map.size() > 0) {
            sb.append("Missing Movie "+labelPlural+" ("+map.size()+"):").append(Constants.LINE_SEPARATOR);
            sb.append("--------------------------").append(Constants.LINE_SEPARATOR);
        }
        for (final Movie movie : map.keySet()) {
            final List<String> missing = map.get(movie);
            for (String fileOrFolder: missing) {
                sb.append("[").append(fileOrFolder).append("] ... "); 
                sb.append(labelSingular).append(" missing for Movie '").append(movie.getTitle()).append("'");
                sb.append(Constants.LINE_SEPARATOR);
            }
        }
    }
    
    private JPanel southPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        final JButton btnClose = new JButton("Close");
        this.getRootPane().setDefaultButton(btnClose);
        btnClose.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doClose();
        }});
        btnClose.setOpaque(false);
        
        panel.add(btnClose, BorderLayout.EAST);

        return panel;
    }
    
    
    private void doClose() {
        this.dispose();
    }
    
    public static void main(String[] args) {
        new FileSystemCheckDialog(null).setVisible(true);
    }
}
