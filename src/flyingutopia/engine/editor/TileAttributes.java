package flyingutopia.engine.editor;

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
	private JCheckBox solid;
	private JLabel foreground, background;
	private JTextField action, attribute;
	private Tile currentTile;
	
	public TileAttributes(Level level, EditorPane editor) {
		this.editor = editor;
		this.level = level;
		this.editor.setSelectionChangeListener(this);
		this.setLayout(new GridLayout(0, 2));
		currentTile = null;
		
		this.add(new JLabel("Solid"));
		solid = new JCheckBox();
		solid.setEnabled(false);
		this.add(solid);
		
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
	
	public void onSelectionChange(int x, int y) {
		if(currentTile != null) {
			currentTile.setSolid(solid.isSelected());
			currentTile.setAction(action.getText(), attribute.getText());
		}
		currentTile = level.getTile(x, y);
		if(currentTile != null) {
			solid.setSelected(currentTile.isSolid());
			solid.setEnabled(true);
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
			solid.setSelected(false);
			solid.setEnabled(false);
			foreground.setText("");
			background.setText("");
			action.setText("");
			action.setEnabled(false);
			attribute.setText("");
			attribute.setEnabled(false);
		}
	}
}
