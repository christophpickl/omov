/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.doubletten;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.tools.doubletten.DuplicatesFinder;
import net.sourceforge.omov.core.tools.doubletten.DuplicatesFinder.IDuplicatesFinderListener;
import net.sourceforge.omov.gui.GuiActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DuplicatesFinderProgressDialog extends JDialog implements IDuplicatesFinderListener {

    private static final Log LOG = LogFactory.getLog(DuplicatesFinderProgressDialog.class);
    private static final long serialVersionUID = -1210382497017009556L;

    private final DuplicatesFinder finder;

    private final JProgressBar progressBar = new JProgressBar();
    private final JButton btnCancel = new JButton("Cancel");


    public DuplicatesFinderProgressDialog(JFrame owner) {
        super(owner, "Finding duplicates ...", true);
        this.finder = new DuplicatesFinder();

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);

        LOG.debug("Starting new thread to find duplicates...");

        new Thread(new Runnable() {
            public void run() {
                finder.findDuplicates(DuplicatesFinderProgressDialog.this);
            }
        }).start();


        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this);
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel();

        this.progressBar.setIndeterminate(true);
        this.btnCancel.addActionListener(new GuiActionListener() {
            public void action(ActionEvent e) {
                doCancel();
            }
        });

        panel.add(this.progressBar);
        panel.add(this.btnCancel);

        return panel;
    }

    private void doCancel() {
        LOG.debug("User clicked cancel button.");

        this.btnCancel.setEnabled(false);
        this.progressBar.setValue(0);
        this.finder.doAbort();
    }


    public void doProgressFinished(boolean successfullyFinished) {
        LOG.debug("doProgressFinished(successfullyFinished="+successfullyFinished+") invoked");
        if(successfullyFinished == false) {
            final Exception ex = this.finder.getThrownException();
            if(ex != null) {
                GuiUtil.error("Progress aborted", "Searching for duplicates was aborted\ndue to an internal error!");
                LOG.error("Searching for duplicates failed", ex);
            }
            this.dispose();
            return;
        }
        if(this.finder.getFoundDuplicates().size() == 0) {
            GuiUtil.info("No Duplicate Movies", "There could be not any duplicate movie found.");
            this.dispose();
        } else {
            final DuplicatesFinderDialog dialog = new DuplicatesFinderDialog((JFrame) this.getOwner(), this.finder);
            this.dispose();
            dialog.setVisible(true);
        }
    }
}
