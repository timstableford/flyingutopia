package flyingutopia.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import flyingutopia.engine.gui.FlyingUtopia;
import flyingutopia.engine.world.Level;
import flyingutopia.engine.world.Tile;

public class Player extends Sprite implements WorldCollidable, KeyListener{
	protected int lastPressedKey;
	protected Tile lastCollision;
	public Player(double x, double y) {
		super(x, y);
		this.setStartResource(ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("left", ImageResources.getInstance().getResource("boy_left"));
		this.resources.put("down", ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("up", ImageResources.getInstance().getResource("boy_back"));
		this.resources.put("right", ImageResources.getInstance().getResource("boy_right"));
		lastCollision = null;
	}

	@Override
	public boolean checkForCollisions(Level level) {
		boolean colliding = false;
		int x = 0, y = 0;
		if(level.isColliding(getX() - getWidth()/2, getY())) {
			if(this.velocity.getX() < 0) {
				this.velocity.setX(0);
			}
			x = (int) (getX() - getWidth()/2);
			y = (int) getY();
			colliding = true;
		}
		if(level.isColliding(getX() + getWidth()/2, getY())) {
			if(this.velocity.getX() > 0) {
				this.velocity.setX(0);
			}
			x = (int) (getX() + getWidth()/2);
			y = (int) getY();
			colliding = true;
		}
		if(level.isColliding(getX(), getY() + getHeight()/2)) {
			if(this.velocity.getY() > 0) {
				this.velocity.setY(0);
			}
			x = (int) (getX());
			y = (int) (getY() + getHeight()/2);
			colliding = true;
		}
		if(level.isColliding(getX(), getY() - getHeight()/6)) {
			if(this.velocity.getY() < 0) {
				this.velocity.setY(0);
			}
			x = (int) (getX());
			y = (int) (getY() + getHeight()/2);
			colliding = true;
		}
		if(colliding) {
			Tile t = level.getTile(x / ImageResources.TILE_SIZE, y / ImageResources.TILE_SIZE);
			if(t != null && t != lastCollision) {
				if(lastCollision != null) {
					lastCollision.onSeperate(this);
				}
				t.onCollision(this);
				lastCollision = t;
			}
		} else if(lastCollision != null) {
			lastCollision.onSeperate(this);
			lastCollision = null;
		}
		return colliding;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		Level level = FlyingUtopia.getInstance().getEngine().getLevel();
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			this.getVelocity().setX(-this.getSpeed());
			this.getVelocity().setY(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_RIGHT:
			this.getVelocity().setX(this.getSpeed());
			this.getVelocity().setY(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_UP:
			this.getVelocity().setY(-this.getSpeed());
			this.getVelocity().setX(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_DOWN:
			this.getVelocity().setY(this.getSpeed());
			this.getVelocity().setX(0);
			lastPressedKey = arg0.getKeyCode();
			break;
		case KeyEvent.VK_ENTER: case KeyEvent.VK_E:
			double x = this.getX();
			double y = this.getY();
			switch(this.getVelocity().getDirection()) {
			case LEFT:
				x -= Engine.INTERACTION_DISTANCE;
				break;
			case RIGHT:
				x += Engine.INTERACTION_DISTANCE;
				break;
			case UP:
				y -= Engine.INTERACTION_DISTANCE;
				break;
			case DOWN:
				y += Engine.INTERACTION_DISTANCE;
				break;
			default:
				return;
			}
			if(level != null) {
				Tile t = level.getTile((int)(x / ImageResources.TILE_SIZE), (int)(y / ImageResources.TILE_SIZE));
				if(t != null) {
					this.interact(t);
				}
			}
			break;
		}
		if(level != null) {
			this.checkForCollisions(level);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_LEFT: case KeyEvent.VK_RIGHT: case KeyEvent.VK_UP: case KeyEvent.VK_DOWN:
			if(lastPressedKey == arg0.getKeyCode()) {
				this.getVelocity().setX(0);
				this.getVelocity().setY(0);
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
