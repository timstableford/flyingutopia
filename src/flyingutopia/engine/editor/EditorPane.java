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

import flyingutopia.engine.ImageResource;
import flyingutopia.engine.ImageResources;
import flyingutopia.engine.world.Level;
import flyingutopia.engine.world.Tile;

public class EditorPane extends JPanel implements MouseListener, KeyListener{
	private static final long serialVersionUID = 6079786755121339840L;
	private ResourcePanel resPanel;
	private int zoom = 1;
	private Level level;
	private int selectedX, selectedY;
	private int copyX, copyY;
	private SelectionChangeListener onSelectionChange;
	public EditorPane(ResourcePanel resPanel, Level level) {
		this.resPanel = resPanel;
		this.level = level;
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setBorder(new LineBorder(Color.black, 2));
		this.selectedX = this.selectedY = 0;
		this.copyX = this.copyY = 0;
		this.onSelectionChange = null;
		
		this.setPreferredSize(new Dimension(level.getWidth()*ImageResources.TILE_SIZE*zoom, level.getHeight()*ImageResources.TILE_SIZE*zoom));
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.repaint();
	}
	
	public void reload(Level level) {
		this.level = level;
		this.setPreferredSize(new Dimension(level.getWidth()*ImageResources.TILE_SIZE*zoom, level.getHeight()*ImageResources.TILE_SIZE*zoom));
		select(0,0);
	}
	
	public void setSelectionChangeListener(SelectionChangeListener l) {
		this.onSelectionChange = l;
	}
	
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        for(int x=0; x<this.getWidth()/(ImageResources.TILE_SIZE * zoom) && x<level.getWidth(); x++) {
        	for(int y=0; y<this.getHeight()/(ImageResources.TILE_SIZE * zoom) && y<level.getHeight(); y++) {
        		Tile t = level.getTile(x, y);
        		if(t != null) {
        			if(t.getBackground() != null) {
        				g.drawImage(t.getBackground().getImage().getImage(),
        						x*ImageResources.TILE_SIZE*zoom, y*ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom, null);
        			}
        			if(t.getResource() != null) {
        				g.drawImage(t.getResource().getImage().getImage(),
        						x*ImageResources.TILE_SIZE*zoom, y*ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom, null);
        			}
        		}
        	}
        }
        
        g.setColor(Color.black);
        for(int x=0; x<this.getWidth()/(ImageResources.TILE_SIZE * zoom) && x<level.getWidth(); x++) {
        	g.drawLine(x*ImageResources.TILE_SIZE*zoom, 0, x*ImageResources.TILE_SIZE*zoom, level.getHeight()*ImageResources.TILE_SIZE*zoom);
        }
        
        for(int y=0; y<this.getHeight()/(ImageResources.TILE_SIZE * zoom) && y<level.getHeight(); y++) {
        	g.drawLine(0, y*ImageResources.TILE_SIZE*zoom, level.getWidth()*ImageResources.TILE_SIZE*zoom, y*ImageResources.TILE_SIZE*zoom);
        }
        
        g.setColor(Color.green);
        g.drawRect(selectedX*ImageResources.TILE_SIZE*zoom, selectedY*ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom, ImageResources.TILE_SIZE*zoom);
    }
	
	public void select(int x, int y) {
		selectedX = x;
		selectedY = y;
		this.repaint();
		if(this.onSelectionChange != null) {
			this.onSelectionChange.onSelectionChange(x, y);
		}
	}
	
	public void paste(int width, int height) {
		Tile tile = level.getTile(copyX, copyY);
		if(tile != null) {
			for(int x=0; x<width; x++) {
				for(int y=0; y<height; y++) {
					Tile copy = tile.getCopy();
					copy.setX(x + selectedX);
					copy.setY(y + selectedY);
					level.setTile(x + selectedX, y + selectedY, copy);
				}
			}
		} else {
			for(int x=0; x<width; x++) {
				for(int y=0; y<height; y++) {
					level.setTile(x + selectedX, y + selectedY, null);
				}
			}
		}
		this.select(selectedX, selectedY);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x = arg0.getX()/(ImageResources.TILE_SIZE * zoom);
		int y = arg0.getY()/(ImageResources.TILE_SIZE * zoom);
		if(arg0.getButton() == MouseEvent.BUTTON1) {
			ImageResource r = resPanel.getSelectedResource();
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
			ImageResource r = resPanel.getSelectedResource();
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
		if(arg0.getKeyCode() == KeyEvent.VK_DELETE || arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			level.setTile(selectedX, selectedY, null);
			this.repaint();
		}else if((arg0.getKeyCode() == KeyEvent.VK_C) 
				&& ((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			this.copyX = selectedX;
			this.copyY = selectedY;
		}else if((arg0.getKeyCode() == KeyEvent.VK_V)
				&& ((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			Tile t = level.getTile(copyX, copyY);
			if(t != null) {
				Tile nt = t.getCopy();
				nt.setX(selectedX);
				nt.setY(selectedY);
				level.setTile(selectedX, selectedY, nt);
				select(selectedX, selectedY);
			} else {
				level.setTile(selectedX, selectedY, null);
				select(selectedX, selectedY);
			}
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
