package flyingutopia.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import flyingutopia.engine.timer.Timer;
import flyingutopia.engine.timer.TimerManager;
import flyingutopia.engine.timer.Timers;
import flyingutopia.engine.world.Level;

public class Engine extends JPanel{
	public static final int INTERACTION_DISTANCE = ImageResources.TILE_SIZE / 3;
	public static final double DEFAULT_ZOOM = 4;
	private static final long serialVersionUID = -2525142042121440587L;
	private Level level;
	private double zoom;
	private Focus focus;
	private ArrayList<WorldCollidable> collidable;
	private BufferedImage graphics;

	public Engine() {
		//Jpanel related
		this.setFocusable(true);
		//Game releated
		collidable = new ArrayList<WorldCollidable>();
		focus = null;
		graphics = null;
		zoom = DEFAULT_ZOOM;
		setupLoops();
	}
	
	private void setupLoops() {
		TimerManager.reset();
		
		TimerManager.addTimer(Timers.MAIN, new Timer(){
			public void onTimer(long millis) {
				for(WorldCollidable w: collidable) {
					if(level != null) {
						w.checkForCollisions(level);
					}
				}
			}
		});
		
		TimerManager.addTimer(Timers.MAIN, new Timer(){
			public void onTimer(long millis) {
				paintBuffer();
				repaint();
			}
		});
	}
	
	public void addCollidable(WorldCollidable w) {
		this.collidable.add(w);
	}
	
	public void removeCollidable(WorldCollidable w) {
		this.collidable.remove(w);
	}

	public void setLevel(Level level) {
		this.level = level;
		this.repaint();
	}

	public void setFocus(Focus focus) {
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
			sx = (int) (this.getWidth()/2 - (this.getWidth()/(zoom * 2)));
			sy = (int) (this.getHeight()/2 - (this.getHeight()/(zoom * 2)));
			g.drawImage(graphics, 0, 0, this.getWidth(), this.getHeight(),
					sx, sy, this.getWidth() - sx, this.getHeight() - sy, null);
		}
	}

	public void paintBuffer() {
		if(graphics != null) {
			Graphics2D g = graphics.createGraphics();
			int x = 0, y = 0, fx = 0, fy = 0;
			if(focus != null) {
				x = (int)(focus.getX() - this.getWidth()/2);
				y = (int)(focus.getY() - this.getHeight()/2);
				fx = (int) focus.getX();
				fy = (int) focus.getY();
			}
			g.setColor(Color.black);
			g.fillRect(0, 0, graphics.getWidth(), graphics.getHeight());
			if(this.level != null) {
				BufferedImage buffer = new BufferedImage(level.getWidth() * ImageResources.TILE_SIZE,
						level.getHeight() * ImageResources.TILE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics bufferGraphics = buffer.getGraphics();
				
				int dx = 0, dy = 0;
				if(x < 0) {
					dx = Math.abs(x);
					x = 0;
				}
				if(y < 0) {
					dy = Math.abs(y);
					y = 0;
				}
				int num_tiles_x = this.getWidth() / (ImageResources.TILE_SIZE * 4);
				int num_tiles_y = this.getHeight() / (ImageResources.TILE_SIZE * 4);
				int player_x = fx / ImageResources.TILE_SIZE;
				int player_y = fy / ImageResources.TILE_SIZE;
				
				this.level.render(bufferGraphics, 
						player_x - num_tiles_x/2 - 1,
						player_x + num_tiles_x/2 + 2,
						player_y - num_tiles_y/2 - 1,
						player_y + num_tiles_y/2 + 2);
				g.drawImage(buffer, dx, dy, this.getWidth() + dx, this.getHeight() + dy,
						x, y, (int)((x + this.getWidth())), (int)((y + this.getHeight())), null);
			}
		}
	}
	
	public Level getLevel() {
		return this.level;
	}

}
