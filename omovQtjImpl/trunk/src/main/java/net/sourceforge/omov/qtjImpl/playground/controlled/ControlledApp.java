package net.sourceforge.omov.qtjImpl.playground.controlled;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import net.sourceforge.omov.qtjImpl.QtjUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.app.view.MoviePlayer;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTJComponent;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import at.ac.tuwien.e0525580.jlib.util.GuiUtil;
import at.ac.tuwien.e0525580.jlib.util.TimeUtil;

// FIXME see net.sourceforge.atunes.gui.views.panels.PlayerControlsPanel

public class ControlledApp extends JFrame {

    private static final Log LOG = LogFactory.getLog(ControlledApp.class);
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) throws Exception {
		new ControlledApp().setVisible(true);
	}
	// TODO use TimeUtil.milliseconds2String(ms) -> "13:06"
	
	private final Movie movie;
	private final MoviePlayer player;
	private final JPanel panelSouth = new JPanel();
	private boolean playing = false;
	
//	private final JProgressBar progressBar = new JProgressBar(0, 100);
	private final MySlider timeSlider = new MySlider();
	private static class MySlider extends JSlider {
		private static final long serialVersionUID = 1L;
		private boolean pressed;
		public void setPressed(boolean pressed) {
			this.pressed = pressed;
		}
		public boolean isPressed() {
			return this.pressed;
		}
	}
	
	private final JLabel lblTime = new JLabel();
	
	private final int maxMicros;
	private final String maxTimeString;
	private void updateTimeUi() {
		try {
			if(player.getTimeBase().getTime() == maxMicros && playing) {
				System.out.println("EndOfVideo -> stop()");
				movie.stop();
				playing = false;
			}
		} catch (QTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(timeSlider.isPressed()) {
			System.out.println("updateTimeUi aborting; timeSlider.isPressed == true");
			
			final int draggedMicros = timeSlider.getValue();
			final String draggedTime = TimeUtil.microSecondsToString(draggedMicros);
			this.lblTime.setText(draggedTime+"/"+maxTimeString);
			return;
		}
		try {
			final int curMicros = player.getTimeBase().getTime();
			
			final String curTime = TimeUtil.microSecondsToString(curMicros);
			this.lblTime.setText(curTime+"/"+maxTimeString);
//			final int percent = (int) (curMs / ((double)maxMs) * 100);
//			System.out.println("percent: " + percent + "(curMs:"+curMs+"; maxMs:"+maxMs+")");
			
			
//			timeSlider.setValue(curMs);
			
		} catch (QTException e) {
			throw new RuntimeException(e);
		}		
	}
	// FIXME if is playing, then bwdx3; movie plays to early start but playing:boolean is still true!
	
	public ControlledApp() throws Exception{
		this.movie = QtjUtil.openQtMovie(new File("/movie.mov"));
		this.movie.setTimeScale(1000000); // in micro seconds
		this.player = new MoviePlayer(movie);
		final QTJComponent qtPlayercomponent = QTFactory.makeQTJComponent(player);
		final JComponent playerComponent = qtPlayercomponent.asJComponent();

		this.maxMicros = player.getTimeBase().getStopTime();
		final int maxTimeInSeconds = maxMicros / 1000000;
		System.out.println("movie length in seconds: "+maxTimeInSeconds);
		this.maxTimeString = TimeUtil.microSecondsToString(player.getTimeBase().getStopTime());
		

		addBtn("Start", new A() { public void a() throws Exception {
			System.out.println("start");
			playing = true;
			movie.start();
		}});
		
		addBtn("Stop", new A() { public void a() throws Exception {
			System.out.println("stop");
			playing = false;
			movie.stop();
		}});
		
		addBtn("Full Stop", new A() { public void a() throws Exception {
			System.out.println("full stop");
			if(playing) {
				movie.stop(); // actualy invoked doPause
				playing = false; // same here
			}
			player.setTime(0);
		}});
		
		addBtn("Prev", new A() { public void a() throws Exception {
			final float oldRate= movie.getRate();
			final float newRate = oldRate - 0.25f;
			LOG.debug("Prev ... old rate: "+oldRate+" -> new rate: "+newRate);
			movie.setRate(newRate);
		}});
		
		addBtn("Fwd", new A() { public void a() throws Exception {
			final float oldRate= movie.getRate();
			final float newRate = oldRate + 0.25f;
			LOG.debug("Fwd ... old rate: "+oldRate+" -> new rate: "+newRate);
			movie.setRate(newRate);
		}});
		
		addBtn("Vol+", new A() { public void a() throws Exception {
			final float oldVolume = movie.getVolume();
			final float newVolume = oldVolume + 0.1f;
			LOG.debug("Vol+ ... old vol: "+oldVolume+" -> new vol: "+newVolume);
			movie.setVolume(newVolume);
		}});
		
		addBtn("Vol-", new A() { public void a() throws Exception {
			final float oldVolume = movie.getVolume();
			final float newVolume = oldVolume - 0.1f;
			LOG.debug("Vol- ... old vol: "+oldVolume+" -> new vol: "+newVolume);
			movie.setVolume(newVolume);
		}});
		
		addBtn("Sysout Time", new A() { public void a() throws Exception {
//			final int time = player.getTime();
//			final int startPTime = player.getTimeBase().getStartTime();
//			final int stopPTime = player.getTimeBase().getStopTime();
//			final int pTime = player.getTimeBase().getTime();
//			System.out.println("time: " + time + "; startPTime: " + startPTime + "; stopPTime: " + stopPTime + "; pTime: " + pTime + "; ");
			
			final int curTime = player.getTimeBase().getTime();
			final int percPlayed = (int) (curTime / ((double)maxMicros) * 100);
			System.out.println(curTime+"/"+maxMicros+" ("+percPlayed+"%) -- timescale: " + movie.getTimeScale());
		}});

		// always true: movie.getActive()
		// something?: movie.getUserData()
		// always null: movie.getStatus()
		
		addBtn("un/mute", new A() { public void a() throws Exception {
			System.out.println("un/mute");
			player.setMuted(player.isMuted());
		}});

		
		addBtn("timevalue=5s", new A() { public void a() throws Exception {
			player.setTime(5000000);
			/*
			 * 5000 setTime --> results in curMs: 8333334
			 */
			
//			movie.setTime(new TimeRecord(movie.getTimeScale(), 5000));
//			movie.start();
			
			
//			movie.stop();
//			movie.setTimeValue(5);
//			movie.start();
			
		}});

		
		this.timeSlider.setValue(0);
		this.timeSlider.setMaximum(maxMicros);
		// this.timeSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE); // does not work on OSX :(
		this.timeSlider.setFocusable(false);
		this.timeSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(final MouseEvent e) {
				System.out.println("timeSlider pressed");
				timeSlider.setPressed(true);
			}
			public void mouseReleased(final MouseEvent e) {
				System.out.println("timeSlider released");
				timeSlider.setPressed(false);
				if (e.getSource().equals(timeSlider) && timeSlider.isEnabled()) {
//					final int value = timeSlider.getValue();
//					System.out.println("mouse released; timeSlider.getValue() = " + value);
//					seekToMs(value);
					
//					int value = ((JSlider)e.getSource()).getValue();
//					double perCent = (double) value / ((JSlider)e.getSource()).getMaximum();
//					double perCentOfSong = value * 1000000 / duration;
					
					int widthClicked = e.getPoint().x;
					int widthOfProgressBar = timeSlider.getSize().width;
					double perCent = (double) widthClicked / (double) widthOfProgressBar;
//					System.out.println("widthClicked="+widthClicked+"; widthOfProgressBar="+widthOfProgressBar);

					if(perCent > 1.0) perCent = 1.0;
					if(perCent < 0.0) perCent = 0.0;
					
					final int newTimeMicros = (int) (maxMicros * perCent);
					System.out.println("perCent="+perCent+"% -> seeking to " + (newTimeMicros/1000)+"ms ("+TimeUtil.microSecondsToString(newTimeMicros)+")");
					seekToMicros(newTimeMicros);
				}
			}
		});
		
		final JPanel northPanel = new JPanel();
		northPanel.add(this.lblTime);
		northPanel.add(this.timeSlider);
		
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(playerComponent, BorderLayout.CENTER);
		panel.add(panelSouth, BorderLayout.SOUTH);
		
		this.getContentPane().add(panel);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("QTJ Controller");
		this.pack();
		GuiUtil.setCenterLocation(this);
		

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
//				System.out.println("Running update UI task.");
				updateTimeUi();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, 200);
	}

	private void seekToMicros(int microsecs) {
		System.out.println("seeking to microsecs "+microsecs);
		try {
			player.setTime(microsecs); // timevalue == 1000000 (microseconds)
		} catch (StdQTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void seek(double percent) {
		final int newTime = (int) (maxMicros * percent);
		System.out.println("seeking to " +percent+"% -> " + newTime + " of " + maxMicros);
		try {
//			movie.setTime(newTime);
			player.setTime((int) Math.round(percent * 100)); // FIXME which value?
			
		} catch (StdQTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static interface A { void a() throws Exception; }
	private void addBtn(String text, final A a) {
		JButton btn = new JButton(text);
		btn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			try {
				updateTimeUi(); // in any case (any btn pressed) update time ui
				a.a();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}});
		panelSouth.add(btn);
		
	}
	

}
/*
TIME LOG

INFO  2008-06-03 18:58:58,173 [main] net.sourceforge.omov.qtjImpl.QtjSessionInitializer --- Opening quicktime session...
INFO  2008-06-03 18:58:58,202 [main] net.sourceforge.omov.qtjImpl.QtjSessionInitializer --- QT version: 7.4 and Java version: 65541
time: 0; startPTime: 0; stopPTime: 24416667; pTime: 0; 
start
time: 804; startPTime: 0; stopPTime: 24416667; pTime: 1340640; 
time: 1375; startPTime: 0; stopPTime: 24416667; pTime: 2292318; 
time: 1855; startPTime: 0; stopPTime: 24416667; pTime: 3092671; 
time: 2467; startPTime: 0; stopPTime: 24416667; pTime: 4111964; 
time: 3271; startPTime: 0; stopPTime: 24416667; pTime: 5452732; 
time: 3929; startPTime: 0; stopPTime: 24416667; pTime: 6548618; 
time: 4507; startPTime: 0; stopPTime: 24416667; pTime: 7511877; 
time: 5023; startPTime: 0; stopPTime: 24416667; pTime: 8372989; 
time: 5503; startPTime: 0; stopPTime: 24416667; pTime: 9172722; 
time: 6022; startPTime: 0; stopPTime: 24416667; pTime: 10036818; 
time: 6429; startPTime: 0; stopPTime: 24416667; pTime: 10716667; 
time: 6770; startPTime: 0; stopPTime: 24416667; pTime: 11284630; 
stop
time: 7351; startPTime: 0; stopPTime: 24416667; pTime: 12252740; 
time: 7351; startPTime: 0; stopPTime: 24416667; pTime: 12252740; 
time: 7351; startPTime: 0; stopPTime: 24416667; pTime: 12252740; 
time: 7351; startPTime: 0; stopPTime: 24416667; pTime: 12252740; 
time: 7351; startPTime: 0; stopPTime: 24416667; pTime: 12252740; 
start
time: 7906; startPTime: 0; stopPTime: 24416667; pTime: 13178265; 
time: 8799; startPTime: 0; stopPTime: 24416667; pTime: 14666514; 
time: 9327; startPTime: 0; stopPTime: 24416667; pTime: 15546217; 
time: 10225; startPTime: 0; stopPTime: 24416667; pTime: 17048512; 
time: 10561; startPTime: 0; stopPTime: 24416667; pTime: 17602263; 
time: 10892; startPTime: 0; stopPTime: 24416667; pTime: 18154233; 
time: 11252; startPTime: 0; stopPTime: 24416667; pTime: 18754229; 
time: 11689; startPTime: 0; stopPTime: 24416667; pTime: 19482234; 
time: 12092; startPTime: 0; stopPTime: 24416667; pTime: 20154195; 
time: 12513; startPTime: 0; stopPTime: 24416667; pTime: 20855371; 
time: 12922; startPTime: 0; stopPTime: 24416667; pTime: 21538043; 
time: 13331; startPTime: 0; stopPTime: 24416667; pTime: 22218377; 
time: 14434; startPTime: 0; stopPTime: 24416667; pTime: 24058249; 
time: 14650; startPTime: 0; stopPTime: 24416667; pTime: 24416667; 
time: 14650; startPTime: 0; stopPTime: 24416667; pTime: 24416667; 
time: 14650; startPTime: 0; stopPTime: 24416667; pTime: 24416667; 
time: 14650; startPTime: 0; stopPTime: 24416667; pTime: 24416667; 
*/