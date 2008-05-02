package net.sourceforge.omov.app.spielwiese.swingworker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jdesktop.swingworker.SwingWorker;

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
        this.btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doStart();
            }
        });
        this.btnCancel.setEnabled(false);
        this.btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
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
