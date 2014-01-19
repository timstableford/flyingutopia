package flyingutopia.engine.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.saj.InvalidSyntaxException;
import flyingutopia.engine.ImageResources;
import flyingutopia.engine.world.Level;

public class Editor extends JFrame implements ComponentListener, AdjustmentListener, ActionListener{
	private static final long serialVersionUID = 3036191526888577204L;
	private static String resourceFile = "res/tiles.json";
	public static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	public static final JdomParser JDOM_PARSER = new JdomParser();
	private ResourcePanel resPanel;
	private ImageResources res;
	private EditorPane editor;
	private TileAttributes attr;
	private Level level;
	private JScrollPane editorScroll;
	public Editor(String[] args) throws FileNotFoundException, InvalidSyntaxException {
		if(args.length>0) {
			resourceFile = args[0];
		}
		res = new ImageResources();
		//Load resources file
	    Scanner scan = new Scanner(new FileInputStream(new File(resourceFile)));
	    String str = scan.useDelimiter("\\A").next();
	    scan.close();
	    res.load(JDOM_PARSER.parse(str));
		
		resPanel = new ResourcePanel(res);
		level = new Level(30,30);
		editor = new EditorPane(resPanel, level);
		attr = new TileAttributes(level, editor);
		this.setSize(640, 480);
		this.setTitle("Map Editor");
		
		//Resource frame
		JFrame resFrame = new JFrame();
		resFrame.setSize(50*4, 600);
		resFrame.setTitle("Resource Viewer");
		resFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JScrollPane pane = new JScrollPane(resPanel);
		pane.setMinimumSize(new Dimension(50*4, 10));
		resFrame.add(pane, BorderLayout.CENTER);
		resFrame.setVisible(true);
		resFrame.addComponentListener(this);
		//Tile attributes frame
		JFrame attrFrame = new JFrame();
		attrFrame.setTitle("Tile Attributes");
		attrFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		attrFrame.add(attr, BorderLayout.CENTER);
		attrFrame.pack();
		attrFrame.setVisible(true);
		
		//Menu bar
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		menu.add(file);
		JMenuItem newLevel = new JMenuItem("New");
		newLevel.setActionCommand("new");
		newLevel.addActionListener(this);
		file.add(newLevel);
		JMenuItem open = new JMenuItem("Open");
		open.setActionCommand("open");
		open.addActionListener(this);
		file.add(open);
		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		file.add(save);
		
		JMenu edit = new JMenu("Edit");
		menu.add(edit);
		JMenuItem paste = new JMenuItem("Special Paste");
		paste.setActionCommand("paste");
		paste.addActionListener(this);
		edit.add(paste);
		
		JMenu view = new JMenu("View");
		menu.add(view);
		JMenuItem zoom = new JMenuItem("Zoom");
		zoom.setActionCommand("zoom");
		zoom.addActionListener(this);
		view.add(zoom);
		this.add(menu, BorderLayout.NORTH);
		
		
		//Main editor
		editorScroll = new JScrollPane(editor,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		editorScroll.getVerticalScrollBar().addAdjustmentListener(this);
		editorScroll.getHorizontalScrollBar().addAdjustmentListener(this);
		this.add(editorScroll, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static String getResourceFile() {
		return resourceFile;
	}
	
	public static void main(String[] args) {
		try {
			new Editor(args);
		} catch (FileNotFoundException | InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("save")) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					FileOutputStream f = new FileOutputStream(file);
					PrintWriter writer = new PrintWriter(f);
					String json = JSON_FORMATTER.format(level.getJson().build());
					writer.write(json);
					writer.flush();
					writer.close();
				} catch(IOException e) {
					System.err.println("Could not save file");
					e.printStackTrace();
				}	
			}
		}else if(arg0.getActionCommand().equals("open")) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
			  	try {
					Scanner s = new Scanner(new FileInputStream(file));
					String json = s.useDelimiter("\\A").next();
					s.close();
					JsonNode node = JDOM_PARSER.parse(json);
					level = new Level(res, node);
					attr.setLevel(level);
					editor.reload(level);
					editorScroll.revalidate();
				} catch (FileNotFoundException | InvalidSyntaxException e) {
					System.err.println("Could not load level");
					e.printStackTrace();
				}
			}
		}else if(arg0.getActionCommand().equals("new")) {
			int width = 0, height = 0;
			String str1 = null;
			String str2 = null;
			do {
				str1 = JOptionPane.showInputDialog(null, "Enter level width", 
					"Level Width", 1);
				if(str1 == null) {
					break;
				}
				try {
					width = Integer.parseInt(str1);
				} catch (NumberFormatException e){}
			} while(width <= 0);
			do {
				str2 = JOptionPane.showInputDialog(null, "Enter level height", 
					"Level height", 1);
				if(str2 == null) {
					break;
				}
				try {
					height = Integer.parseInt(str2);
				} catch (NumberFormatException e){}
			} while(height <= 0);
			if(str1 != null && str2 != null) {
				level = new Level(width, height);
				attr.setLevel(level);
				editor.reload(level);
				editorScroll.revalidate();
			}
		}else if(arg0.getActionCommand().equals("zoom")) {
			int zoom = 0;
			String str = null;
			do {
				str = JOptionPane.showInputDialog(null, "Enter zoom (int)", 
					"Set Zoom", 1);
				try {
					if(str == null) {
						break;
					}
					zoom = Integer.parseInt(str);
				} catch (NumberFormatException e){}
			} while(zoom <= 0);
			if(str != null) {
				editor.setZoom(zoom);
			}
		}else if(arg0.getActionCommand().equals("paste")) {
			int width = 0, height = 0;
			String str1 = null;
			String str2 = null;
			do {
				str1 = JOptionPane.showInputDialog(null, "Enter paste width", 
					"Paste Width", 1);
				if(str1 == null) {
					break;
				}
				try {
					width = Integer.parseInt(str1);
				} catch (NumberFormatException e){}
			} while(width <= 0);
			do {
				str2 = JOptionPane.showInputDialog(null, "Enter paste height", 
					"Paste height", 1);
				if(str2 == null) {
					break;
				}
				try {
					height = Integer.parseInt(str2);
				} catch (NumberFormatException e){}
			} while(height <= 0);
			if(str1 != null && str2 != null) {
				editor.paste(width, height);
			}
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
