// Applet to visualize operations on heaps of integers
//
// CS 201 HW 7
//
// Lucas Shin

import java.applet.*;
import java.awt.*;
import java.util.*; // for Random
import java.awt.event.*;

@SuppressWarnings("serial") // suppress warning in Eclipse about serializable class

public class HeapTest extends Applet implements ActionListener {

	// instance variables for all components that we need access to

	Label titleLabel;
	TextField textField;
	Button addButton, removeButton, clearButton;
	HeapCanvas hc1, hc2;
	Random rand = new Random(); 

	// this initializes the applet
	public void init() {
		// Font to use in applet
		setFont(new Font("TimesRoman", Font.BOLD, 14));

		setLayout(new BorderLayout());
		add("North", makeTitleLabel());  // add at top
		add("South", makeButtonPanel()); // add at bottom
		add("Center", makeHeapPanel());  // add in remaining area (fills space)
	}

	// Creates label to be displayed at top
	public Label makeTitleLabel() {
		titleLabel = new Label("Demonstration of Heap Operations");
		titleLabel.setAlignment(Label.CENTER);
		titleLabel.setBackground(Color.blue);
		titleLabel.setForeground(Color.white);
		return titleLabel;
	}

	// Creates panel with two HeapCanvas objects
	public Panel makeHeapPanel() {
		hc1 = new HeapCanvas(new IntVectorHeap(), "Complete Heap");

		hc2 = new HeapCanvas(new IntSkewHeap(), "Skew Heap");

		Panel heapPanel = new Panel();
		heapPanel.setBackground(Color.white);
		heapPanel.setLayout(new GridLayout(2, 1));  // two rows, one column
		heapPanel.add(hc1);
		heapPanel.add(hc2);
		return heapPanel;
	}

	// Creates panel with three buttons
	public Panel makeButtonPanel() {

		// left button: add (also contains a text field)
		textField = new TextField("50", 3);
		textField.setBackground(Color.yellow);
		addButton = new Button("add");
		addButton.setBackground(Color.yellow);
		addButton.addActionListener(this);
		Panel leftPanel = new Panel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add("West", textField);       // add at left
		leftPanel.add("Center", addButton);     // add in remaining space

		// center button: remove
		removeButton = new Button("remove");
		removeButton.setBackground(Color.cyan);
		removeButton.addActionListener(this);

		// right button: clear
		clearButton = new Button("clear");
		clearButton.setBackground(Color.red);
		clearButton.addActionListener(this);

		Panel buttonPanel = new Panel();
		buttonPanel.setBackground(Color.blue);
		buttonPanel.setLayout(new GridLayout(1, 3)); // one row, three columns
		buttonPanel.add(leftPanel);
		buttonPanel.add(removeButton);
		buttonPanel.add(clearButton);
		return buttonPanel;
	}

	// This method is called by java to handle events
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			int n = Integer.parseInt(textField.getText()); // get number
			hc1.add(n);   // add to both heaps
			hc2.add(n);
			n = Math.abs(rand.nextInt()) % 100; // random number among 0..99
			textField.setText(Integer.toString(n));
		} else if (e.getSource() == removeButton) {
			hc1.remove(); // remove minimum from both heaps
			hc2.remove();
		} else if (e.getSource() == clearButton) {
			hc1.clear();  // clear both heaps
			hc2.clear();
		}
	}
}

// HeapCanvas:  an object that displays a Heap
// Since HeapCanvas is used nowhere else, it can be defined in same file
// (but not public!)

@SuppressWarnings("serial") // suppress warning in Eclipse about serializable class

class HeapCanvas extends Canvas {

	// instance variables
	IntHeap heap;       // the heap displayed on this canvas
	String title;       // what kind of a heap it is

	// Constructor
	public HeapCanvas(IntHeap h, String s) {
		heap = h;
		title = s;
	}

	// add number to the heap and redraw the picture
	public void add(int n) {
		heap.add(n);
		repaint();
	}

	// if possible, remove number from the heap and redraw the picture
	public void remove() {
		if (!heap.isEmpty()) {
			heap.remove();
			repaint();
		}
	}

	// clear the heap and redraw the picture
	public void clear() {
		heap.clear();
		repaint();
	}

	// This method is called by Java when the window is changed (e.g.,
	// uncovered or resized), or when "repaint()" is called.
	public void paint (Graphics g) {
		Dimension d = getSize();        // size of canvas

		g.setColor(Color.black);
		centerString(g, title, d.width/2, 10); // draw title at top

		// draw the heap:
		int radius = 10;
		int dy = 30;
		int y0 = 30;
		int l = radius;
		int r = d.width-radius;
		drawHeap(g, heap, l, r, y0, dy, radius);
	}


	// Draws the heap h at height y with root centered between l and r.
	// The next level is drawn at height y+dy.
	// Each leaf is drawn as a circle with radius rad.
	public static void drawHeap(Graphics g, IntHeap h, int l, int r,
			int y, int dy, int rad) {

		int x = (l + r) / 2;

		g.setColor(Color.black);
		if (h.isEmpty()) {
			g.fillOval(x-3, y-3, 7, 7);
		} else {

			// If the left or right heap is not empty, draws
			// a line to that respective side and draws a 
			// circle with the integer in the middle.
			if (!h.left().isEmpty()) {
				g.drawLine(x, y, (l+x)/2, y+dy);
				drawHeap(g, h.left(), l, x, y+dy, dy, rad);

			} if (!h.right().isEmpty()) {
				g.drawLine(x, y, (x+r)/2, y+dy);
				drawHeap(g, h.right(), x, r, y+dy, dy, rad);
			}
			g.setColor(Color.yellow);
			g.fillOval(x-rad, y-rad, 2*rad, 2*rad);
			g.setColor(Color.black);
			g.drawOval(x-rad,  y-rad,  2*rad,  2*rad);
			g.setColor(Color.black);
			centerString(g, Integer.toString(h.value()), x, y);
		}
	}


	// helper method to draw a String centered at x, y
	public static void centerString(Graphics g, String s, int x, int y) {
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int xs = x - fm.stringWidth(s)/2 + 1;
		int ys = y + fm.getAscent()/3 + 1;
		g.drawString(s, xs, ys);
	}
}
