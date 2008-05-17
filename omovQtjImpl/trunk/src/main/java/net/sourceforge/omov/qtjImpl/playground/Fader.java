package net.sourceforge.omov.qtjImpl.playground;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Fader
{
	private static final int MIN = 0;
	private static final int MAX = 10;
 
	private Color fadeFrom;
	private Color fadeTo;
 
	private Hashtable backgroundColors = new Hashtable();
 
	/*
	**  The background of any Component added to this Fader
	**  will be set/reset to the fadeFrom color.
	*/
	public Fader(Color fadeTo, Color fadeFrom)
	{
		this(fadeTo);
		this.fadeFrom = fadeFrom;
	}
 
	/*
	**  The original background of any Component added to this Fader
	**  will be preserved.
	*/
	public Fader(Color fadeTo)
	{
		this.fadeTo = fadeTo;
	}
 
	/*
	**  Fading will be applied to this component on gained/lost focus
	*/
	public Fader add(JComponent component)
	{
		//  Set background of all components to the fadeFrom color
 
		if (fadeFrom != null)
			component.setBackground( fadeFrom );
 
		//  Get colors to be used for fading
 
		Vector colors = getColors( component.getBackground() );
 
		//	FaderTimer will apply colors to the component
 
		new FaderTimer( colors, component );
 
		return this;
	}
 
	/*
	**  Get the colors used to fade this background
	*/
	private Vector getColors(Color background)
	{
		//  Check if the color Vector already exists
 
		Object o = backgroundColors.get( background );
 
		if (o != null)
		{
			return (Vector)o;
		}
 
		//  Doesn't exist, create fader colors for this background
 
		int rIncrement = ( background.getRed() - fadeTo.getRed() ) / MAX;
		int gIncrement = ( background.getGreen() - fadeTo.getGreen() ) / MAX;
		int bIncrement = ( background.getBlue() - fadeTo.getBlue() ) / MAX;
 
		Vector colors = new Vector( MAX + 1 );
		colors.addElement( background );
 
		for (int i = 1; i <= MAX; i++)
		{
			int rValue = background.getRed() - (i * rIncrement);
			int gValue = background.getGreen() - (i * gIncrement);
			int bValue = background.getBlue() - (i * bIncrement);
 
			colors.addElement( new Color(rValue, gValue, bValue) );
		}
 
		backgroundColors.put(background, colors);
 
		return colors;
	}
 
	class FaderTimer implements FocusListener, ActionListener
	{
		private Vector colors;
		private JComponent component;
		private Timer timer;
		private int alpha;
		private int increment;
 
		FaderTimer(Vector colors, JComponent component)
		{
			this.colors = colors;
			this.component = component;
			component.addFocusListener( this );
			timer = new Timer(5, this);
		}
 
		public void focusGained(FocusEvent e)
		{
			alpha = MIN;
			increment = 1;
			timer.start();
		}
 
		public void focusLost(FocusEvent e)
		{
			alpha = MAX;
			increment = -1;
			timer.start();
		}
 
		public void actionPerformed(ActionEvent e)
		{
			alpha += increment;
 
			component.setBackground( (Color)colors.elementAt(alpha) );
 
			if (alpha == MAX || alpha == MIN)
				timer.stop();
		}
	}
 
	public static void main(String[] args)
	{
		// Create test components
 
		JComponent textField1 = new JTextField(10);
		JComponent textField3 = new JTextField(10);
		JComponent textField4 = new JTextField(10);
		JComponent button = new JButton("Start");
		JComponent checkBox = new JCheckBox("Check Box");
 
		JFrame frame = new JFrame("Fading Background");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(textField1, BorderLayout.NORTH );
		frame.getContentPane().add(button, BorderLayout.SOUTH );
		frame.getContentPane().add(textField3, BorderLayout.EAST );
		frame.getContentPane().add(textField4, BorderLayout.WEST );
		frame.getContentPane().add(checkBox);
 
		//  Fader preserving component background
		Fader fader = new Fader( new Color(155, 255, 155) );
		fader.add( textField1 );
		fader.add( button );
		fader.add( checkBox );
 
		//  Fader resetting component background
		fader = new Fader( new Color(155, 255, 155), Color.yellow );
		fader.add( textField3 );
		fader.add( textField4 );
 
		frame.pack();
		frame.setVisible( true );
	}
}