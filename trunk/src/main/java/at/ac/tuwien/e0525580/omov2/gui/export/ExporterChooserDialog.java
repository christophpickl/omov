package at.ac.tuwien.e0525580.omov2.gui.export;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;

import at.ac.tuwien.e0525580.omov2.FatalException;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class ExporterChooserDialog extends JDialog {

    private static final long serialVersionUID = -683746227786551236L;
    
    private final ExporterChooserController controller = new ExporterChooserController(this);

    private final JRadioButton btnHtml = new JRadioButton("HTML");
    private final HtmlExportPanel panelHtml = new HtmlExportPanel();
    private final JRadioButton btnXml = new JRadioButton("XML");
    private final XmlExportPanel panelXml = new XmlExportPanel();

    private final JPanel contentPanel = new JPanel(new BorderLayout());
    
    
    public ExporterChooserDialog(JFrame owner) {
        super(owner);
        this.setTitle("Export");
        this.setModal(true);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });

        final ButtonGroup group = new ButtonGroup();
        group.add(this.btnHtml);
        group.add(this.btnXml);
        
        this.btnHtml.setSelected(true);
        this.doButtonSelected(this.btnHtml);
        
        this.btnHtml.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doButtonSelected(btnHtml);
        }});
        this.btnXml.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
           doButtonSelected(btnXml);
        }});
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.add(this.newNorthPanel(), BorderLayout.NORTH);
        panel.add(this.newContentPanel(), BorderLayout.CENTER);
        panel.add(this.newCommandPanel(), BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // top, left, bottom, right
        return panel;
    }
    
    private JPanel newNorthPanel() {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Choose desired format: "), c);
        c.gridx = 1;
        panel.add(this.btnHtml, c);
        c.gridx = 2;
        panel.add(this.btnXml, c);

        CompoundBorder border = new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)
        );
        panel.setBorder(border);
        
        return panel;
    }

    private JPanel newContentPanel() {
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // top, left, bottom, right
        
        return this.contentPanel;
    }
    
    private void doButtonSelected(JRadioButton button) {
        
        final JPanel panel;
        if(button == this.btnHtml) {
            panel = this.panelHtml;
        } else {
            panel = this.panelXml;
        }

        this.contentPanel.removeAll();
        this.contentPanel.add(panel, BorderLayout.CENTER);
        this.pack();
        this.contentPanel.revalidate();
    }
    
    
    private JPanel newCommandPanel() {
        final JButton btnConfirm = new JButton("Export");
        final JButton btnCancel = new JButton("Cancel");

        this.getRootPane().setDefaultButton(btnConfirm);

        btnConfirm.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent event) {
            doConfirm();
        }});
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent event) {
            doCancel();
        }});
        
        final JPanel panel = new JPanel();
        panel.add(btnCancel);
        panel.add(btnConfirm);
        return panel;
    }
    
    private void doConfirm() {
        if(this.btnHtml.isSelected()) {
            this.controller.doExportHtml(this.panelHtml.getHtmlColumns());
        } else if(this.btnXml.isSelected()) {
            this.controller.doExportXml();
        } else {
            throw new FatalException("Unkown export format selected.");
        }
        
        
        this.dispose();
    }
    
    private void doCancel() {
        this.dispose();
    }
    
    public static void main(String[] args) {
        new ExporterChooserDialog(null).setVisible(true);
    }
}
