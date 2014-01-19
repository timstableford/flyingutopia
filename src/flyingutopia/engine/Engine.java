package flyingutopia.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import flyingutopia.engine.world.Level;

public class Engine extends JPanel implements KeyListener{
	private static final long serialVersionUID = -2525142042121440587L;
	private Level level;
	private double zoom;
	private Sprite focus;

	public Engine() {
		focus = null;
		zoom = 2;
		focus = new Sprite(32,32);
		focus.setResource(ImageResources.getInstance().getResource("baby"));
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setFocus(focus);
	}

	public void setLevel(Level level) {
		this.level = level;
		this.repaint();
	}
	
	public void setFocus(Sprite focus) {
		this.focus = focus;
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		this.requestFocus();
		double x = 0, y = 0;
		if(focus != null) {
			x = focus.getX() * zoom - this.getWidth()/2 + ImageResources.TILE_SIZE * zoom;
			y = focus.getY() * zoom - this.getHeight()/2 + ImageResources.TILE_SIZE * zoom;
		}
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(this.level != null) {
			BufferedImage buffer = new BufferedImage(level.getWidth() * ImageResources.TILE_SIZE,
					level.getHeight() * ImageResources.TILE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics bufferGraphics = buffer.getGraphics();
			this.level.render(bufferGraphics);
			if(focus != null) {
				focus.render(bufferGraphics);
			}
			
			g.drawImage(buffer, 0, 0, this.getWidth(), this.getHeight(),
					(int)(x / zoom), (int)(y / zoom), (int)((x + this.getWidth())/zoom), (int)((y + this.getHeight())/zoom), null);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			focus.setX(focus.getX() - 32);
			break;
		case KeyEvent.VK_RIGHT:
			focus.setX(focus.getX() + 32);
			break;
		case KeyEvent.VK_UP:
			focus.setY(focus.getY() - 32);
			break;
		case KeyEvent.VK_DOWN:
			focus.setY(focus.getY() + 32);
			break;
		}
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
