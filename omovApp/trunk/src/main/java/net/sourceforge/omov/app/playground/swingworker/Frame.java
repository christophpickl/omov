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

package net.sourceforge.omov.app.playground.swingworker;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.sourceforge.omov.guicore.GuiActionListener;

import org.jdesktop.swingworker.SwingWorker;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Frame extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new Frame().setVisible(true);
    }
    
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JButton btnStart = new JButton("start");
    private final JButton btnCancel = new JButton("cancel");
    public Frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.btnStart.addActionListener(new GuiActionListener() {
            @Override
			public void action(ActionEvent e) {
                doStart();
            }
        });
        this.btnCancel.setEnabled(false);
        this.btnCancel.addActionListener(new GuiActionListener() {
            @Override
			public void action(ActionEvent e) {
                doCancel();
            }
        });
        
        final JPanel panel = new JPanel();
        panel.add(this.btnStart);
        panel.add(this.progressBar);
        panel.add(this.btnCancel);
        
        this.getContentPane().add(panel);
        
        this.pack();
        

//        worker.addPropertyChangeListener(new PropertyChangeListener() {
//            public void propertyChange(PropertyChangeEvent evt) {
//                System.out.println("property changed: " + evt.getPropertyName() + "; old="+evt.getOldValue()+"; new="+evt.getNewValue());
//            }
//        });
    }
    
    private MyWorker worker;
    
    private void doStart() {
        System.out.println("doStart()");
        this.btnStart.setEnabled(false);
        this.btnCancel.setEnabled(true);

        
        
        worker = new MyWorker();
        System.out.println("worker.execute()");
        worker.execute();
    }

    private void doCancel() {
        System.out.println("doCancel()");
        worker.cancel(false);
    }
    
    private class MyWorker extends SwingWorker<String, Integer> {

        @Override
        protected String doInBackground() throws Exception {
            System.out.println("swing worker: doInBackground() start");
            
            for (int i = 1; i <= 100; i++) {
                if(this.isCancelled()) {
                    System.out.println("swing worker: isCancelled == true");
                    break;
                }
                Thread.sleep(50);
                if(i % 10 == 0) System.out.println("swing worker: exec " + i);
                progressBar.setValue(i);
            }
            
            System.out.println("swing worker: doInBackground() end");
            return "finished";
        }

        @Override
        protected void done() {
            System.out.println("swing worker: done(); was cancelled = " + worker.isCancelled());
            if(worker.isCancelled() == false) {
                progressBar.setValue(100);
            }
            
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
        }
    }
}
