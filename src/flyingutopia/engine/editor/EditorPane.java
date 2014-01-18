package flyingutopia.engine.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import flyingutopia.engine.Resource;
import flyingutopia.engine.world.Level;
import flyingutopia.engine.world.Tile;

public class EditorPane extends JPanel implements MouseListener, KeyListener{
	private static final int TILE_SIZE = 32;
	private static final long serialVersionUID = 6079786755121339840L;
	private ResourcePanel resPanel;
	private Level level;
	private int selectedX, selectedY;
	private SelectionChangeListener onSelectionChange;
	public EditorPane(ResourcePanel resPanel, Level level) {
		this.resPanel = resPanel;
		this.level = level;
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setBorder(new LineBorder(Color.black, 2));
		this.selectedX = this.selectedY = 0;
		this.onSelectionChange = null;
		
		this.setPreferredSize(new Dimension(level.getWidth()*TILE_SIZE, level.getHeight()*TILE_SIZE));
	}
	
	public void setSelectionChangeListener(SelectionChangeListener l) {
		this.onSelectionChange = l;
	}
	
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        for(int x=0; x<this.getWidth()/TILE_SIZE && x<level.getWidth(); x++) {
        	for(int y=0; y<this.getHeight()/TILE_SIZE && y<level.getHeight(); y++) {
        		Tile t = level.getTile(x, y);
        		if(t != null) {
        			if(t.getBackground() != null) {
        				g.drawImage(t.getBackground().getImage().getImage(), x*TILE_SIZE, y*TILE_SIZE, null);
        			}
        			if(t.getResource() != null) {
        				g.drawImage(t.getResource().getImage().getImage(), x*TILE_SIZE, y*TILE_SIZE, null);
        			}
        		}
        	}
        }
        
        g.setColor(Color.black);
        for(int x=0; x<this.getWidth()/TILE_SIZE && x<level.getWidth(); x++) {
        	g.drawLine(x*TILE_SIZE, 0, x*TILE_SIZE, level.getHeight()*TILE_SIZE);
        }
        
        for(int y=0; y<this.getHeight()/TILE_SIZE && y<level.getHeight(); y++) {
        	g.drawLine(0, y*TILE_SIZE, level.getWidth()*TILE_SIZE, y*TILE_SIZE);
        }
        
        g.setColor(Color.green);
        g.drawRect(selectedX*TILE_SIZE, selectedY*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
	
	public void select(int x, int y) {
		selectedX = x;
		selectedY = y;
		this.repaint();
		if(this.onSelectionChange != null) {
			this.onSelectionChange.onSelectionChange(x, y);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x = arg0.getX()/TILE_SIZE;
		int y = arg0.getY()/TILE_SIZE;
		if(arg0.getButton() == MouseEvent.BUTTON1) {
			Resource r = resPanel.getSelectedResource();
			if(r != null) {
				Tile t;
				if(level.getTile(x, y) == null) {
					t = new Tile(x, y);
					level.setTile(x, y, t);
				} else {
					t = level.getTile(x, y);
				}
				t.setResource(r);
				this.repaint();
			} else {
				select(x, y);
			}
			
		}else if(arg0.getButton() == MouseEvent.BUTTON3) {
			Resource r = resPanel.getSelectedResource();
			if(r != null) {
				Tile t;
				if(level.getTile(x, y) == null) {
					t = new Tile(x, y);
					level.setTile(x, y, t);
				} else {
					t = level.getTile(x, y);
				}
				t.setBackground(r);
				this.repaint();
			} else {
				select(x, y);
			}
		}else if(arg0.getButton() == MouseEvent.BUTTON2) {
			select(x, y);
		}
		if(selectedX == x && selectedY == y) {
			select(x, y);
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_DELETE) {
			level.setTile(selectedX, selectedY, null);
			this.repaint();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {} 
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}

}
