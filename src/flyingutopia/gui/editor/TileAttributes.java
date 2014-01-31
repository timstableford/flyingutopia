package flyingutopia.gui.editor;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import flyingutopia.engine.world.Level;
import flyingutopia.engine.world.Tile;

public class TileAttributes extends JPanel implements SelectionChangeListener{
	private static final long serialVersionUID = -1689832713205549219L;
	private Level level;
	private EditorPane editor;
	private JCheckBox backgroundSolid, foregroundSolid;
	private JLabel foreground, background;
	private JTextField action, attribute;
	private Tile currentTile;
	
	public TileAttributes(Level level, EditorPane editor) {
		this.editor = editor;
		this.level = level;
		this.editor.setSelectionChangeListener(this);
		this.setLayout(new GridLayout(0, 2));
		currentTile = null;
		
		this.add(new JLabel("Background Solid"));
		backgroundSolid = new JCheckBox();
		backgroundSolid.setEnabled(false);
		this.add(backgroundSolid);
		
		this.add(new JLabel("Foreground Solid"));
		foregroundSolid = new JCheckBox();
		foregroundSolid.setEnabled(false);
		this.add(foregroundSolid);
		
		this.add(new JLabel("Foreground"));
		foreground = new JLabel();
		this.add(foreground);
		this.add(new JLabel("Background"));
		background = new JLabel();
		this.add(background);
		
		this.add(new JLabel("Action"));
		action = new JTextField(20);
		action.setEnabled(false);
		this.add(action);
		
		this.add(new JLabel("Attribute"));
		attribute = new JTextField(20);
		attribute.setEnabled(false);
		this.add(attribute);
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	public void onSelectionChange(int x, int y) {
		if(currentTile != null) {
			currentTile.setForegroundSolid(foregroundSolid.isSelected());
			currentTile.setBackgroundSolid(backgroundSolid.isSelected());
			currentTile.setAction(action.getText(), attribute.getText());
		}
		currentTile = level.getTile(x, y);
		if(currentTile != null) {
			backgroundSolid.setSelected(currentTile.isBackgroundSolid());
			foregroundSolid.setSelected(currentTile.isForegroundSolid());
			foregroundSolid.setEnabled(true);
			backgroundSolid.setEnabled(true);
			if(currentTile.getResource() != null) {
				foreground.setText(currentTile.getResource().getName());
			} else {
				foreground.setText("");
			}
			if(currentTile.getBackground() != null) {
				background.setText(currentTile.getBackground().getName());
			} else {
				background.setText("");
			}
			action.setText(currentTile.getAction());
			action.setEnabled(true);
			attribute.setText(currentTile.getAttribute());
			attribute.setEnabled(true);
		} else {
			backgroundSolid.setSelected(false);
			backgroundSolid.setEnabled(false);
			foregroundSolid.setSelected(false);
			foregroundSolid.setEnabled(false);
			foreground.setText("");
			background.setText("");
			action.setText("");
			action.setEnabled(false);
			attribute.setText("");
			attribute.setEnabled(false);
		}
	}
}
