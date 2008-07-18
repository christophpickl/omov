package net.sourceforge.omov.app.playground;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author http://forum.java.sun.com/thread.jspa?threadID=5158520&messageID=9602544
 */
public class MultilineLabel extends JLabel {

	private static final long serialVersionUID = -7025750900063457581L;

	public MultilineLabel() {
		// nothing to do
	}
 
	public MultilineLabel(String arg0) {
		super(arg0);
	}
 
	public MultilineLabel(Icon arg0) {
		super(arg0);
	}
 
	public MultilineLabel(String arg0, int arg1) {
		super(arg0, arg1);
	}
 
	public MultilineLabel(Icon arg0, int arg1) {
		super(arg0, arg1);
	}
 
	public MultilineLabel(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getExactPreferredSize();
	}
	
	public Dimension getExactPreferredSize() {
		Graphics g = getGraphics();
		if (g == null) return super.getPreferredSize();
		Font f = getFont();
		FontMetrics fm = null;
		if (f != null) {
			 fm = g.getFontMetrics(f);
		}
		else { fm = g.getFontMetrics(); }
		int height = fm.getHeight() * countIdealLines();
 
		int idx = -1;
		int newidx =0;
		String txt = getText();
		char[] chars = txt.toCharArray();
		int maxLine = 0;
		while ((newidx = txt.indexOf('\n', idx + 1)) != -1) {
			System.out.println("idx = " + idx + " newidx = " + newidx + "chars = " + chars.length);
			if (idx + 1 == newidx) {
				idx++;
				continue;
			}
			int wid = fm.charsWidth(chars, idx + 1, newidx);
			if (wid > maxLine) {
				maxLine = wid;
			}
			idx = newidx;
			//int diff = newidx - idx - 1;
			//if (diff > maxLine) maxLine = diff;
			//idx = newidx;
		}
		String lastLine = txt.substring(idx + 1);
		//if (idx + 1 != chars.length) {
			//System.out.println("idx = " + idx + " chars - 1 = " + (chars.length - 1));
			//int wid = fm.charsWidth(chars, idx + 1, chars.length -1);
			int wid = fm.stringWidth(lastLine);
			if (wid > maxLine) {
				maxLine = wid;
			}
		//}
		return new Dimension(maxLine, height);
 
	}
	
	public Dimension getRoughPreferredSize() {
		Graphics g = getGraphics();
		if (g == null) return super.getPreferredSize();
		Font f = getFont();
		FontMetrics fm = null;
		if (f != null) {
			 fm = g.getFontMetrics(f);
		}
		else { fm = g.getFontMetrics(); }
		int wid = fm.charWidth('W');
		int len = getLongestLine();
		return new Dimension(wid * len, fm.getHeight() * countIdealLines());
	}
	
	private int getLongestLine() {
		int idx = -1;
		int newidx =0;
		String txt = getText();
		int maxLine = 0;
		while ((newidx = txt.indexOf('\n', idx + 1)) != -1) {
			int diff = newidx - idx - 1;
			if (diff > maxLine) maxLine = diff;
			idx = newidx;
		}
		if (maxLine < txt.length() - idx) {
			maxLine = txt.length() - idx;
		}
		return maxLine;
	}
	
