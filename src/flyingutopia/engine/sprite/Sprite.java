package flyingutopia.engine.sprite;

import java.awt.Graphics;

import flyingutopia.engine.resources.ImageResource;
import flyingutopia.engine.vector.Vector2D;
import flyingutopia.engine.world.Tile;

public interface Sprite {

	public abstract Sprite newInstance(String[] constructors);
	
	public abstract String getName();

	public abstract void setName(String name);

	public abstract void setStartResource(ImageResource res);

	public abstract void onTimer(long millis);

	public abstract void interact(Tile t);

	public abstract void render(Graphics g);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract double getX();

	public abstract void setX(double x);

	public abstract double getY();

	public abstract void setY(double y);

	public abstract Vector2D getVelocity();

	public abstract double getSpeed();

	public abstract void setSpeed(double speed);
	
	public abstract void setup();
	
	public abstract String getConstructor();

}