package flyingutopia.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import flyingutopia.engine.world.Level;

public class Engine extends JPanel implements KeyListener, Runnable{
	private static final long serialVersionUID = -2525142042121440587L;
	private int lastPressedKey;
	private Level level;
	private double zoom;
	private Sprite focus;
	private ArrayList<Timer> timers;
	private long lastTick;
	private boolean loop;
	private BufferedImage graphics;

	public Engine() {
		timers = new ArrayList<Timer>();
		lastTick = System.currentTimeMillis();
		this.loop = true;
		focus = null;
		zoom = 1.6;
		focus = new Player(32,32);
		graphics = null;
		this.addTimer(focus);
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setFocus(focus);
	}

	public void addTimer(Timer timer) {
		this.timers.add(timer);
	}

	public void removeTimer(Timer timer) {
		this.timers.remove(timer);
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
		if(graphics == null && this.getWidth() > 0 && this.getHeight() > 0) {
			graphics = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			this.requestFocus();
		}
		if(graphics != null) {
			int sx = 0, sy = 0;
			sx = (int)(this.getWidth()/(1/zoom + 2));
			sy = (int)(this.getHeight()/(1/zoom + 2));
			g.drawImage(graphics, 0, 0, this.getWidth(), this.getHeight(),
					sx, sy, this.getWidth() - sx, this.getHeight() - sy, null);
		}
	}

	public void paintBuffer() {
		if(graphics != null) {
			Graphics2D g = graphics.createGraphics();
			int x = 0, y = 0;
			if(focus != null) {
				x = (int)(focus.getX() - this.getWidth()/2);
				y = (int)(focus.getY() - this.getHeight()/2);
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
						x, y, (int)((x + this.getWidth())), (int)((y + this.getHeight())), null);
				
				if(this.level.isColliding(focus.getX() - focus.getWidth()/2, focus.getY()) ||
						this.level.isColliding(focus.getX() + focus.getWidth()/2, focus.getY()) ||
						this.level.isColliding(focus.getX(), focus.getY() + focus.getHeight()/2) ||
						this.level.isColliding(focus.getX(), focus.getY())) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.green);
				}
				g.fillRect(300, 300, 10, 10);
			}
		}
	}
	
	public Level getLevel() {
		return this.level;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			focus.getVelocity().setX(-focus.getSpeed());
			focus.getVelocity().setY(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_RIGHT:
			focus.getVelocity().setX(focus.getSpeed());
			focus.getVelocity().setY(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_UP:
			focus.getVelocity().setY(-focus.getSpeed());
			focus.getVelocity().setX(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_DOWN:
			focus.getVelocity().setY(focus.getSpeed());
			focus.getVelocity().setX(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT: case KeyEvent.VK_RIGHT: case KeyEvent.VK_UP: case KeyEvent.VK_DOWN:
			if(lastPressedKey == arg0.getKeyCode()) {
				focus.getVelocity().setX(0);
				focus.getVelocity().setY(0);
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void run() {
		while(loop) {
			long dt = System.currentTimeMillis() - lastTick;
			for(Timer t: timers) {
				t.onTimer(dt);
			}
			lastTick = System.currentTimeMillis();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				//Thread was not sleepy, doesn't matter
			}
			this.paintBuffer();
			this.repaint();
		}
	}

}