	public int countIdealLines() {
		
//		int newlines = 42;
//		int newlines = StringUtils.count(getText(), '\n') + 1;
//		return newlines; // MINOR countIdealLines isnt really implemented
		return 2;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Color fore = getForeground(), back = getBackground();
		int align = getHorizontalAlignment();
		if (back != null) {
			g.setColor(getBackground());
			g.fillRect(0,0,getWidth(), getHeight());
		}
		if (fore != null) g.setColor(getForeground());
		Font f = getFont();
		FontMetrics fm;
		if (f != null) {
			g.setFont(f);
			fm = g.getFontMetrics(f);
		}
		else fm = g.getFontMetrics();
//		int wid = fm.charWidth('W');
		int width = getWidth();
//		int charsPerLine = getWidth() / wid;
		String txt = getText();
		int height = fm.getHeight();
		int currHeight = height;
		int idx = -1;
		int newidx = 0;
		while ((newidx = txt.indexOf('\n', idx + 1)) != -1) {
			String line = txt.substring(idx + 1, newidx);
			int lineLength = fm.stringWidth(line);
			while (lineLength > width) {
				int idx2 = -1;
				int last = line.length();
				do {
					last = idx2 < 0 ? line.length() : idx2;
					idx2 = line.indexOf(' ', idx2 + 1);
				}
				while (idx2 > -1 && fm.stringWidth(line.substring(0, idx2)) < width);
				String toDraw = line.substring(0, last);
				int diff = width - fm.stringWidth(toDraw);
				g.drawString(toDraw, align == SwingConstants.LEFT ? 0 : align == SwingConstants.CENTER ? diff / 2 : diff, currHeight);
				currHeight += height;
				line = line.substring(last);
				lineLength = fm.stringWidth(line); 
			}
			int diff = width - fm.stringWidth(line);
			g.drawString(line, align == SwingConstants.LEFT ? 0 : align == SwingConstants.CENTER ? diff / 2 : diff, currHeight);
			//g.drawString(line, 0, currHeight);
			currHeight += height;
			idx = newidx;
		}
		String line = txt.substring(idx + 1, txt.length());
		int lineLength = fm.stringWidth(line);
		while (lineLength > width) {
			int idx2 = -1;
			int last = line.length();
			do {
				last = idx2;
				idx2 = line.indexOf(' ', idx2 + 1);
			}
			while (fm.stringWidth(line.substring(0, idx2)) < width);
			
			String toDraw = line.substring(0, last);
			int diff = width - fm.stringWidth(toDraw);
			g.drawString(toDraw, align == SwingConstants.LEFT ? 0 : align == SwingConstants.CENTER ? diff / 2 : diff, currHeight);
			//g.drawString(line.substring(0, last), 0, currHeight);
			currHeight += height;
			line = line.substring(last);
			lineLength = fm.stringWidth(line); 
		}
		int diff = width - fm.stringWidth(line);
		g.drawString(line, align == SwingConstants.LEFT ? 0 : align == SwingConstants.CENTER ? diff / 2 : diff, currHeight);
		//g.drawString(line, 0, currHeight);
		currHeight += height;
 
	}
 
	public static void main(String[] args) {
		MultilineLabel ml = new MultilineLabel("As globalizations occurred and the world moved to 21st century, it seemed as if there was a convergence amongst industrialized nations. That is, a single world culture started to emerge which diffused the national boundaries of national cultures forced by the ineluctable effects of markets. But market forces are not so powerful after all. Market development pose fundamental changes but they leave considerable leeway for governments and corporations to design their own distinctive responses to these challenges.\n\nThese responses have specific relevance to the capitalism of the countries they emerge from and have certain peculiarities. In this paper I will look into three such models and contend the relevance of the Anglo-American model to be more fitting for the 21st century.");
		//MultilineLabel ml = new MultilineLabel("As globalizations occurred and the world moved to 21st century, it seemed as if there was a convergence amongst industrialized nations. That is, a single world culture started to emerge which diffused the national boundaries of national cultures forced by the ineluctable effects of markets. But market forces are not so powerful after all. Market development pose fundamental changes but they leave considerable leeway for governments and corporations to design their own distinctive responses to these challenges.");
		//ml.setFont(new Font("sanserif", 12, 12));
		ml.setBackground(Color.BLUE);
		ml.setForeground(Color.RED);
		ml.setHorizontalAlignment(SwingConstants.RIGHT);
		JFrame jf = new JFrame("Multi-line Test");
		jf.setContentPane(ml);
		jf.setLocation(100,100);
		jf.pack();
		jf.setVisible(true);
		//((JFrame)WindowUtilities.visualize(ml)).setTitle("Multi-line label test");
	}
}