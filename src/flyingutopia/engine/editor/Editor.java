package flyingutopia.engine.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import argo.saj.InvalidSyntaxException;
import flyingutopia.engine.Resources;
import flyingutopia.engine.world.Level;

public class Editor extends JFrame implements ComponentListener, AdjustmentListener{
	private static final long serialVersionUID = 3036191526888577204L;
	private ResourcePanel resPanel;
	private Resources res;
	private EditorPane editor;
	public Editor(String[] args) throws FileNotFoundException, InvalidSyntaxException {
		res = new Resources();
		resPanel = new ResourcePanel(res);
		Level lev = new Level(30,30);
		editor = new EditorPane(resPanel, lev);
		this.setSize(640, 480);
		this.setTitle("Map Editor");
		
		//Resource frame
		JFrame resFrame = new JFrame();
		resFrame.setSize(38*4, 600);
		resFrame.setTitle("Resource Viewer");
		JScrollPane pane = new JScrollPane(resPanel);
		pane.setMinimumSize(new Dimension(38*4, 10));
		resFrame.add(pane, BorderLayout.CENTER);
		resFrame.setVisible(true);
		resFrame.addComponentListener(this);
		//Tile attributes frame
		
		JScrollPane editorScroll = new JScrollPane(editor,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		editorScroll.getVerticalScrollBar().addAdjustmentListener(this);
		editorScroll.getHorizontalScrollBar().addAdjustmentListener(this);
		this.add(editorScroll, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		try {
			new Editor(args);
		} catch (FileNotFoundException | InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		editor.repaint();
	}
	
	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		editor.repaint();
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}

}
